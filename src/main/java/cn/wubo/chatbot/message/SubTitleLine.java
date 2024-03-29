package cn.wubo.chatbot.message;

import lombok.Getter;

public class SubTitleLine extends SubLine {

    @Getter
    private Integer level;

    public SubTitleLine(String content,Integer level){
        this.lineType = SubLineEnum.TITLE;
        this.content = content;
        this.level = level;
    }
}
