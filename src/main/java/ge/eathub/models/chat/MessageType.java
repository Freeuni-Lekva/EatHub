package ge.eathub.models.chat;

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
    ACTIVE_USERS,
    @JsonProperty("invitation")
    INVITATION,
    @JsonProperty("time-change")
    TIME_CHANGE,
    @JsonProperty("pay-for-all")
    PAY_FOR_ALL,
    @JsonProperty("split-bill")
    SPLIT_BILL,

//    @JsonProperty("pay-for-all-start")
//    PAY_FOR_ALL_START,
//    @JsonProperty("pay-for-all-success")
//    PAY_FOR_ALL_SUCCESS,
//    @JsonProperty("pay-for-all-fail")
//    PAY_FOR_ALL_FAIL,
//    @JsonProperty("pay-for-start")
//    SPLIT_BILL_START,
//    @JsonProperty("time-change")
//    SPLIT_BILL_SUCCESS,
//    @JsonProperty("time-change")
//    SPLIT_BILL_FAIL,

}
