package cn.wubo.chatbot.entity;

import cn.wubo.chatbot.entity.enums.SubLineEnum;
import lombok.Getter;

public class SubLinkLine extends SubLine{

    @Getter
    private String content;

    @Getter
    private String link;

    public SubLinkLine(){
        this.lineType = SubLineEnum.LINK;
    }

    public SubLinkLine link(String content,String link){
        this.content = content;
        this.link = link;
        return this;
    }
}
