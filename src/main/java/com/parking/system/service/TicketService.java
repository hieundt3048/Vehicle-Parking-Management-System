package com.parking.system.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.parking.system.dto.CreateTicketRequest;
import com.parking.system.entity.ParkingSlot;
import com.parking.system.entity.ParkingZone;
import com.parking.system.entity.Ticket;
import com.parking.system.exception.InvalidRequestException;
import com.parking.system.exception.ResourceNotFoundException;
import com.parking.system.repository.ParkingSlotRepository;
import com.parking.system.repository.ParkingZoneRepository;
import com.parking.system.repository.TicketRepository;
import com.parking.system.service.factory.TicketFactory;

@Service
public class TicketService {
    
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingZoneRepository parkingZoneRepository;
    private final FeeCalculationService feeCalculationService;
    private final TicketFactory ticketFactory;

    public TicketService(TicketRepository ticketRepository,
                         ParkingSlotRepository parkingSlotRepository,
                         ParkingZoneRepository parkingZoneRepository,
                         FeeCalculationService feeCalculationService,
                         TicketFactory ticketFactory) {
        this.ticketRepository = ticketRepository;
        this.parkingSlotRepository = parkingSlotRepository;
        this.parkingZoneRepository = parkingZoneRepository;
        this.feeCalculationService = feeCalculationService;
        this.ticketFactory = ticketFactory;
    }
    
    /**
     * Tạo vé xe mới
     */
    @Transactional
    public Ticket createTicket(CreateTicketRequest request) {
        // ============================================
        // BƯỚC 1: VALIDATE DỮ LIỆU ĐẦU VÀO
        // ============================================
        
        if (request.getLicensePlate() == null || request.getLicensePlate().trim().isEmpty()) {
            throw new InvalidRequestException("Biển số xe không được để trống");
        }
        
        if (request.getVehicleType() == null) {
            throw new InvalidRequestException("Loại xe không được để trống");
        }
        
        if (request.getZoneId() == null) {
            throw new InvalidRequestException("Vui lòng chọn khu vực đỗ xe");
        }
        
        // ============================================
        // BƯỚC 2: VALIDATE ZONE TỒN TẠI
        // ============================================
        
        ParkingZone zone = parkingZoneRepository.findById(request.getZoneId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Không tìm thấy khu vực với ID: " + request.getZoneId()
            ));
        
        // Kiểm tra loại xe có khớp với zone không
        if (zone.getVehicleType() != request.getVehicleType()) {
            throw new InvalidRequestException(
                "Loại xe " + request.getVehicleType() + 
                " không phù hợp với khu vực " + zone.getName() + 
                " (chỉ dành cho " + zone.getVehicleType() + ")"
            );
        }
        
        // ============================================
        // BƯỚC 3: KIỂM TRA XE ĐÃ GỬI TRONG BÃI CHƯA
        // ============================================
        
        List<Ticket> existingTickets = ticketRepository.findByLicensePlateAndStatus(
            request.getLicensePlate(), Ticket.Status.ACTIVE
        );

        if (!existingTickets.isEmpty()) {
            Ticket existingTicket = existingTickets.get(0);
            throw new InvalidRequestException(
                "Xe có biển số " + request.getLicensePlate() +
                " đang gửi trong bãi (Vé #" + existingTicket.getId() +
                ", Slot: " + existingTicket.getSlot().getSlotNumber() + ")"
            );
        }
        
        // ============================================
        // BƯỚC 4: TÌM SLOT TRỐNG VỚI PESSIMISTIC LOCK
        // ============================================
        // Sử dụng pessimistic lock để đảm bảo chỉ 1 thread có thể lấy slot này
        // Database sẽ lock row cho đến khi transaction kết thúc
        
        ParkingSlot availableSlot = parkingSlotRepository
            .findFirstByZoneIdAndStatusOrderBySlotNumberAsc(request.getZoneId(), ParkingSlot.Status.AVAILABLE)
            .orElseThrow(() -> new RuntimeException(
                "Khu vực " + zone.getName() + " đã hết chỗ trống. " +
                "Vui lòng chọn khu vực khác hoặc quay lại sau."
            ));
        
