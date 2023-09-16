package cn.wubo.message.message;

import cn.wubo.message.core.MessageType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class RequestContent {

    @Getter
    protected List<String> alias = new ArrayList<>();

    @Getter
    protected List<MessageType> messageType = new ArrayList<>();

    public static TextContent buildText() {
        return new TextContent();
    }

    public static MarkdownContent buildMarkdown() {
        return new MarkdownContent();
    }
}
