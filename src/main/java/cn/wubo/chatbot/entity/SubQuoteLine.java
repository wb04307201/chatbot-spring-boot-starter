package cn.wubo.chatbot.entity;

import cn.wubo.chatbot.entity.enums.SubLineEnum;

public class SubQuoteLine  extends SubLine{

    public SubQuoteLine(String content) {
        this.content = content;
        this.lineType = SubLineEnum.QUOTE;
    }
}
