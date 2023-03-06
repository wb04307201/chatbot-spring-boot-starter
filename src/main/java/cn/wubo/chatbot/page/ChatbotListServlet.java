package cn.wubo.chatbot.page;

import cn.wubo.chatbot.exception.ChatbotRuntimeException;
import cn.wubo.chatbot.record.ChatbotHistory;
import cn.wubo.chatbot.record.IChatbotRecord;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Setter;
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

    private IChatbotRecord storageService;

    public ChatbotListServlet(IChatbotRecord storageService) {
        this.storageService = storageService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        if (storageService != null) {
            Map<String, Object> data = new HashMap<>();

            ChatbotHistory chatbotHistory = new ChatbotHistory();
            if (req.getMethod().equalsIgnoreCase("post")) {
                Map<String, String[]> map = req.getParameterMap();
                chatbotHistory.setType(map.get("type")[0]);
                chatbotHistory.setAlias(map.get("alias")[0]);
                chatbotHistory.setRequest(map.get("request")[0]);
                chatbotHistory.setResponse(map.get("response")[0]);
            }

            data.put("list", storageService.list(chatbotHistory));
            data.put("contextPath", contextPath);
            data.put("query", chatbotHistory);

            freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
            cfg.setClassForTemplateLoading(this.getClass(), "/template");
            resp.setCharacterEncoding("UTF-8");
            try {
                Template template = cfg.getTemplate("list.ftl", "UTF-8");
                template.process(data, resp.getWriter());
            } catch (TemplateException | IOException e) {
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
