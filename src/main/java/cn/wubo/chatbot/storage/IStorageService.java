package cn.wubo.chatbot.storage;

import java.util.List;

public interface IStorageService {

    /**
     * 保存
     *
     * @param chatbotHistory 发送记录
     * @return ChatbotHistory
     */
    ChatbotHistory save(ChatbotHistory chatbotHistory);

    /**
     * 查询
     *
     * @param chatbotHistory 查询信息
     * @return List<ChatbotHistory>
     */
    List<ChatbotHistory> list(ChatbotHistory chatbotHistory);

    /**
     * 初始化
     */
    void init();
}
