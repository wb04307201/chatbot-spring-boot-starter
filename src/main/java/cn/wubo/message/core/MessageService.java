package cn.wubo.message.core;

import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.RequestContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.ISendService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageService {

    CopyOnWriteArrayList<MessageInfo> infos;
    CopyOnWriteArrayList<ISendService> sendServices;

    public MessageService(List<MessageInfo> infos, List<ISendService> sendServices) {
        this.infos = new CopyOnWriteArrayList<>(infos);
        this.sendServices = new CopyOnWriteArrayList<>(sendServices);
    }

    public List<String> send(RequestContent content) {
        List<String> strings = new ArrayList<>();
        infos.forEach(info -> {
            sendServices.stream()
                    .filter(service -> service.support(info.getMessageType()) && content.getMessageType().isEmpty() || content.getMessageType().stream().anyMatch(service::support))
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

    private void add(MessageInfo messageInfo) {
        infos.stream().filter(e -> e.getAlias().equals(messageInfo.getAlias())).findAny().ifPresent(e -> infos.remove(e));
        infos.add(messageInfo);
    }

    public void addDingtalk(String alias, String token, String secret) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setAlias(alias);
        messageInfo.setToken(token);
        messageInfo.setSecret(secret);
        messageInfo.setMessageType(MessageType.DINGTALK);
        add(messageInfo);
    }

    public void addWeixin(String alias, String token) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setAlias(alias);
        messageInfo.setToken(token);
        messageInfo.setMessageType(MessageType.WEIXIN);
        add(messageInfo);
    }

    public void addFeishu(String alias, String token, String secret) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setAlias(alias);
        messageInfo.setToken(token);
        messageInfo.setSecret(secret);
        messageInfo.setMessageType(MessageType.FEISHU);
        add(messageInfo);
    }

    public void addMail(String from, String to, String host, String username, String password) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setFrom(from);
        messageInfo.setTo(to);
        messageInfo.setHost(host);
        messageInfo.setUsername(username);
        messageInfo.setPassword(password);
        messageInfo.setMessageType(MessageType.MAIL);
        add(messageInfo);
    }

    public void removeByAlias(String alias) {
        infos.stream().filter(e -> e.getAlias().equals(alias)).findAny().ifPresent(e -> infos.remove(e));
    }
}
