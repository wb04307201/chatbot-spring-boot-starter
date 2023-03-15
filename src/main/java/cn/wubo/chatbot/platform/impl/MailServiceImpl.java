package cn.wubo.chatbot.platform.impl;

import cn.wubo.chatbot.core.ChatbotInfo;
import cn.wubo.chatbot.core.ChatbotType;
import cn.wubo.chatbot.exception.DingtalkRuntimeException;
import cn.wubo.chatbot.message.*;
import cn.wubo.chatbot.platform.ISendService;
import cn.wubo.chatbot.record.ChatbotHistory;
import cn.wubo.chatbot.record.IChatbotRecord;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
public class MailServiceImpl implements ISendService {

    IChatbotRecord chatbotRecord;

    public MailServiceImpl(IChatbotRecord chatbotRecord) {
        this.chatbotRecord = chatbotRecord;
    }

    @Override
    public Boolean support(ChatbotType chatbotType) {
        return chatbotType.equals(ChatbotType.MAIL);
    }

    @Override
    public String sendText(ChatbotInfo chatbotInfo, TextContent content) {
        return null;
    }

    @Override
    public String sendMarkDown(ChatbotInfo chatbotInfo, MarkdownContent content) {
        return null;
    }
}
