package cn.wubo.chatbot.entity;

import cn.wubo.chatbot.entity.enums.SubLineEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class SubLinkLine extends SubLine {

    public SubLinkLine(String content, String link) {
        this.content = content;
        this.link = link;
        this.lineType = SubLineEnum.LINK;
    }

    @Getter
    private String link;
}
