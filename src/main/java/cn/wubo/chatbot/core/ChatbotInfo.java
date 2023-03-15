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
    private String password;
    private String host;

    public static ChatbotInfo dingtalk(String alias, String token, String secret) {
        ChatbotInfo chatbotInfo = new ChatbotInfo();
        chatbotInfo.setAlias(alias);
        chatbotInfo.setToken(token);
        chatbotInfo.setSecret(secret);
        chatbotInfo.setChatbotType(ChatbotType.DINGTALK);
        return chatbotInfo;
    }

    public static ChatbotInfo mail(String to, String from, String host, String password) {
        ChatbotInfo chatbotInfo = new ChatbotInfo();
        chatbotInfo.setChatbotType(ChatbotType.MAIL);
        return chatbotInfo;
    }
}
