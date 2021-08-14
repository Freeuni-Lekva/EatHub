package ge.eathub.dao;

import ge.eathub.models.chat.Message;

import java.util.List;

public interface ChatDao {

    boolean saveMessage(Message msg);

    // return all messages in room
    List<Message> getMessagesByRoomID(Long roomID);

}
