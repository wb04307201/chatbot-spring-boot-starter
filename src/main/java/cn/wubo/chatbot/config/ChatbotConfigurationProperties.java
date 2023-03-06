package cn.wubo.chatbot.config;

import cn.wubo.chatbot.core.ChatbotInfo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "chatbot.config")
public class ChatbotConfigurationProperties {

    private String storageClass = "cn.wubo.chatbot.record.impl.MemChatbotRecordImpl";
    private List<ChatbotInfo> chatbotInfo = new ArrayList<>();
}
