package cn.wubo.message.platform;

import cn.wubo.message.core.MessageBase;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.record.MessageRecord;

public interface ISendService<T extends MessageBase> {

    String send(T aliasProperties, Object content);

    MessageRecord beforeSend(String content);

    String doSendMarkdown(T aliasProperties, MarkdownContent content);

    String doSendText(T aliasProperties, TextContent content);

    void afterSend(MessageRecord messageRecord);
}
