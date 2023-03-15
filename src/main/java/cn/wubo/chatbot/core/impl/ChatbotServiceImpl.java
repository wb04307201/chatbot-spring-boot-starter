package cn.wubo.chatbot.core.impl;

import cn.wubo.chatbot.core.ChatbotInfo;
import cn.wubo.chatbot.core.IChatbotService;
import cn.wubo.chatbot.message.MarkdownContent;
import cn.wubo.chatbot.message.RequestContent;
import cn.wubo.chatbot.message.TextContent;
import cn.wubo.chatbot.platform.ISendService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatbotServiceImpl implements IChatbotService {

    CopyOnWriteArrayList<ChatbotInfo> infos;
    CopyOnWriteArrayList<ISendService> sendServices;

    public ChatbotServiceImpl(List<ChatbotInfo> infos, List<ISendService> sendServices) {
        this.infos = new CopyOnWriteArrayList<>(infos);
        this.sendServices = new CopyOnWriteArrayList<>(sendServices);
    }

    @Override
    public List<String> send(RequestContent content) {
        List<String> strings = new ArrayList<>();
        infos.forEach(info -> {
            sendServices.stream()
                    .filter(service -> service.support(info.getChatbotType()) && content.getChatbotType().isEmpty() || content.getChatbotType().stream().anyMatch(service::support))
                    .filter(service -> content.getAlias().isEmpty() || content.getAlias().stream().anyMatch(e -> info.getAlias().equals(e)))
                    .findAny()
                    .ifPresent(service -> {
                        if (content instanceof TextContent)
                            strings.add(service.sendText(info, (TextContent) content));
                        else if (content instanceof MarkdownContent)
                            strings.add(service.sendMarkDown(info, (MarkdownContent) content));
                    });
        });
        return strings;
    }

    @Override
    public void add(ChatbotInfo chatbotInfo) {
        infos.stream().filter(e -> e.getAlias().equals(chatbotInfo.getAlias())).findAny().ifPresent(e -> infos.remove(e));
        infos.add(chatbotInfo);
    }

    @Override
    public void removeByAlias(String alias) {
        infos.stream().filter(e -> e.getAlias().equals(alias)).findAny().ifPresent(e -> infos.remove(e));
    }
}
