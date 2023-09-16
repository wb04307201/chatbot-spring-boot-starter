package cn.wubo.message.record;

import lombok.Data;

import java.util.Date;

@Data
public class MessageRecord {
    private String id;
    private String type;
    private String request;
    private String response;
    private String alias;
    private Date createTime;
}
