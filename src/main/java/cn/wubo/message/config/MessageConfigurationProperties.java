package cn.wubo.message.config;

import cn.wubo.message.core.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "message")
public class MessageConfigurationProperties {

    private String messageRecord = "cn.wubo.chatbot.record.impl.MemChatbotRecordImpl";
    private List<MessageInfo> alias = new ArrayList<>();

    private DingtalkProperties dingtalk = new DingtalkProperties();
    private FeishuProperties feishu = new FeishuProperties();
    private WeixinPrpperties weixin = new WeixinPrpperties();
    private MailProperties mail = new MailProperties();

}
