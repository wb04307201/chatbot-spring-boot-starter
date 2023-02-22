package cn.wubo.chatbot.entity;

import cn.wubo.chatbot.entity.enums.ChatbotType;
import lombok.Getter;

public class TextContent extends RequestContent {
    @Getter
    private String text;

    public TextContent atAll(boolean atAll){
        this.isAll = atAll;
        return this;
    }

    public TextContent text(String text){
        this.text = text;
        return this;
    }

    public TextContent addPlatform(String platform) {
        this.platform.add(platform);
        return this;
    }

    public TextContent addChatbotType(ChatbotType chatbotType) {
        this.chatbotType.add(chatbotType);
        return this;
    }
}
