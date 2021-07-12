package ge.eathub.models.chat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Message  implements Serializable {
    private Long userID;
    private Long roomID;
    private LocalDateTime timeSend;
    private MessageType type;
    private String content; // this is temporary

    public Message(Long userID, Long roomID, MessageType type,
                   String content) {
        this.userID = userID;
        this.roomID = roomID;
        this.type = type;
        this.content= content;
        this.timeSend = LocalDateTime.now();
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
        return timeSend;
    }

    public Message setTimeSend(LocalDateTime timeSend) {
        this.timeSend = timeSend;
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

    public Message setContent(String messageContent) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userID=" + userID +
                ", roomID=" + roomID +
                ", timeSend=" + timeSend +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
