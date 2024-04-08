package com.project.workflow.models.response;

import com.project.workflow.models.Task;
import com.project.workflow.models.dto.UserEmails;

import java.util.List;

public class TaskForUsers {

    private List<UserEmails> emails;
    private Task task;

    public List<UserEmails> getEmails() {
        return emails;
    }

    public void setEmails(List<UserEmails> emails) {
        this.emails = emails;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
