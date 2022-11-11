package cn.wubo.chatbot.core;

import cn.wubo.chatbot.entity.ChatbotInfo;
import cn.wubo.chatbot.entity.RequestContent;

import java.util.List;

public interface IChatbotService {
    List<String> sendText(List<ChatbotInfo> chatbotInfos, RequestContent content);

    List<String> sendMarkDown(List<ChatbotInfo> chatbotInfos, RequestContent content);
}
