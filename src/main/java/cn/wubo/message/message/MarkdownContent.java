package cn.wubo.message.message;

import cn.wubo.message.core.MessageType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarkdownContent extends RequestContent {
    @Getter
    private String title;
    @Getter
    List<SubLine> lines = new ArrayList<>();

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

    public MarkdownContent addChatbotType(MessageType messageType) {
        this.messageType.add(messageType);
        return this;
    }
}
