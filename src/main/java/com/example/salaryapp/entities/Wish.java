package com.example.salaryapp.entities;

import com.example.salaryapp.entities.enums.WishType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "wishes")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private WishType type;

    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    @Column(name = "endTime")
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "fixed")
    private Boolean fixed;

    public void addToEmployee(Employee employee) {
        if (employee.equals(this.employee)) return;
        if (this.employee != null ) {
            this.employee.getWishes().remove(this);
        }
        employee.getWishes().add(this);
        this.employee = employee;
    }

    public void removeFromEmployee() {
        if (this.employee != null) {
            this.employee.getWishes().remove(this);
            this.employee = null;
        }
    }
}
