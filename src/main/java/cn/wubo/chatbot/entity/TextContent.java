package cn.wubo.chatbot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TextContent extends RequestContent {
    private String text;
}
