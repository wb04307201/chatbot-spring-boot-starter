package cn.wubo.chatbot.storage;

import java.util.List;

public interface IStorageService {

    ChatbotHistory save(ChatbotHistory chatbotHistory);

    List<ChatbotHistory> list(ChatbotHistory chatbotHistory);

    void init();
}
