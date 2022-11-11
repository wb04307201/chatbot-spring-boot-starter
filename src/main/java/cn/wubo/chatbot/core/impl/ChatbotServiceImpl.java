package cn.wubo.chatbot.core.impl;

import cn.wubo.chatbot.config.ChatbotConfigurationProperties;
import cn.wubo.chatbot.core.IChatbotService;
import cn.wubo.chatbot.core.chatbot.ISendService;
import cn.wubo.chatbot.entity.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ChatbotServiceImpl implements IChatbotService {

    ChatbotConfigurationProperties properties;

    public ChatbotServiceImpl(ChatbotConfigurationProperties properties) {
        this.properties = properties;
    }

    @Autowired
    List<ISendService> services;

    @Override
    public List<String> send(RequestContent content) {
        List<String> strings = new ArrayList<>();
        properties.getChatbotInfo().forEach(chatbotInfo -> {
            services.stream()
                    .filter(service -> service.support(chatbotInfo))
                    .findAny()
                    .ifPresent(service -> {
                        if (content instanceof TextContent)
                            strings.add(service.sendText(chatbotInfo, (TextContent) content));
                        else if (content instanceof MarkdownContent)
                            strings.add(service.sendMarkDown(chatbotInfo, (MarkdownContent) content));
                    });
        });
        return strings;
    }
}
