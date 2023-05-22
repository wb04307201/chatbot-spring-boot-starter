package cn.wubo.chatbot.platform.feishu;

import cn.wubo.chatbot.core.ChatbotInfo;
import cn.wubo.chatbot.core.ChatbotType;
import cn.wubo.chatbot.exception.FeishuRuntimeException;
import cn.wubo.chatbot.message.MarkdownContent;
import cn.wubo.chatbot.message.SubBoldLine;
import cn.wubo.chatbot.message.SubLinkLine;
import cn.wubo.chatbot.message.TextContent;
import cn.wubo.chatbot.platform.ISendService;
import cn.wubo.chatbot.record.ChatbotHistory;
import cn.wubo.chatbot.record.IChatbotRecord;
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

    IChatbotRecord chatbotRecord;
    RestTemplate restTemplate;

    public FeishuServiceImpl(IChatbotRecord chatbotRecord, RestTemplate restTemplate) {
        this.chatbotRecord = chatbotRecord;
        this.restTemplate = restTemplate;
    }

    @Override
    public Boolean support(ChatbotType chatbotType) {
        return chatbotType.equals(ChatbotType.FEISHU);
    }

    @Override
    public String sendText(ChatbotInfo chatbotInfo, TextContent content) {
        JSONObject jo = request(chatbotInfo);
        jo.put("msgtype", "text");
        JSONObject text = new JSONObject();
        if (content.isAll()) {
            text.put("text", "<at user_id = \"all\">所有人</at>" + content.getText());
        } else {
            text.put("text", content.getText());
        }
        jo.put("content", text);
        return post(chatbotInfo, jo.toJSONString());
    }

    @Override
    public String sendMarkDown(ChatbotInfo chatbotInfo, MarkdownContent content) {
        JSONObject jo = request(chatbotInfo);
        jo.put("msgtype", "post");
        JSONObject post = new JSONObject();
        post.put("title", content.getTitle());
        post.put("content", build(content));
        jo.put("content", new JSONObject().put("post", new JSONObject().put("zh_cn", post)));
        return post(chatbotInfo, jo.toJSONString());
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

    private JSONObject request(ChatbotInfo chatbotInfo) {
        JSONObject jo = new JSONObject();
        Long timestamp = System.currentTimeMillis();
        String secret = chatbotInfo.getSecret();
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

    private String post(ChatbotInfo chatbotInfo, String body) {
        ChatbotHistory chatbotHistory = new ChatbotHistory();
        chatbotHistory.setType(ChatbotType.FEISHU.getType());
        chatbotHistory.setRequest(JSON.toJSONString(body));
        chatbotHistory.setAlias(chatbotInfo.getAlias());
        chatbotHistory.setCreateTime(new Date());
        chatbotRecord.save(chatbotHistory);
        String response = null;
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
            HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);
            response = restTemplate.postForObject(String.format(chatbotInfo.getChatbotType().getWebhook(), chatbotInfo.getToken()), request, String.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = e.getMessage();
        } finally {
            chatbotHistory.setResponse(response);
            chatbotRecord.save(chatbotHistory);
        }
        return response;
    }
}
