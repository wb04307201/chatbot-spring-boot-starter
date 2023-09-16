package cn.wubo.message.message;

import cn.wubo.message.core.MessageType;
import lombok.Getter;

import java.util.Arrays;

public class TextContent extends RequestContent {
    @Getter
    private String text;

    public TextContent text(String text){
        this.text = text;
        return this;
    }

    public TextContent addAlias(String... alias) {
        this.alias.add(Arrays.toString(alias));
        return this;
    }

    public TextContent addChatbotType(MessageType messageType) {
        this.messageType.add(messageType);
        return this;
    }
}
