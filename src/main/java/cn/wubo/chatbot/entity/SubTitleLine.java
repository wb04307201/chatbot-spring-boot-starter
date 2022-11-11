package cn.wubo.chatbot.entity;

import cn.wubo.chatbot.entity.enums.SubLineEnum;
import lombok.Getter;

public class SubTitleLine extends SubLine {

    @Getter
    private Integer level;

    public SubTitleLine(){
        this.lineType = SubLineEnum.TITLE;
        this.level = 1;
    }

    public SubTitleLine title(String content,Integer level){
        this.content = content;
        this.level = level;
        return this;
    }
}
