package cn.wubo.chatbot.core;

import cn.wubo.chatbot.entity.ChatbotInfo;
import cn.wubo.chatbot.entity.RequestContent;

import java.util.List;

public interface IChatbotService {
    List<String> send(RequestContent content);
}