        // ============================================
        // BƯỚC 5: TẠO VÉ MỚI VÀ CẬP NHẬT SLOT
        // ============================================
        
        // Tạo vé mới sử dụng Factory Method Pattern
        // Factory sẽ đảm nhiệm việc khởi tạo và chuẩn hóa dữ liệu
        Ticket ticket = ticketFactory.createTicket(
            request.getLicensePlate(),
            request.getVehicleType(),
            availableSlot,
            LocalDateTime.now()
        );
        
        // Cập nhật trạng thái slot NGAY LẬP TỨC (quan trọng để tránh race condition)
        availableSlot.setStatus(ParkingSlot.Status.OCCUPIED);
        parkingSlotRepository.save(availableSlot);
        
        // Lưu vé xuống database
        Ticket savedTicket = ticketRepository.save(ticket);
        
        // Log thông tin (nếu cần debug)
        logger.info("Check-in thành công: Vé #{}, Biển số: {}, Slot: {}, Zone: {}",
            savedTicket.getId(),
            savedTicket.getLicensePlate(),
            availableSlot.getSlotNumber(),
            zone.getName());
        
        return savedTicket;
    }
    
    /**
     * Xử lý xuất bãi (Check-out)
     * 
     * @param ticketId ID của vé cần checkout
     * @return Ticket đã được xử lý
     */
    @Transactional
    public Ticket processExit(Long ticketId) {
        // Tìm vé theo ID
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vé với ID: " + ticketId));
        
        // Kiểm tra vé đã checkout chưa
        if (ticket.getStatus() == Ticket.Status.COMPLETED) {
            throw new InvalidRequestException("Vé này đã được thanh toán và xuất bãi rồi");
        }
        
        // Kiểm tra nếu vé chưa có giờ vào (trường hợp bất thường)
        if (ticket.getEntryTime() == null) {
            throw new InvalidRequestException("Vé không hợp lệ: không có thời gian vào");
        }
        
        // Set giờ ra
        LocalDateTime exitTime = LocalDateTime.now();
        ticket.setExitTime(exitTime);
        
        // Tính tiền gửi xe
        Double totalAmount = feeCalculationService.calculateFee(
            ticket.getEntryTime(), 
            exitTime, 
            ticket.getVehicleType()
        );
        ticket.setTotalAmount(totalAmount);
        
        // Cập nhật trạng thái vé
        ticket.setStatus(Ticket.Status.COMPLETED);
        
        // Cập nhật trạng thái slot về trống
        ParkingSlot slot = ticket.getSlot();
        if (slot != null) {
            slot.setStatus(ParkingSlot.Status.AVAILABLE);
            parkingSlotRepository.save(slot);
        }
        
        // Lưu lại thông tin vé
        return ticketRepository.save(ticket);
    }
    
    /**
     * Lấy tất cả vé
     */
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
    
    /**
     * Lấy vé theo ID
     */
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vé với ID: " + id));
    }
    
    /**
     * Lấy vé đang hoạt động theo biển số
     */
    public Ticket getActiveTicketByLicensePlate(String licensePlate) {
        List<Ticket> tickets = ticketRepository.findByLicensePlateAndStatus(licensePlate, Ticket.Status.ACTIVE);

        if (tickets.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy vé đang hoạt động cho xe: " + licensePlate);
        }

        if (tickets.size() > 1) {
            throw new InvalidRequestException(
                "Phát hiện nhiều vé đang hoạt động cho biển số: " + licensePlate +
                ". Vui lòng kiểm tra và đóng các vé dư thừa trước."
            );
        }

        return tickets.get(0);
    }
    
    /**
     * Lấy tất cả vé đang hoạt động
     */
    public List<Ticket> getActiveTickets() {
        return ticketRepository.findByStatus(Ticket.Status.ACTIVE);
    }
}
