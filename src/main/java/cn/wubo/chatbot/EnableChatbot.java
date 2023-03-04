package cn.wubo.chatbot;

import cn.wubo.chatbot.config.ChatbotConfiguration;
import cn.wubo.chatbot.config.ChatbotHttpClientConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用钉钉机器人
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({ChatbotHttpClientConfig.class, ChatbotConfiguration.class})
public @interface EnableChatbot {
}
