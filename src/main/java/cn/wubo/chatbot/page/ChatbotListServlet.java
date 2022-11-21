package cn.wubo.chatbot.page;

import cn.wubo.chatbot.core.storage.IStorageService;
import cn.wubo.chatbot.core.storage.impl.H2StorageServiceImpl;
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

    private IStorageService histroyService = new H2StorageServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        if (histroyService != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("list", histroyService.list());
            data.put("contextPath", contextPath);

            freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
            cfg.setClassForTemplateLoading(this.getClass(), "/template");
            resp.setCharacterEncoding("UTF-8");
            try {
                Template template = cfg.getTemplate("list.ftl", "UTF-8");
                template.process(data, resp.getWriter());
            } catch (TemplateException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            log.debug("contextPath========{}", contextPath);
            String servletPath = req.getServletPath();
            log.debug("servletPath========{}", servletPath);
            super.doGet(req, resp);
        }
    }
}
