package cn.wubo.message.message;

import cn.wubo.message.core.MessageType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class RequestContent<T> {
    protected List<String> alias = new ArrayList<>();
    protected List<MessageType> messageType = new ArrayList<>();
    protected ContentParams params = new ContentParams();

    public static TextContent buildText() {
        return new TextContent();
    }

    public static MarkdownContent buildMarkdown() {
        return new MarkdownContent();
    }

    public T addAlias(String... alias) {
        this.alias.add(Arrays.toString(alias));
        return (T) this;
    }

    public T addMessageType(MessageType messageType) {
        this.messageType.add(messageType);
        return (T) this;
    }

    public T addDingtalkCustomRobot(String key, Object value) {
        this.params.getDingtalkCustomRobot().put(key, value);
        return (T) this;
    }

    public T addDingtalkMessage(String key, Object value) {
        this.params.getDingtalkMessage().put(key, value);
        return (T) this;
    }

    public T addFeishuCustomRobot(String key, Object value) {
        this.params.getFeishuCustomRobot().put(key, value);
        return (T) this;
    }

    public T addFeishuMessage(String key, Object value) {
        this.params.getFeishuMessage().put(key, value);
        return (T) this;
    }

    public T addWeixinMessage(String key, Object value) {
        this.params.getWeixinMessage().put(key, value);
        return (T) this;
    }

    public T addMailSmtp(String key, Object value) {
        this.params.getMailSmtp().put(key, value);
        return (T) this;
    }
}
