package com.parksmartai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parking_lots")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int totalSlots;

    @Column(nullable = false)
    private int availableSlots;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingSlot> slots = new ArrayList<>();
    
    public static ParkingLotBuilder builder() {
        return new ParkingLotBuilder();
    }
    
    public static class ParkingLotBuilder {
        private Long id;
        private String name;
        private String address;
        private String description;
        private int totalSlots;
        private int availableSlots;

        public ParkingLotBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ParkingLotBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ParkingLotBuilder address(String address) {
            this.address = address;
            return this;
        }

        public ParkingLotBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ParkingLotBuilder totalSlots(int totalSlots) {
            this.totalSlots = totalSlots;
            return this;
        }

        public ParkingLotBuilder availableSlots(int availableSlots) {
            this.availableSlots = availableSlots;
            return this;
        }

        public ParkingLot build() {
            ParkingLot lot = new ParkingLot();
            lot.id = this.id;
            lot.name = this.name;
            lot.address = this.address;
            lot.description = this.description;
            lot.totalSlots = this.totalSlots;
            lot.availableSlots = this.availableSlots;
            lot.slots = new ArrayList<>();
            return lot;
        }
    }
    
    public void addSlot(ParkingSlot slot) {
        if (slots == null) {
            slots = new ArrayList<>();
        }
        slot.setParkingLot(this);
        slots.add(slot);
    }
}
