package cn.wubo.message.platform.wx;

import cn.wubo.message.core.MessageInfo;
import cn.wubo.message.record.MessageRecord;
import cn.wubo.message.core.MessageType;
import cn.wubo.message.message.*;
import cn.wubo.message.platform.ISendService;
import cn.wubo.message.record.IMessageRecordService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
public class WeixinServiceImpl implements ISendService {

    IMessageRecordService chatbotRecord;
    RestTemplate restTemplate;

    public WeixinServiceImpl(IMessageRecordService chatbotRecord, RestTemplate restTemplate) {
        this.chatbotRecord = chatbotRecord;
        this.restTemplate = restTemplate;
    }

    @Override
    public Boolean support(MessageType messageType) {
        return messageType.equals(MessageType.WEIXIN);
    }

    @Override
    public String sendText(MessageInfo messageInfo, TextContent content) {
        JSONObject jo = new JSONObject();
        jo.put("msgtype", "text");
        JSONObject text = new JSONObject();
        text.put("content", content.getText());
        if (content.isAll())
            text.put("mentioned_list", new JSONArray().add("@all"));
        jo.put("text", text);
        return post(messageInfo, jo.toJSONString());
    }

    @Override
    public String sendMarkDown(MessageInfo messageInfo, MarkdownContent content) {
        JSONObject jo = new JSONObject();
        jo.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", build(content));
        if (content.isAll())
            markdown.put("mentioned_list", new JSONArray().add("@all"));
        jo.put("markdown", markdown);
        return post(messageInfo, jo.toJSONString());
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
                .collect(Collectors.joining("\n\n"));
    }

    private String post(MessageInfo messageInfo, String body) {
        MessageRecord messageRecord = new MessageRecord();
        messageRecord.setType(MessageType.WEIXIN.getType());
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
