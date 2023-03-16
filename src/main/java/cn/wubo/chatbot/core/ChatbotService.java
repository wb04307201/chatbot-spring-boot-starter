package cn.wubo.chatbot.core;

import cn.wubo.chatbot.message.MarkdownContent;
import cn.wubo.chatbot.message.RequestContent;
import cn.wubo.chatbot.message.TextContent;
import cn.wubo.chatbot.platform.ISendService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatbotService {

    CopyOnWriteArrayList<ChatbotInfo> infos;
    CopyOnWriteArrayList<ISendService> sendServices;

    public ChatbotService(List<ChatbotInfo> infos, List<ISendService> sendServices) {
        this.infos = new CopyOnWriteArrayList<>(infos);
        this.sendServices = new CopyOnWriteArrayList<>(sendServices);
    }

    public List<String> send(RequestContent content) {
        List<String> strings = new ArrayList<>();
        infos.forEach(info -> {
            sendServices.stream()
                    .filter(service -> service.support(info.getChatbotType()) && content.getChatbotType().isEmpty() || content.getChatbotType().stream().anyMatch(service::support))
                    .filter(service -> content.getAlias().isEmpty() || content.getAlias().stream().anyMatch(e -> info.getAlias().equals(e)))
                    .forEach(service -> {
                        if (content instanceof TextContent)
                            strings.add(service.sendText(info, (TextContent) content));
                        else if (content instanceof MarkdownContent)
                            strings.add(service.sendMarkDown(info, (MarkdownContent) content));
                    });
        });
        return strings;
    }

    private void add(ChatbotInfo chatbotInfo) {
        infos.stream().filter(e -> e.getAlias().equals(chatbotInfo.getAlias())).findAny().ifPresent(e -> infos.remove(e));
        infos.add(chatbotInfo);
    }

    public void addDingtalk(String alias, String token, String secret) {
        ChatbotInfo chatbotInfo = new ChatbotInfo();
        chatbotInfo.setAlias(alias);
        chatbotInfo.setToken(token);
        chatbotInfo.setSecret(secret);
        chatbotInfo.setChatbotType(ChatbotType.DINGTALK);
        add(chatbotInfo);
    }

    public void addWeixin(String alias, String token) {
        ChatbotInfo chatbotInfo = new ChatbotInfo();
        chatbotInfo.setAlias(alias);
        chatbotInfo.setToken(token);
        chatbotInfo.setChatbotType(ChatbotType.WEIXIN);
        add(chatbotInfo);
    }

    public void addFeishu(String alias, String token, String secret) {
        ChatbotInfo chatbotInfo = new ChatbotInfo();
        chatbotInfo.setAlias(alias);
        chatbotInfo.setToken(token);
        chatbotInfo.setSecret(secret);
        chatbotInfo.setChatbotType(ChatbotType.FEISHU);
        add(chatbotInfo);
    }

    public void addMail(String from, String to, String host, String username, String password) {
        ChatbotInfo chatbotInfo = new ChatbotInfo();
        chatbotInfo.setFrom(from);
        chatbotInfo.setTo(to);
        chatbotInfo.setHost(host);
        chatbotInfo.setUsername(username);
        chatbotInfo.setPassword(password);
        chatbotInfo.setChatbotType(ChatbotType.MAIL);
        add(chatbotInfo);
    }

    public void removeByAlias(String alias) {
        infos.stream().filter(e -> e.getAlias().equals(alias)).findAny().ifPresent(e -> infos.remove(e));
    }
}
