package cn.wubo.chatbot.platform;

import cn.wubo.chatbot.core.ChatbotInfo;
import cn.wubo.chatbot.message.MarkdownContent;
import cn.wubo.chatbot.message.TextContent;
import cn.wubo.chatbot.core.ChatbotType;

public interface ISendService {

    /**
     * 支持平台
     * @param chatbotType
     * @return
     */
    Boolean support(ChatbotType chatbotType);

    /**
     * 发送文本
     * @param chatbotInfo
     * @param content
     * @return
     */
    String sendText(ChatbotInfo chatbotInfo, TextContent content);

    /**
     * 发送markdown
     * @param chatbotInfo
     * @param content
     * @return
     */
    String sendMarkDown(ChatbotInfo chatbotInfo, MarkdownContent content);
}
