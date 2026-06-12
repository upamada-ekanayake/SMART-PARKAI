package com.parksmartai.config;

import com.parksmartai.entity.*;
import com.parksmartai.enums.*;
import com.parksmartai.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seed(UserRepository users, ParkingLotRepository lots, ParkingSlotRepository slots, VehicleRepository vehicles) {
        return args -> {
            try {
                if (users.count() > 0) {
                    return;
                }
                User admin = users.save(User.builder()
                        .firstName("Admin")
                        .lastName("Manager")
                        .email("admin@parksmart.ai")
                        .password(passwordEncoder.encode("admin123"))
                        .phone("+100000000")
                        .role(Role.ROLE_ADMIN)
                        .vip(true)
                        .createdAt(LocalDateTime.now())
                        .build());
                User user = users.save(User.builder()
                        .firstName("Demo")
                        .lastName("User")
                        .email("user@parksmart.ai")
                        .password(passwordEncoder.encode("user123"))
                        .phone("+200000000")
                        .role(Role.ROLE_USER)
                        .vip(false)
                        .createdAt(LocalDateTime.now())
                        .build());
                
                // Create parking lot
                ParkingLot lot = new ParkingLot();
                lot.setName("North Campus Parking");
                lot.setAddress("University Avenue");
                lot.setDescription("Covered parking near lecture halls");
                lot.setTotalSlots(2);
                lot.setAvailableSlots(2);
                ParkingLot savedLot = lots.save(lot);
                
                // Create slots separately and link to lot
                ParkingSlot slot1 = new ParkingSlot();
                slot1.setSlotNumber("A-01");
                slot1.setSlotType("STANDARD");
                slot1.setStatus(SlotStatus.AVAILABLE);
                slot1.setParkingLot(savedLot);
                slots.save(slot1);
                
                ParkingSlot slot2 = new ParkingSlot();
                slot2.setSlotNumber("A-02");
                slot2.setSlotType("EV");
                slot2.setStatus(SlotStatus.AVAILABLE);
                slot2.setParkingLot(savedLot);
                slots.save(slot2);
                
                vehicles.save(Vehicle.builder().user(user).vehicleNumber("PSA-1001").vehicleType(VehicleType.CAR).brand("Toyota").model("Prius").color("White").build());
                vehicles.save(Vehicle.builder().user(admin).vehicleNumber("VIP-0001").vehicleType(VehicleType.EV).brand("Tesla").model("Model 3").color("Black").build());
                System.out.println("Database seeded successfully!");
            } catch (Exception e) {
                System.err.println("Error seeding database: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
