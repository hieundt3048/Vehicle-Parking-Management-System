package com.parking.system.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.parking.system.dto.CreateZoneRequest;
import com.parking.system.entity.ParkingZone;
import com.parking.system.entity.User;
import com.parking.system.repository.ParkingZoneRepository;
import com.parking.system.repository.UserRepository;
import com.parking.system.service.ParkingZoneService;
import com.parking.system.service.UserService;

/**
 * Khởi tạo dữ liệu cấu hình cơ bản cho hệ thống khi chạy lần đầu.
 *
 * Lưu ý: chỉ tạo khu vực đỗ xe nếu hiện tại chưa có bản ghi nào trong bảng
 * parking_zones. Vé xe và doanh thu vẫn hoàn toàn do người dùng thao tác.
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final ParkingZoneRepository parkingZoneRepository;
    private final ParkingZoneService parkingZoneService;
    private final UserRepository userRepository;
    private final UserService userService;

    public DatabaseInitializer(ParkingZoneRepository parkingZoneRepository,
                               ParkingZoneService parkingZoneService,
                               UserRepository userRepository,
                               UserService userService) {
        this.parkingZoneRepository = parkingZoneRepository;
        this.parkingZoneService = parkingZoneService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        // ===== Tạo tài khoản mẫu =====
        // Nếu chưa có user nào trong hệ thống, tạo 2 tài khoản mẫu
        if (userRepository.count() == 0) {
            try {
                // Tạo tài khoản Admin
                userService.createUser("admin", "admin123", "Quản Trị Viên", User.Role.ADMIN);
                
                // Tạo tài khoản Nhân viên
                userService.createUser("nhanvien", "nhanvien123", "Nhân Viên Bãi Xe", User.Role.EMPLOYEE);
            } catch (Exception e) {
                System.err.println("Lỗi khi tạo tài khoản mẫu: " + e.getMessage());
            }
        }
        
        // ===== Tạo khu vực đỗ xe mẫu =====
        // Nếu đã có cấu hình khu vực rồi thì không làm gì thêm
        if (parkingZoneRepository.count() > 0) {
            return;
        }

        // Tạo mặc định Khu A cho xe máy
        CreateZoneRequest motorbikeZone = new CreateZoneRequest();
        motorbikeZone.setName("A");
        motorbikeZone.setVehicleType(ParkingZone.VehicleType.MOTORBIKE);
        motorbikeZone.setTotalSlots(20);
        parkingZoneService.createZone(motorbikeZone);

        // Tạo mặc định Khu B cho ô tô
        CreateZoneRequest carZone = new CreateZoneRequest();
        carZone.setName("B");
        carZone.setVehicleType(ParkingZone.VehicleType.CAR);
        carZone.setTotalSlots(12);
        parkingZoneService.createZone(carZone);
    }
}
