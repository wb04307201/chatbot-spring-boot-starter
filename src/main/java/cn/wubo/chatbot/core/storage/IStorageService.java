package cn.wubo.chatbot.core.storage;

import cn.wubo.chatbot.entity.ChatbotHistory;

import java.util.List;

public interface IStorageService {

    ChatbotHistory save(ChatbotHistory chatbotHistory);

    List<ChatbotHistory> list();
}
