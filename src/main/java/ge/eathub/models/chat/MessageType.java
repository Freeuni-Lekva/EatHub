package ge.eathub.models.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MessageType {
    @JsonProperty("text")
    TEXT,
    @JsonProperty("image")
    IMAGE,
    @JsonProperty("welcome")
    WELCOME,
    @JsonProperty("goodbye")
    GOODBYE,
    @JsonProperty("active-users")
    ACTIVE_USERS
}