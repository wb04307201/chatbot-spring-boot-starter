package cn.wubo.chatbot.message;

public class SubQuoteLine  extends SubLine{

    public SubQuoteLine(String content) {
        this.content = content;
        this.lineType = SubLineEnum.QUOTE;
    }
}
