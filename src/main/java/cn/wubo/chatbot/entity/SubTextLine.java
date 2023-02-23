package cn.wubo.chatbot.entity;

import cn.wubo.chatbot.entity.enums.SubLineEnum;

public class SubTextLine extends SubLine {

    public SubTextLine(String content) {
        this.content = content;
        this.lineType = SubLineEnum.QUOTE;
    }
}
