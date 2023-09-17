package cn.wubo.message.config;

import cn.wubo.message.core.MessageService;
import cn.wubo.message.exception.ChatbotRuntimeException;
import cn.wubo.message.page.ChatbotListServlet;
import cn.wubo.message.platform.ISendService;
import cn.wubo.message.platform.dingtalk.DingtalkCustomRobotServiceImpl;
import cn.wubo.message.platform.feishu.FeishuCustomRobotServiceImpl;
import cn.wubo.message.platform.mail.MailSmtpServiceImpl;
import cn.wubo.message.platform.wx.WeixinCustomRobotServiceImpl;
import cn.wubo.message.record.IMessageRecordService;
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
@EnableConfigurationProperties({MessageConfigurationProperties.class})
public class MessageConfiguration {

    private final MessageConfigurationProperties properties;

    public MessageConfiguration(MessageConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public List<ISendService> sendServices(List<IMessageRecordService> chatbotRecordList, @Qualifier(value = "chatbotRestTemplate") RestTemplate restTemplate) {
        CopyOnWriteArrayList<ISendService> sendServices = new CopyOnWriteArrayList<>();
        IMessageRecordService chatbotRecord = chatbotRecordList.stream().filter(obj -> obj.getClass().getName().equals(properties.getChatbotRecord())).findAny().orElseThrow(() -> new ChatbotRuntimeException(String.format("未找到%s对应的bean，无法加载IChatbotRecord！", properties.getChatbotRecord())));
        sendServices.add(new DingtalkCustomRobotServiceImpl(chatbotRecord));
        sendServices.add(new WeixinCustomRobotServiceImpl(chatbotRecord, restTemplate));
        sendServices.add(new FeishuCustomRobotServiceImpl(chatbotRecord, restTemplate));
        sendServices.add(new MailSmtpServiceImpl(chatbotRecord));
        return sendServices;
    }

    @Bean
    public MessageService chatbotService(MessageConfigurationProperties properties, List<ISendService> sendServices) {
        return new MessageService(properties.getChatbotInfo(), sendServices);
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> chatBotListServlet(List<IMessageRecordService> chatbotRecordList) {
        ServletRegistrationBean<HttpServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new ChatbotListServlet(chatbotRecordList.stream().filter(obj -> obj.getClass().getName().equals(properties.getChatbotRecord())).findAny().orElseThrow(() -> new ChatbotRuntimeException(String.format("未找到%s对应的bean，无法加载IChatbotRecord！", properties.getChatbotRecord())))));
        registration.addUrlMappings("/chat/robot/list");
        return registration;
    }
}
