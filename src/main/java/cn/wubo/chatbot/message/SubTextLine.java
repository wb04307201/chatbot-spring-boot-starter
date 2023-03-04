package cn.wubo.chatbot.message;

public class SubTextLine extends SubLine {

    public SubTextLine(String content) {
        this.content = content;
        this.lineType = SubLineEnum.TEXT;
    }
}
