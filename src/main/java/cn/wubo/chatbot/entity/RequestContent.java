package cn.wubo.chatbot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public abstract class RequestContent {
    protected Boolean isAll;

    public static RequestContent buildText(){
        return new TextContent();
    }

    public static RequestContent buildMarkdown(){
        return new MarkdownContent();
    }
}
