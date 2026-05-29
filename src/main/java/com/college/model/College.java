package com.college.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "colleges")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;

    private Double fees;

    private Double rating;

    private Double placements;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image;

    @Column(columnDefinition = "TEXT")
    private String courses;

    @Column(columnDefinition = "TEXT")
    private String facilities;

    @Column(columnDefinition = "TEXT")
    private String reviews;
}
