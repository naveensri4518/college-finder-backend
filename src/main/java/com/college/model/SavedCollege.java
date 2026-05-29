package com.college.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saved_colleges",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "college_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedCollege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "college_id", nullable = false)
    private Long collegeId;
}
