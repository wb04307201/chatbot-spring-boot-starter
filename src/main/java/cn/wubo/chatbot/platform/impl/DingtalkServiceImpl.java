package cn.wubo.chatbot.platform.impl;

import cn.wubo.chatbot.exception.DingtalkRuntimeException;
import cn.wubo.chatbot.record.ChatbotHistory;
import cn.wubo.chatbot.core.ChatbotInfo;
import cn.wubo.chatbot.core.ChatbotType;
import cn.wubo.chatbot.message.*;
import cn.wubo.chatbot.platform.ISendService;
import cn.wubo.chatbot.record.IChatbotRecord;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DingtalkServiceImpl implements ISendService {

    IChatbotRecord chatbotRecord;

    public DingtalkServiceImpl(IChatbotRecord chatbotRecord) {
        this.chatbotRecord = chatbotRecord;
    }

    @Override
    public Boolean support(ChatbotType chatbotType) {
        return chatbotType.equals(ChatbotType.DINGTALK);
    }

    public String sendText(ChatbotInfo chatbotInfo, TextContent content) {
        OapiRobotSendRequest request = request(content.isAll(), null, null);
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content.getText());
        request.setText(text);
        return execute(chatbotInfo, request);
    }

    public String sendMarkDown(ChatbotInfo chatbotInfo, MarkdownContent content) {
        OapiRobotSendRequest request = request(content.isAll(), null, null);
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(content.getTitle());
        markdown.setText(build(content));
        request.setMarkdown(markdown);
        return execute(chatbotInfo, request);
    }

    private OapiRobotSendRequest request(Boolean atAll, List<String> mobiles, List<String> userIds) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        if (atAll.equals(Boolean.TRUE))
            at.setIsAtAll(true);
        else {
            at.setAtMobiles(mobiles);
            at.setAtUserIds(userIds);
        }
        request.setAt(at);
        return request;
    }

    private String build(MarkdownContent content) {
        return content.getLines().stream()
                .map(line -> {
                    switch (line.getLineType()) {
                        case TITLE:
                            SubTitleLine subTitleLine = (SubTitleLine) line;
                            return IntStream.range(0, subTitleLine.getLevel()).mapToObj(i -> "#").collect(Collectors.joining()) + " " + subTitleLine.getContent();
                        case LINK:
                            SubLinkLine subLinkLine = (SubLinkLine) line;
                            return String.format("[%s](%s)", subLinkLine.getContent(), subLinkLine.getLink());
                        case QUOTE:
                            SubQuoteLine subQuoteLine = (SubQuoteLine) line;
                            return String.format("> %s", subQuoteLine.getContent());
                        case BOLD:
                            SubBoldLine subBoldLine = (SubBoldLine) line;
                            return String.format("**%s**", subBoldLine.getContent());
                        case TEXT:
                        default:
                            return line.getContent();
                    }
                })
                .collect(Collectors.joining("\n"));
    }

    private DingTalkClient client(ChatbotInfo chatbotInfo) {
        Long timestamp = System.currentTimeMillis();
        String secret = chatbotInfo.getSecret();
        if (!StringUtils.hasLength(secret))
            throw new DingtalkRuntimeException("发送钉钉机器人消息时，secret必须配置！");
        String stringToSign = timestamp + "\n" + secret;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
            return new DefaultDingTalkClient(String.format(chatbotInfo.getChatbotType().getWebhook(), chatbotInfo.getToken(), timestamp, sign));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new DingtalkRuntimeException(e.getMessage(), e);
        }
    }

    private String execute(ChatbotInfo chatbotInfo, OapiRobotSendRequest request) {
        try {
            ChatbotHistory chatbotHistory = new ChatbotHistory();
            chatbotHistory.setType(ChatbotType.DINGTALK.getType());
            chatbotHistory.setRequest(JSON.toJSONString(request));
            chatbotHistory.setAlias(chatbotInfo.getAlias());
            chatbotHistory.setCreateTime(new Date());
            chatbotRecord.save(chatbotHistory);
            DingTalkClient client = client(chatbotInfo);
            OapiRobotSendResponse oapiRobotSendResponse = client.execute(request);
            String response = JSON.toJSONString(oapiRobotSendResponse);
            chatbotHistory.setResponse(response);
            chatbotRecord.save(chatbotHistory);
            return response;
        } catch (ApiException e) {
            throw new DingtalkRuntimeException(e.getMessage(), e);
        }
    }
}
