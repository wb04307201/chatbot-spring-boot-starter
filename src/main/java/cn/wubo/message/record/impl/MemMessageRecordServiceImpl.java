package cn.wubo.message.record.impl;

import cn.wubo.message.record.MessageRecord;
import cn.wubo.message.record.IMessageRecordService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MemMessageRecordServiceImpl implements IMessageRecordService {

    private static List<MessageRecord> chatbotHistories = new ArrayList<>();

    @Override
    public MessageRecord save(MessageRecord messageRecord) {
        if (StringUtils.hasLength(messageRecord.getId())) {
            chatbotHistories.stream()
                    .filter(e -> e.getId().equals(messageRecord.getId()))
                    .findAny()
                    .ifPresent(e -> e = messageRecord);
        } else {
            messageRecord.setId(UUID.randomUUID().toString());
            chatbotHistories.add(messageRecord);
        }
        return messageRecord;
    }

    @Override
    public List<MessageRecord> list(MessageRecord messageRecord) {
        return chatbotHistories.stream()
                .filter(e -> !StringUtils.hasLength(messageRecord.getType()) || messageRecord.getType().equals(e.getType()))
                .filter(e -> !StringUtils.hasLength(messageRecord.getRequest()) || e.getRequest().contains(messageRecord.getRequest()))
                .filter(e -> !StringUtils.hasLength(messageRecord.getResponse()) || e.getResponse().contains(messageRecord.getResponse()))
                .filter(e -> !StringUtils.hasLength(messageRecord.getAlias()) || e.getAlias().contains(messageRecord.getAlias()))
                .collect(Collectors.toList());
    }

    @Override
    public void init() {
    }
}
