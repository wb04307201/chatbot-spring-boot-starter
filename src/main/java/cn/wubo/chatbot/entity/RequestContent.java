package cn.wubo.chatbot.entity;

import lombok.Getter;

public abstract class RequestContent {

    @Getter
    protected boolean isAll;

    public static TextContent buildText(){
        return new TextContent();
    }

    public static MarkdownContent buildMarkdown(){
        return new MarkdownContent();
    }
}
