package cn.wubo.chatbot.platform;

import cn.wubo.chatbot.core.ChatbotInfo;
import cn.wubo.chatbot.message.MarkdownContent;
import cn.wubo.chatbot.message.TextContent;
import cn.wubo.chatbot.core.ChatbotType;

public interface ISendService {

    Boolean support(ChatbotType chatbotType);

    String sendText(ChatbotInfo chatbotInfo, TextContent content);

    String sendMarkDown(ChatbotInfo chatbotInfo, MarkdownContent content);
}
