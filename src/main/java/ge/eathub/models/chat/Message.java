package ge.eathub.models.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    public static final String TABLE = "messages";
    public static final String MESSAGE_ID = "message_id";
    public static final String USER_ID = "user_id";
    public static final String ROOM_ID = "room_id";
    public static final String SEND_TIME = "send_time";
    public static final String TYPE = "type";
    public static final String CONTENT = "content";

    private Long messageID;
    private String username;
    @JsonIgnore
    private Long userID;
    private Long roomID;
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime sendTime;
    private MessageType type;
    private String content; // this is temporary

    public Message() {
    }

    public Message(String username, Long userID, Long roomID, MessageType type,
                   String content) {
        this.username = username;
        this.userID = userID;
        this.roomID = roomID;
        this.type = type;
        this.content = content;
        this.sendTime = LocalDateTime.now();
    }

    public Message(Long messageID, String username, Long userID, Long roomID, LocalDateTime sendTime, MessageType type,
                   String content) {
        this.messageID = messageID;
        this.username = username;
        this.userID = userID;
        this.roomID = roomID;
        this.type = type;
        this.content = content;
        this.sendTime = sendTime;
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

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public Message setSendTime(LocalDateTime timeSend) {
        this.sendTime = timeSend;
        return this;
    }

    public Message setCurrentDateTime() {
        this.sendTime = LocalDateTime.now();
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

    public Long getMessageID() {
        return messageID;
    }

    public Message setMessageID(Long messageID) {
        this.messageID = messageID;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Message setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageID=" + messageID +
                ", username='" + username + '\'' +
                ", userID=" + userID +
                ", roomID=" + roomID +
                ", sendTime=" + sendTime +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
