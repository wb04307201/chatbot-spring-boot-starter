package cn.wubo.chatbot.entity;

import cn.wubo.chatbot.entity.enums.SubLineEnum;

public class SubTextLine extends SubLine {

    public SubTextLine() {
        this.lineType = SubLineEnum.TEXT;
    }

    public SubTextLine text(String content) {
        this.content = content;
        return this;
    }
}
