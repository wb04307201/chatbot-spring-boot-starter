package cn.wubo.chatbot.core;

import lombok.Data;

@Data
public class ChatbotInfo {
    private String alias;
    private ChatbotType chatbotType;
    private String token;
    private String secret;
    private String from;
    private String to;
    private String host;
    private String username;
    private String password;
}
