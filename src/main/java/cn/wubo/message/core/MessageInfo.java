package cn.wubo.message.core;

import lombok.Data;

@Data
public class MessageInfo {
    private String alias;
    private MessageType messageType;
    private String token;
    private String secret;
    private String from;
    private String to;
    private String host;
    private String username;
    private String password;
}
