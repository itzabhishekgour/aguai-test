package com.aguai.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "assigned_tasks")
@Data
@NoArgsConstructor  // 🔥 Hibernate ke liye default empty constructor banayega
@AllArgsConstructor // 🔥 Saare parameters wala constructor banayega
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private Long projectId; // projects table se link karne ke liye

    private String fileName; // e.g., index.html, MenuCard.java

    private String assignedCoderId; // e.g., AI-5, AI-12

    private String fileStatus; // ASSIGNED, CODING, REVIEW, DONE

    @Column(columnDefinition = "TEXT")
    private String generatedCode; // AI ka generate kiya hua backup code

    // Custom helper constructor humare manual logic ke liye
    public Task(Long projectId, String fileName, String assignedCoderId, String fileStatus) {
        this.projectId = projectId;
        this.fileName = fileName;
        this.assignedCoderId = assignedCoderId;
        this.fileStatus = fileStatus;
    }
}