package cn.wubo.message.page;

import cn.wubo.message.exception.ChatbotRuntimeException;
import cn.wubo.message.record.MessageRecord;
import cn.wubo.message.record.IMessageRecordService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ChatbotListServlet extends HttpServlet {

    private IMessageRecordService storageService;

    public ChatbotListServlet(IMessageRecordService storageService) {
        this.storageService = storageService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        if (storageService != null) {
            Map<String, Object> data = new HashMap<>();

            MessageRecord messageRecord = new MessageRecord();
            if (req.getMethod().equalsIgnoreCase("post")) {
                Map<String, String[]> map = req.getParameterMap();
                messageRecord.setType(map.get("type")[0]);
                messageRecord.setAlias(map.get("alias")[0]);
                messageRecord.setRequest(map.get("request")[0]);
                messageRecord.setResponse(map.get("response")[0]);
            }

            data.put("list", storageService.list(messageRecord));
            data.put("contextPath", contextPath);
            data.put("query", messageRecord);

            freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
            cfg.setClassForTemplateLoading(this.getClass(), "/template");
            resp.setCharacterEncoding("UTF-8");
            try {
                Template template = cfg.getTemplate("list.ftl", "UTF-8");
                template.process(data, resp.getWriter());
            } catch (TemplateException e) {
                throw new ChatbotRuntimeException(e.getMessage(), e);
            }
        } else {
            log.debug("contextPath========{}", contextPath);
            String servletPath = req.getServletPath();
            log.debug("servletPath========{}", servletPath);
            super.doGet(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
