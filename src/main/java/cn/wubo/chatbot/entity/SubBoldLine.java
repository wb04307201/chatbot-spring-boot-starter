package cn.wubo.chatbot.entity;

import cn.wubo.chatbot.entity.enums.SubLineEnum;

public class SubBoldLine extends SubLine {

    public SubBoldLine(String content) {
        this.content = content;
        this.lineType = SubLineEnum.BOLD;
    }
}
