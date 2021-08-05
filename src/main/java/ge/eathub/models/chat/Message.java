package ge.eathub.models.chat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    public static final String MESSAGE_ID = "message_id";
    public static final String USER_ID = "user_id";
    public static final String ROOM_ID = "room_id";
    public static final String SEND_TIME = "send_time";
    public static final String TYPE = "type";
    public static final String CONTENT = "content";

    private Long messageID;
    private Long userID;
    private Long roomID;
    private LocalDateTime sendTime;
    private MessageType type;
    private String content; // this is temporary

    public Message() {
    }

    public Message(Long messageID, Long userID, Long roomID, MessageType type,
                   String content) {
        this.messageID = messageID;
        this.userID = userID;
        this.roomID = roomID;
        this.type = type;
        this.content = content;
        this.sendTime = LocalDateTime.now();
    }


    public Long getUserID() {
        return userID;
    }

    public Message setUserID(Long userID) {
        this.userID = userID;
        return this;
    }

    public Long getRoomID() {
        return roomID;
    }

    public Message setRoomID(Long roomID) {
        this.roomID = roomID;
        return this;
    }

    public LocalDateTime getTimeSend() {
        return sendTime;
    }

    public Message setTimeSend(LocalDateTime timeSend) {
        this.sendTime = timeSend;
        return this;
    }

    public MessageType getType() {
        return type;
    }

    public Message setType(MessageType type) {
        this.type = type;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userID=" + userID +
                ", roomID=" + roomID +
                ", timeSend=" + sendTime +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }

    public Long getMessageID() {
        return messageID;
    }
}
