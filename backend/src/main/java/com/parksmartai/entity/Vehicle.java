package com.parksmartai.entity;

import com.parksmartai.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicles", indexes = {
        @Index(name = "idx_vehicles_number", columnList = "vehicleNumber", unique = true),
        @Index(name = "idx_vehicles_user", columnList = "user_id")
})
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String vehicleNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;

    private String brand;
    private String model;
    private String color;
}
