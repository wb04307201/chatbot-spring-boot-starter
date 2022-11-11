package cn.wubo.chatbot.core.impl;

import cn.wubo.chatbot.core.IChatbotService;
import cn.wubo.chatbot.core.chatbot.ISendService;
import cn.wubo.chatbot.entity.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ChatbotServiceImpl implements IChatbotService {

    @Autowired
    List<ISendService> services;

    @Override
    public List<String> sendText(List<ChatbotInfo> chatbotInfos, RequestContent content) {
        List<String> strings = new ArrayList<>();
        if (content instanceof TextContent) {
            chatbotInfos.forEach(chatbotInfo -> {
                services.stream()
                        .filter(service -> service.support(chatbotInfo))
                        .findAny()
                        .ifPresent(service -> strings.add(service.sendText(chatbotInfo, (TextContent) content)));
            });
        } else
            throw new RuntimeException("发送群消息【文本类型】格式不匹配!");
        return strings;
    }

    @Override
    public List<String> sendMarkDown(List<ChatbotInfo> chatbotInfos, RequestContent content) {
        List<String> strings = new ArrayList<>();
        if (content instanceof MarkdownContent) {
            chatbotInfos.forEach(chatbotInfo -> {
                services.stream()
                        .filter(service -> service.support(chatbotInfo))
                        .findAny()
                        .ifPresent(service -> strings.add(service.sendMarkDown(chatbotInfo, (MarkdownContent) content)));
            });
        } else
            throw new RuntimeException("发送群消息【MD类型】格式不匹配!");
        return strings;
    }
}
