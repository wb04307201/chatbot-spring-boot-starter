package cn.wubo.chatbot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class MarkdownContent extends RequestContent {
    private String title;
    List<SubLine> lines;

    public RequestContent addLine(SubLine line){
        this.lines.add(line);
        return this;
    }
}
