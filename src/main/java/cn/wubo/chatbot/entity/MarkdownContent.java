package cn.wubo.chatbot.entity;

import lombok.Getter;

import java.util.List;

public class MarkdownContent extends RequestContent {
    @Getter
    private String title;
    @Getter
    List<SubLine> lines;

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
}
