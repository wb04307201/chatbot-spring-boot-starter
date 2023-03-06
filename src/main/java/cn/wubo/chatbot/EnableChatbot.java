package cn.wubo.chatbot;

import cn.wubo.chatbot.config.ChatbotConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用钉钉机器人
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({ChatbotConfiguration.class})
public @interface EnableChatbot {
}
