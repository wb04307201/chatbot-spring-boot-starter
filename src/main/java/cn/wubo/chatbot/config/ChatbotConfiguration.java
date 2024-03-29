package cn.wubo.chatbot.config;

import cn.wubo.chatbot.core.ChatbotService;
import cn.wubo.chatbot.exception.ChatbotRuntimeException;
import cn.wubo.chatbot.page.ChatbotListServlet;
import cn.wubo.chatbot.platform.ISendService;
import cn.wubo.chatbot.platform.dingtalk.DingtalkServiceImpl;
import cn.wubo.chatbot.platform.feishu.FeishuServiceImpl;
import cn.wubo.chatbot.platform.mail.MailServiceImpl;
import cn.wubo.chatbot.platform.wx.WeixinServiceImpl;
import cn.wubo.chatbot.record.IChatbotRecord;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
@EnableConfigurationProperties({ChatbotConfigurationProperties.class})
public class ChatbotConfiguration {

    private final ChatbotConfigurationProperties properties;

    public ChatbotConfiguration(ChatbotConfigurationProperties properties) {
        this.properties = properties;
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
    public List<ISendService> sendServices(List<IChatbotRecord> chatbotRecordList, @Qualifier(value = "chatbotRestTemplate") RestTemplate restTemplate) {
        CopyOnWriteArrayList<ISendService> sendServices = new CopyOnWriteArrayList<>();
        IChatbotRecord chatbotRecord = chatbotRecordList.stream().filter(obj -> obj.getClass().getName().equals(properties.getChatbotRecord())).findAny().orElseThrow(() -> new ChatbotRuntimeException(String.format("未找到%s对应的bean，无法加载IChatbotRecord！", properties.getChatbotRecord())));
        sendServices.add(new DingtalkServiceImpl(chatbotRecord));
        sendServices.add(new WeixinServiceImpl(chatbotRecord, restTemplate));
        sendServices.add(new FeishuServiceImpl(chatbotRecord, restTemplate));
        sendServices.add(new MailServiceImpl(chatbotRecord));
        return sendServices;
    }

    @Bean
    public ChatbotService chatbotService(ChatbotConfigurationProperties properties, List<ISendService> sendServices) {
        return new ChatbotService(properties.getChatbotInfo(), sendServices);
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> chatBotListServlet(List<IChatbotRecord> chatbotRecordList) {
        ServletRegistrationBean<HttpServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new ChatbotListServlet(chatbotRecordList.stream().filter(obj -> obj.getClass().getName().equals(properties.getChatbotRecord())).findAny().orElseThrow(() -> new ChatbotRuntimeException(String.format("未找到%s对应的bean，无法加载IChatbotRecord！", properties.getChatbotRecord())))));
        registration.addUrlMappings("/chat/robot/list");
        return registration;
    }
}
