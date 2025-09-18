package easyfix;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FixMessage {
    private String sessionId;
    private String msgDirection;  // "SENT" or "RECEIVED"
    private String messageContent;
    private String timestamp;

    // Default constructor required for JAX-B
    public FixMessage() {
    }

    public FixMessage(String sessionId, String msgDirection, String messageContent, String timestamp) {
        this.sessionId = sessionId;
        this.msgDirection = msgDirection;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMsgDirection() {
        return msgDirection;
    }

    public void setMsgDirection(String msgDirection) {
        this.msgDirection = msgDirection;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "FixMessage{" +
                "sessionId='" + sessionId + '\'' +
                ", msgDirection='" + msgDirection + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}