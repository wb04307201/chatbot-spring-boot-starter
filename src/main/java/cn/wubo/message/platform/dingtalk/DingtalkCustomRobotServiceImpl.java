package cn.wubo.message.platform.dingtalk;

import cn.wubo.message.core.DingtalkProperties;
import cn.wubo.message.exception.DingtalkRuntimeException;
import cn.wubo.message.message.*;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.record.IMessageRecordService;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
public class DingtalkCustomRobotServiceImpl extends AbstractSendService<DingtalkProperties.CustomRobot> {

    private final static String WEBHOOK = "https://oapi.dingtalk.com/robot/send?access_token=%s&timestamp=%s&sign=%s";

    protected DingtalkCustomRobotServiceImpl(IMessageRecordService messageRecordService) {
        super(messageRecordService);
    }

    @Override
    public String doSendMarkdown(DingtalkProperties.CustomRobot aliasProperties, MarkdownContent content) {
        OapiRobotSendRequest request = request(aliasProperties);
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(content.getTitle());
        markdown.setText(build(content));
        request.setMarkdown(markdown);
        return execute(aliasProperties, request);
    }

    @Override
    public String doSendText(DingtalkProperties.CustomRobot aliasProperties, TextContent content) {
        OapiRobotSendRequest request = request(aliasProperties);
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content.getText());
        request.setText(text);
        return execute(aliasProperties, request);
    }

    private String execute(DingtalkProperties.CustomRobot aliasProperties, OapiRobotSendRequest request) {
        String response;
        try {
            DingTalkClient client = getClient(aliasProperties);
            OapiRobotSendResponse oapiRobotSendResponse = client.execute(request);
            response = JSON.toJSONString(oapiRobotSendResponse);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = e.getMessage();
        }
        return response;
    }

    private OapiRobotSendRequest request(DingtalkProperties.CustomRobot aliasProperties) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        if (aliasProperties.getIsAll().equals(Boolean.TRUE)) at.setIsAtAll(true);
        else {
            at.setAtMobiles(aliasProperties.getAtMobiles());
            at.setAtUserIds(aliasProperties.getAtUserIds());
        }
        request.setAt(at);
        return request;
    }

    private String build(MarkdownContent content) {
        return content.getLines().stream().map(line -> {
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
        }).collect(Collectors.joining("\n\n"));
    }

    private DingTalkClient getClient(DingtalkProperties.CustomRobot aliasProperties,) {
        Long timestamp = System.currentTimeMillis();
        String secret = aliasProperties.getSecret();
        if (!StringUtils.hasLength(secret)) throw new DingtalkRuntimeException("发送钉钉机器人消息时，secret必须配置！");
        String stringToSign = timestamp + "\n" + secret;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
            return new DefaultDingTalkClient(String.format(WEBHOOK, aliasProperties.getAccessToken(), timestamp, sign));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new DingtalkRuntimeException(e.getMessage(), e);
        }
    }
}
