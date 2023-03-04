package cn.wubo.chatbot.message;

import cn.wubo.chatbot.core.ChatbotType;
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

    public TextContent addAlias(String alias) {
        this.alias.add(alias);
        return this;
    }

    public TextContent addChatbotType(ChatbotType chatbotType) {
        this.chatbotType.add(chatbotType);
        return this;
    }
}
