package cn.wubo.chatbot.entity;

import cn.wubo.chatbot.entity.enums.ChatbotType;
import lombok.Data;

@Data
public class ChatbotInfo {
    private ChatbotType chatbotType;
    private String token;
    private String secret;
}
