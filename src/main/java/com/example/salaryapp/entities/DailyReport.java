package com.example.salaryapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "daily_reports")
public class DailyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "dailyReport", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "date_of_created")
    private Date dateOfCreated;

}
