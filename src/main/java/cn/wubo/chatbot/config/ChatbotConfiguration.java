package cn.wubo.chatbot.config;

import cn.wubo.chatbot.core.IChatbotService;
import cn.wubo.chatbot.exception.ChatbotRuntimeException;
import cn.wubo.chatbot.platform.impl.DingtalkServiceImpl;
import cn.wubo.chatbot.platform.impl.FeishuServiceImpl;
import cn.wubo.chatbot.platform.impl.WeixinServiceImpl;
import cn.wubo.chatbot.core.impl.ChatbotServiceImpl;
import cn.wubo.chatbot.page.ChatbotListServlet;
import cn.wubo.chatbot.record.IChatbotRecord;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServlet;

@Configuration
@EnableConfigurationProperties({ChatbotConfigurationProperties.class})
@AutoConfigureAfter(value = {ChatbotHttpClientConfig.class})
public class ChatbotConfiguration {

    private final ChatbotConfigurationProperties properties;

    public ChatbotConfiguration(ChatbotConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public IChatbotRecord storageService() {
        try {
            Class<?> clazz = Class.forName(properties.getChatbotRecord());
            IChatbotRecord storageService = (IChatbotRecord) clazz.newInstance();
            storageService.init();
            return storageService;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new ChatbotRuntimeException(e.getMessage(), e);
        }
    }

    @Bean
    public ServletRegistrationBean listServlet() {
        ServletRegistrationBean<HttpServlet> registration = new ServletRegistrationBean();
        registration.setServlet(new ChatbotListServlet());
        registration.addUrlMappings(new String[]{"/chat/robot/list"});
        return registration;
    }

    @Bean
    public DingtalkServiceImpl dingtalkService() {
        return new DingtalkServiceImpl();
    }

    @Bean
    public WeixinServiceImpl weixinService() {
        return new WeixinServiceImpl();
    }

    @Bean
    public FeishuServiceImpl feishuService() {
        return new FeishuServiceImpl();
    }

    @Bean
    public IChatbotService chatbotService(ChatbotConfigurationProperties properties) {
        return new ChatbotServiceImpl(properties);
    }
}
