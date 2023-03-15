package cn.wubo.chatbot.platform.impl;

import cn.wubo.chatbot.record.ChatbotHistory;
import cn.wubo.chatbot.core.ChatbotInfo;
import cn.wubo.chatbot.core.ChatbotType;
import cn.wubo.chatbot.message.*;
import cn.wubo.chatbot.platform.ISendService;
import cn.wubo.chatbot.record.IChatbotRecord;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
public class WeixinServiceImpl implements ISendService {

    IChatbotRecord chatbotRecord;
    RestTemplate restTemplate;

    public WeixinServiceImpl(IChatbotRecord chatbotRecord, RestTemplate restTemplate) {
        this.chatbotRecord = chatbotRecord;
        this.restTemplate = restTemplate;
    }

    @Override
    public Boolean support(ChatbotType chatbotType) {
        return chatbotType.equals(ChatbotType.WEIXIN);
    }

    @Override
    public String sendText(ChatbotInfo chatbotInfo, TextContent content) {
        JSONObject jo = new JSONObject();
        jo.put("msgtype", "text");
        JSONObject text = new JSONObject();
        text.put("content", content.getText());
        if (content.isAll())
            text.put("mentioned_list", new JSONArray().add("@all"));
        jo.put("text", text);
        return post(chatbotInfo, jo.toJSONString());
    }

    @Override
    public String sendMarkDown(ChatbotInfo chatbotInfo, MarkdownContent content) {
        JSONObject jo = new JSONObject();
        jo.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", build(content));
        if (content.isAll())
            markdown.put("mentioned_list", new JSONArray().add("@all"));
        jo.put("markdown", markdown);
        return post(chatbotInfo, jo.toJSONString());
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

    private String post(ChatbotInfo chatbotInfo, String body) {
        ChatbotHistory chatbotHistory = new ChatbotHistory();
        chatbotHistory.setType(ChatbotType.WEIXIN.getType());
        chatbotHistory.setRequest(JSON.toJSONString(body));
        chatbotHistory.setAlias(chatbotInfo.getAlias());
        chatbotHistory.setCreateTime(new Date());
        chatbotRecord.save(chatbotHistory);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);
        String response = restTemplate.postForObject(String.format(chatbotInfo.getChatbotType().getWebhook(), chatbotInfo.getToken()), request, String.class);
        chatbotHistory.setResponse(response);
        chatbotRecord.save(chatbotHistory);
        return response;
    }
}
