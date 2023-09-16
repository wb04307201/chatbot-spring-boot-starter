package cn.wubo.message.core;

public enum MessageType {

    DINGTALK("DINGTALK", "https://oapi.dingtalk.com/robot/send?access_token=%s&timestamp=%s&sign=%s"),
    WEIXIN("WEIXIN", "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s"),
    FEISHU("FEISHU", "https://open.feishu.cn/open-apis/bot/v2/hook/%s"),
    MAIL("MAIL", "smtp"),
    ;

    private final String type;
    private final String webhook;

    MessageType(String type, String webhook) {
        this.type = type;
        this.webhook = webhook;
    }

    public String getWebhook(){
        return webhook;
    }
    public String getType(){
        return type;
    }
}
