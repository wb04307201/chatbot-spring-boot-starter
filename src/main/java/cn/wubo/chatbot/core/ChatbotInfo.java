package cn.wubo.chatbot.core;

import lombok.Data;

@Data
public class ChatbotInfo {
    private String alias;
    private ChatbotType chatbotType;
    private String token;
    private String secret;
}
