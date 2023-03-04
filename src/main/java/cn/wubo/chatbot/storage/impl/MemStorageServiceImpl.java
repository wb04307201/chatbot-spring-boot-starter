package cn.wubo.chatbot.storage.impl;

import cn.wubo.chatbot.storage.ChatbotHistory;
import cn.wubo.chatbot.storage.IStorageService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MemStorageServiceImpl implements IStorageService {

    private static List<ChatbotHistory> chatbotHistories = new ArrayList<>();

    @Override
    public ChatbotHistory save(ChatbotHistory chatbotHistory) {
        if (StringUtils.hasLength(chatbotHistory.getId())) {
            chatbotHistories.stream()
                    .filter(e -> e.getId().equals(chatbotHistory.getId()))
                    .findAny()
                    .ifPresent(e -> e = chatbotHistory);
        } else {
            chatbotHistory.setId(UUID.randomUUID().toString());
            chatbotHistories.add(chatbotHistory);
        }
        return chatbotHistory;
    }

    @Override
    public List<ChatbotHistory> list(ChatbotHistory chatbotHistory) {
        return chatbotHistories.stream()
                .filter(e -> !StringUtils.hasLength(chatbotHistory.getType()) || chatbotHistory.getType().equals(e.getType()))
                .filter(e -> !StringUtils.hasLength(chatbotHistory.getRequest()) || e.getRequest().contains(chatbotHistory.getRequest()))
                .filter(e -> !StringUtils.hasLength(chatbotHistory.getResponse()) || e.getResponse().contains(chatbotHistory.getResponse()))
                .filter(e -> !StringUtils.hasLength(chatbotHistory.getAlias()) || e.getAlias().contains(chatbotHistory.getAlias()))
                .collect(Collectors.toList());
    }

    @Override
    public void init() {

    }
}
