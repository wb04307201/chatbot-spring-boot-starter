package cn.wubo.message.core;

import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.RequestContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.ISendService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageService {
    CopyOnWriteArrayList<MessageBase> aliases;
    CopyOnWriteArrayList<ISendService<?>> sendServices;

    public MessageService(CopyOnWriteArrayList<MessageBase> aliases, CopyOnWriteArrayList<ISendService<?>> sendServices) {
        this.aliases = aliases;
        this.sendServices = sendServices;
    }

    public List<String> send(RequestContent<?> content) {
        List<String> strings = new ArrayList<>();
        aliases.forEach(info -> {
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

    private void add(MessageBase messageBase) {
        aliases.stream().filter(e -> e.getAlias().equals(messageBase.getAlias())).findAny().ifPresent(e -> aliases.remove(e));
        aliases.add(messageBase);
    }

    public void removeByAlias(String alias) {
        aliases.stream().filter(e -> e.getAlias().equals(alias)).findAny().ifPresent(e -> aliases.remove(e));
    }
}
