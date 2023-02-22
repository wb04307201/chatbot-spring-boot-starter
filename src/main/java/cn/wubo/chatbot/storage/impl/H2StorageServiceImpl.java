package cn.wubo.chatbot.storage.impl;

import cn.wubo.chatbot.entity.ChatbotHistory;
import cn.wubo.chatbot.storage.IStorageService;
import cn.wubo.sql.util.ConnectionParam;
import cn.wubo.sql.util.ConnectionPool;
import cn.wubo.sql.util.ExecuteSqlUtils;
import cn.wubo.sql.util.ModelSqlUtils;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class H2StorageServiceImpl implements IStorageService {

    private static final String HISTORY = "chat_robot_history";

    private static ConnectionPool connectionPool = new ConnectionPool(new ConnectionParam());

    @Override
    public ChatbotHistory save(ChatbotHistory chatbotHistoryDto) {
        try {
            Connection conn = connectionPool.getConnection();
            if (!ExecuteSqlUtils.isTableExists(conn, HISTORY, connectionPool.getDbType())) {
                ExecuteSqlUtils.executeUpdate(conn, ModelSqlUtils.createSql(HISTORY, chatbotHistoryDto), new HashMap<>());
            }
            if (!StringUtils.hasLength(chatbotHistoryDto.getId())) {
                chatbotHistoryDto.setId(UUID.randomUUID().toString());
                ExecuteSqlUtils.executeUpdate(conn, ModelSqlUtils.insertSql(HISTORY, chatbotHistoryDto), new HashMap<>());
            } else {
                ExecuteSqlUtils.executeUpdate(conn, ModelSqlUtils.updateByIdSql(HISTORY, chatbotHistoryDto), new HashMap<>());
            }
            connectionPool.returnConnection(conn);
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return chatbotHistoryDto;
    }

    @Override
    public List<ChatbotHistory> list() {
        try {
            Connection conn = connectionPool.getConnection();
            if (!ExecuteSqlUtils.isTableExists(conn, HISTORY, connectionPool.getDbType())) {
                ExecuteSqlUtils.executeUpdate(conn, ModelSqlUtils.createSql(HISTORY, new ChatbotHistory()), new HashMap<>());
            }
            List<ChatbotHistory> res = ExecuteSqlUtils.executeQuery(conn, ModelSqlUtils.selectSql(HISTORY, new ChatbotHistory()), new HashMap<>(), ChatbotHistory.class);
            connectionPool.returnConnection(conn);
            return res;
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
