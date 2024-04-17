package com.project.workflow.controller;

import com.project.workflow.enums.MessageType;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private String content;
    private String sender;
    private MessageType messageType;
    private String token;
    private Long projectId;
    private LocalDateTime chatDateTime;
}
