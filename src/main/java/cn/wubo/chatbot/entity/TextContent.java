package cn.wubo.chatbot.entity;

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
}
