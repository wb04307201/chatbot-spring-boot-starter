package cn.wubo.chatbot.entity;

import cn.wubo.chatbot.entity.enums.SubLineEnum;
import lombok.Data;

@Data
public abstract class SubLine {
    protected SubLineEnum lineType;
    protected String content;
}
