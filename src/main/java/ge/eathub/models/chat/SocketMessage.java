package ge.eathub.models.chat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;


public class SocketMessage {
    private String username;


    private Long roomID;
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy hh:mm:ss")
//    @JsonIgnore
    private LocalDateTime timeSend;
    private MessageType type;
    private String content; // this is temporary


    public SocketMessage(String username, Long roomID, LocalDateTime timeSend, MessageType type, String content) {
        this.username = username;
        this.roomID = roomID;
        this.timeSend = timeSend;
        this.type = type;
        this.content = content;
    }

    public SocketMessage() {

    }

    @Override
    public String toString() {
        return "SocketMessage{" +
                "username='" + username + '\'' +
                ", roomID=" + roomID +
                ", timeSend=" + timeSend +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public SocketMessage setUsername(String username) {
        this.username = username;
        return this;
    }

    public Long getRoomID() {
        return roomID;
    }

    public SocketMessage setRoomID(Long roomID) {
        this.roomID = roomID;
        return this;

    }

    public LocalDateTime getTimeSend() {
        return timeSend;
    }

    public SocketMessage setTimeSend(LocalDateTime timeSend) {
        this.timeSend = timeSend;
        return this;
    }

    public MessageType getType() {
        return type;

    }

    public SocketMessage setType(MessageType type) {
        this.type = type;
        return this;

    }

    public String getContent() {
        return content;
    }

    public SocketMessage setContent(String content) {
        this.content = content;
        return this;

    }
}
