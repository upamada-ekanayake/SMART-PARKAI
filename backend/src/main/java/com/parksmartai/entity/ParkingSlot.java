package com.parksmartai.entity;

import com.parksmartai.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parking_slots", uniqueConstraints = @UniqueConstraint(columnNames = {"parking_lot_id", "slotNumber"}))
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String slotNumber;

    @Column(nullable = false)
    private String slotType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot;
}
