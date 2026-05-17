package com.aguai.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor  // 🔥 Hibernate ke liye empty constructor automatic banayega
@AllArgsConstructor // 🔥 Saare fields wala constructor banayega
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false)
    private String projectName;

    @Column(columnDefinition = "TEXT")
    private String briefDescription;

    private String status; // PLANNING, IN_PROGRESS, COMPLETED

    private LocalDateTime createdAt;

    // Custom helper constructor safe creation ke liye
    public Project(String projectName, String briefDescription, String status) {
        this.projectName = projectName;
        this.briefDescription = briefDescription;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }
}