package cn.wubo.message.core;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 钉钉
 */
@Data
public class DingtalkProperties {

    private List<CustomRobot> customRobot = new ArrayList<>();

    private List<Message> message = new ArrayList<>();

    /**
     * 自定义机器人
     * https://open.dingtalk.com/document/group/custom-robot-access
     * https://open.dingtalk.com/document/orgapp/custom-bot-to-send-group-chat-messages
     */
    @Data
    public class CustomRobot extends MessageBase {
        private String accessToken;
        private String secret;
        private List<String> atMobiles = new ArrayList<>();
        private List<String> atUserIds = new ArrayList<>();
        private Boolean isAll = Boolean.TRUE;
    }

    /**
     * 工作通知
     * https://open.dingtalk.com/document/orgapp/asynchronous-sending-of-enterprise-session-messages
     */
    @Data
    public class Message {

    }
}
