package com.project.workflow.controller;

import com.project.workflow.enums.MessageType;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private String content;
    private String sender;
    private MessageType messageType;
}
