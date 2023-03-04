package cn.wubo.chatbot.storage;

import lombok.Data;

import java.util.Date;

@Data
public class ChatbotHistory {
    private String id;
    private String type;
    private String request;
    private String response;
    private String alias;
    private Date createTime = new Date();
}