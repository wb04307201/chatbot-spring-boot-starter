package cn.wubo.chatbot.exception;

public class FeishuRuntimeException extends RuntimeException {

    public FeishuRuntimeException(String message) {
        super(message);
    }

    public FeishuRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
