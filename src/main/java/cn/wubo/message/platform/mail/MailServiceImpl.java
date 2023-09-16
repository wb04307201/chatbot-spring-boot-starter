package cn.wubo.message.platform.mail;

import cn.wubo.message.core.MessageInfo;
import cn.wubo.message.core.MessageType;
import cn.wubo.message.message.*;
import cn.wubo.message.platform.ISendService;
import cn.wubo.message.record.MessageRecord;
import cn.wubo.message.record.IMessageRecordService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.util.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
public class MailServiceImpl implements ISendService {

    IMessageRecordService chatbotRecord;

    public MailServiceImpl(IMessageRecordService chatbotRecord) {
        this.chatbotRecord = chatbotRecord;
    }

    @Override
    public Boolean support(MessageType messageType) {
        return messageType.equals(MessageType.MAIL);
    }

    @Override
    public String sendText(MessageInfo messageInfo, TextContent content) {
        return send(messageInfo, null, content.getText());
    }

    @Override
    public String sendMarkDown(MessageInfo messageInfo, MarkdownContent content) {
        return send(messageInfo, content.getTitle(), build(content));
    }

    private String build(MarkdownContent content) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(content.getLines().stream()
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
                .collect(Collectors.joining("  ")));
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    private String send(MessageInfo messageInfo, String title, String body) {
        MessageRecord messageRecord = new MessageRecord();
        messageRecord.setType(messageInfo.getMessageType().getType());
        messageRecord.setRequest(JSON.toJSONString(body));
        messageRecord.setAlias(messageInfo.getAlias());
        messageRecord.setCreateTime(new Date());
        chatbotRecord.save(messageRecord);

        //1.创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    //参数配置
        props.setProperty("mail.transport.protocol", messageInfo.getMessageType().getWebhook());   //使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", messageInfo.getHost());   //发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            //需要请求认证

        //2.根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props);
        //设置为debug模式,可以查看详细的发送log
        //session.setDebug(true);

        String response = "success";
        try {
            //3.构造邮件
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, messageInfo.getTo());
            if(StringUtils.hasLength(title)){
                message.setSubject(title);
                message.setContent(body,"text/html;charset=UTF-8");
            }else{
                message.setText(body);
            }
            message.setFrom(new InternetAddress(messageInfo.getFrom()));

            //4.根据 Session获取邮件传输对象
            Transport transport = session.getTransport();
            transport.connect(messageInfo.getUsername(), messageInfo.getPassword());
            transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            response = e.getMessage();
        } finally {
            messageRecord.setResponse("success");
            chatbotRecord.save(messageRecord);
        }
        return response;
    }
}
