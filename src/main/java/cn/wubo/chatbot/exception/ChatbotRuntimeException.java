package cn.wubo.chatbot.exception;

public class ChatbotRuntimeException extends RuntimeException {

    public ChatbotRuntimeException(String message) {
        super(message);
    }

    public ChatbotRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
