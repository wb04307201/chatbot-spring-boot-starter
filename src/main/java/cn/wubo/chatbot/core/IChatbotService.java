package cn.wubo.chatbot.core;

import cn.wubo.chatbot.message.RequestContent;

import java.util.List;

public interface IChatbotService {
    List<String> send(RequestContent content);
}
