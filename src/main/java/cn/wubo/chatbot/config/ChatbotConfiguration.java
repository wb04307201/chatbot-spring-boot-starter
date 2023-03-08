package cn.wubo.chatbot.config;

import cn.wubo.chatbot.core.IChatbotService;
import cn.wubo.chatbot.exception.ChatbotRuntimeException;
import cn.wubo.chatbot.platform.ISendService;
import cn.wubo.chatbot.platform.impl.DingtalkServiceImpl;
import cn.wubo.chatbot.platform.impl.FeishuServiceImpl;
import cn.wubo.chatbot.platform.impl.WeixinServiceImpl;
import cn.wubo.chatbot.core.impl.ChatbotServiceImpl;
import cn.wubo.chatbot.page.ChatbotListServlet;
import cn.wubo.chatbot.record.IChatbotRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServlet;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
@EnableConfigurationProperties({ChatbotConfigurationProperties.class})
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

    /**
     * 连接超时时间
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private final Duration connectTimeout = Duration.ofSeconds(30);

    /**
     * 读超时时间
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private final Duration readTimeout = Duration.ofSeconds(30);

    @Bean(name = "chatbotRestTemplate")
    public RestTemplate chatbotRestTemplate(@Qualifier("chatbotClientHttpRequestFactory") ClientHttpRequestFactory chatbotClientHttpRequestFactory) {
        return new RestTemplate(chatbotClientHttpRequestFactory);
    }

    @Bean(name = "chatbotClientHttpRequestFactory")
    public ClientHttpRequestFactory chatbotClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout((int) readTimeout.toMillis());
        factory.setConnectTimeout((int) connectTimeout.toMillis());
        return factory;
    }

    @Bean
    public List<ISendService> sendServices(IChatbotRecord storageService, @Qualifier(value = "chatbotRestTemplate") RestTemplate restTemplate) {
        CopyOnWriteArrayList<ISendService> sendServices = new CopyOnWriteArrayList<>();
        sendServices.add(new DingtalkServiceImpl(storageService));
        sendServices.add(new WeixinServiceImpl(storageService, restTemplate));
        sendServices.add(new FeishuServiceImpl(storageService, restTemplate));
        return sendServices;
    }

    @Bean
    public IChatbotService chatbotService(ChatbotConfigurationProperties properties, List<ISendService> sendServices) {
        return new ChatbotServiceImpl(properties, sendServices);
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> chatBotListServlet(IChatbotRecord storageService) {
        ServletRegistrationBean<HttpServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new ChatbotListServlet(storageService));
        registration.addUrlMappings("/chat/robot/list");
        return registration;
    }
}
