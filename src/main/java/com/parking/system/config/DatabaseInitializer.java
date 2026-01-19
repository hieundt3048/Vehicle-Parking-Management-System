package com.parking.system.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.parking.system.dto.CreateZoneRequest;
import com.parking.system.entity.ParkingZone;
import com.parking.system.repository.ParkingZoneRepository;
import com.parking.system.service.ParkingZoneService;

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

    public DatabaseInitializer(ParkingZoneRepository parkingZoneRepository,
                               ParkingZoneService parkingZoneService) {
        this.parkingZoneRepository = parkingZoneRepository;
        this.parkingZoneService = parkingZoneService;
    }

    @Override
    public void run(String... args) throws Exception {
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
