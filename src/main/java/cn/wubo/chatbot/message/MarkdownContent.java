package cn.wubo.chatbot.message;

import cn.wubo.chatbot.core.ChatbotType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarkdownContent extends RequestContent {
    @Getter
    private String title;
    @Getter
    List<SubLine> lines = new ArrayList<>();

    public MarkdownContent atAll(boolean atAll){
        this.isAll = atAll;
        return this;
    }

    public MarkdownContent title(String title){
        this.title = title;
        return this;
    }

    public MarkdownContent addLine(SubLine line){
        this.lines.add(line);
        return this;
    }

    public MarkdownContent addAlias(String... alias) {
        this.alias.addAll(Arrays.asList(alias));
        return this;
    }

    public MarkdownContent addChatbotType(ChatbotType chatbotType) {
        this.chatbotType.add(chatbotType);
        return this;
    }
}
