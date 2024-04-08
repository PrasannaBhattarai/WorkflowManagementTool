package com.project.workflow.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ToDoList")
public class ToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "toDoListId")
    private Long toDoListId;

    @Column(name = "toDoItemDescription")
    private String toDoItemDescription;

    @Column(name = "toDoListStatus")
    private String toDoListStatus;

    public void setToDoListId(Long toDoListId) {
        this.toDoListId = toDoListId;
    }

    public void setToDoItemDescription(String toDoItemDescription) {
        this.toDoItemDescription = toDoItemDescription;
    }

    public void setToDoListStatus(String toDoListStatus) {
        this.toDoListStatus = toDoListStatus;
    }
}
