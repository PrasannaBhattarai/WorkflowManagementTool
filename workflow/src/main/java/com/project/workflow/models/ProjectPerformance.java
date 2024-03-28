package com.project.workflow.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ProjectPerformanceId.class)
@Table(name = "ProjectPerformance")
public class ProjectPerformance {

    @Id
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "projectId", referencedColumnName = "projectId")
    private Project project;

    @Column(name = "performanceRank")
    private int performanceRank;


}
