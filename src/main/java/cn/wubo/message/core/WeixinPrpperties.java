package cn.wubo.message.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class WeixinPrpperties {

    private List<DingtalkProperties.CustomRobot> customRobot = new ArrayList<>();

    /**
     * 自定义机器人
     * https://developer.work.weixin.qq.com/document/path/99110
     */
    @Data
    public class CustomRobot {
        private String key;
    }
}
