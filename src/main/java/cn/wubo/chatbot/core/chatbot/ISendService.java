package cn.wubo.chatbot.core.chatbot;

import cn.wubo.chatbot.entity.*;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ISendService {

    Boolean support(ChatbotInfo chatbotInfo);
    String sendText(ChatbotInfo chatbotInfo, TextContent content);
    String sendMarkDown(ChatbotInfo chatbotInfo, MarkdownContent content);
}
