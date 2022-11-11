package cn.wubo.chatbot.entity;

import lombok.Data;

@Data
public class ChatbotHistory {
    private String id;
    private String type;
    private String request;
    private String response;
}
