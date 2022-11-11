package cn.wubo.chatbot.entity.enums;

public enum ChatbotType {

    DINGTALK("钉钉", "https://oapi.dingtalk.com/robot/send?access_token=%s&timestamp=%s&sign=%s"),
    WEIXIN("企业微信", "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s"),
    FEISHU("飞书", "https://open.feishu.cn/open-apis/bot/v2/hook/"),
    ;

    private String type;
    private String webhook;

    ChatbotType(String type, String webhook) {
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
