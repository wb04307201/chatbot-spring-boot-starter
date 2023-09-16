package cn.wubo.message.platform.feishu;

import cn.wubo.message.core.MessageInfo;
import cn.wubo.message.core.MessageType;
import cn.wubo.message.exception.FeishuRuntimeException;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.SubBoldLine;
import cn.wubo.message.message.SubLinkLine;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.ISendService;
import cn.wubo.message.record.MessageRecord;
import cn.wubo.message.record.IMessageRecordService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


@Slf4j
public class FeishuServiceImpl implements ISendService {

    IMessageRecordService chatbotRecord;
    RestTemplate restTemplate;

    public FeishuServiceImpl(IMessageRecordService chatbotRecord, RestTemplate restTemplate) {
        this.chatbotRecord = chatbotRecord;
        this.restTemplate = restTemplate;
    }

    @Override
    public Boolean support(MessageType messageType) {
        return messageType.equals(MessageType.FEISHU);
    }

    @Override
    public String sendText(MessageInfo messageInfo, TextContent content) {
        JSONObject jo = request(messageInfo);
        jo.put("msgtype", "text");
        JSONObject text = new JSONObject();
        if (content.isAll()) {
            text.put("text", "<at user_id = \"all\">所有人</at>" + content.getText());
        } else {
            text.put("text", content.getText());
        }
        jo.put("content", text);
        return post(messageInfo, jo.toJSONString());
    }

    @Override
    public String sendMarkDown(MessageInfo messageInfo, MarkdownContent content) {
        JSONObject jo = request(messageInfo);
        jo.put("msgtype", "post");
        JSONObject post = new JSONObject();
        post.put("title", content.getTitle());
        post.put("content", build(content));
        jo.put("content", new JSONObject().put("post", new JSONObject().put("zh_cn", post)));
        return post(messageInfo, jo.toJSONString());
    }

    private JSONArray build(MarkdownContent content) {
        JSONArray ja = new JSONArray();
        content.getLines().forEach(line -> {
            JSONObject jo = new JSONObject();
            switch (line.getLineType()) {
                case LINK:
                    SubLinkLine subLinkLine = (SubLinkLine) line;
                    jo.put("tag", "a");
                    jo.put("text", subLinkLine.getContent());
                    jo.put("href", subLinkLine.getLink());
                    break;
                case BOLD:
                    SubBoldLine SubBoldLine = (SubBoldLine) line;
                    jo.put("tag", "text");
                    jo.put("text", SubBoldLine.getContent());
                    jo.put("style", new JSONArray().add("bold"));
                    break;
                case TEXT:
                case TITLE:
                case QUOTE:
                default:
                    jo.put("tag", "text");
                    jo.put("text", line.getContent());
            }
            ja.add(jo);
        });
        if (content.isAll()) {
            JSONObject jo = new JSONObject();
            jo.put("tag", "at");
            jo.put("user_id", "all");
            jo.put("user_name", "所有人");
            ja.add(jo);
        }
        return new JSONArray(ja);
    }

    private JSONObject request(MessageInfo messageInfo) {
        JSONObject jo = new JSONObject();
        Long timestamp = System.currentTimeMillis();
        String secret = messageInfo.getSecret();
        if (!StringUtils.hasLength(secret))
            throw new FeishuRuntimeException("发送飞书机器人消息时，secret必须配置！");
        String stringToSign = timestamp + "\n" + secret;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(new byte[]{});
            String sign = new String(Base64.encodeBase64(signData));
            jo.put("timestamp", timestamp);
            jo.put("sign", sign);
            return jo;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new FeishuRuntimeException(e.getMessage(), e);
        }
    }

    private String post(MessageInfo messageInfo, String body) {
        MessageRecord messageRecord = new MessageRecord();
        messageRecord.setType(MessageType.FEISHU.getType());
        messageRecord.setRequest(JSON.toJSONString(body));
        messageRecord.setAlias(messageInfo.getAlias());
        messageRecord.setCreateTime(new Date());
        chatbotRecord.save(messageRecord);
        String response = null;
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
            HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);
            response = restTemplate.postForObject(String.format(messageInfo.getMessageType().getWebhook(), messageInfo.getToken()), request, String.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = e.getMessage();
        } finally {
            messageRecord.setResponse(response);
            chatbotRecord.save(messageRecord);
        }
        return response;
    }
}
