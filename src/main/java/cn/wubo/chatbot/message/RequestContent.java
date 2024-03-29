package cn.wubo.chatbot.message;

import cn.wubo.chatbot.core.ChatbotType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class RequestContent {

    @Getter
    protected boolean isAll;

    @Getter
    protected List<String> alias = new ArrayList<>();

    @Getter
    protected List<ChatbotType> chatbotType = new ArrayList<>();

    public static TextContent buildText() {
        return new TextContent();
    }

    public static MarkdownContent buildMarkdown() {
        return new MarkdownContent();
    }
}
