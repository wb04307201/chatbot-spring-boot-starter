package cn.wubo.chatbot.message;

public class SubBoldLine extends SubLine {

    public SubBoldLine(String content) {
        this.content = content;
        this.lineType = SubLineEnum.BOLD;
    }
}
