package cn.wubo.chatbot.platform;

import cn.wubo.chatbot.entity.ChatbotInfo;
import cn.wubo.chatbot.entity.MarkdownContent;
import cn.wubo.chatbot.entity.TextContent;
import cn.wubo.chatbot.entity.enums.ChatbotType;

public interface ISendService {

    Boolean support(ChatbotType chatbotType);

    String sendText(ChatbotInfo chatbotInfo, TextContent content);

    String sendMarkDown(ChatbotInfo chatbotInfo, MarkdownContent content);
}
