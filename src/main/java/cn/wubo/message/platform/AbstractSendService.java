package cn.wubo.message.platform;

import cn.wubo.message.core.MessageBase;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.record.IMessageRecordService;
import cn.wubo.message.record.MessageRecord;
import com.alibaba.fastjson.JSON;

public abstract class AbstractSendService<T extends MessageBase> implements ISendService<T> {
    protected IMessageRecordService messageRecordService;

    protected AbstractSendService(IMessageRecordService messageRecordService) {
        this.messageRecordService = messageRecordService;
    }

    public String send(T aliasProperties, Object content) {
        MessageRecord messageRecord = this.beforeSend(JSON.toJSONString(content));
        String res;
        if (content instanceof MarkdownContent) {
            res = doSendMarkdown(aliasProperties, (MarkdownContent) content);
        } else if (content instanceof TextContent) {
            res = doSendText(aliasProperties, (TextContent) content);
        } else {
            throw new RuntimeException("");
        }
        messageRecord.setResponse(res);
        afterSend(messageRecord);
        return res;
    }

    public MessageRecord beforeSend(String content) {
        return null;
    }

    public void afterSend(MessageRecord messageRecord) {

    }
}
