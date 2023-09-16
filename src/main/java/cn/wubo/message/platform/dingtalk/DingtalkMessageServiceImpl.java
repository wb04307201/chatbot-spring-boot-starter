package cn.wubo.message.platform.dingtalk;

import cn.wubo.message.core.DingtalkProperties;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.record.IMessageRecordService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DingtalkMessageServiceImpl extends AbstractSendService<DingtalkProperties.Message> {

    protected DingtalkMessageServiceImpl(IMessageRecordService messageRecordService) {
        super(messageRecordService);
    }

    @Override
    public String doSendMarkdown(DingtalkProperties.Message aliasProperties, MarkdownContent content) {
        return null;
    }

    @Override
    public String doSendText(DingtalkProperties.Message aliasProperties, TextContent content) {
        return null;
    }
}
