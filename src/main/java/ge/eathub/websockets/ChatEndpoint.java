package ge.eathub.websockets;


import com.fasterxml.jackson.core.JsonProcessingException;
import ge.eathub.dao.ChatDao;
import ge.eathub.models.UserPrincipal;
import ge.eathub.models.chat.Message;
import ge.eathub.models.chat.MessageType;
import ge.eathub.utils.ObjectMapperFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import static ge.eathub.listener.NameConstants.CHAT_DAO;

@ServerEndpoint(
        value = "/chat",
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class,
        configurator = ChatConfig.class)
public class ChatEndpoint {

    private static final Map<Long, Set<Session>> activeUsers = new ConcurrentHashMap<>();
    private Long roomID;
    private ChatDao chatDao;
    private Long userID;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws SessionException {
        try {
            chatDao = (ChatDao) session.getUserProperties().get(CHAT_DAO);
//            session.
            roomID = Long.valueOf(session.getRequestParameterMap().get("room-id").get(0));
            Set<Session> ses = activeUsers.getOrDefault(roomID, null);
            if (ses == null) {
                ses = new CopyOnWriteArraySet<>();
                ses.add(session);
                activeUsers.put(roomID, ses);
            } else {
                ses.add(session);
            }

            UserPrincipal user = (UserPrincipal) session.getUserPrincipal();
            String username = user.getName();
            userID = user.getUserID();

            List<Message> messagesByRoomID = chatDao.getMessagesByRoomID(roomID);
            sendOldMessages(session, messagesByRoomID);
            welcomeUser(username);
            broadcastAvailableUsers();
        } catch (Exception e) {
            System.out.println("caught error");
            e.printStackTrace();
            throw new SessionException(e.getMessage(), e, session);
        }

    }

    private void sendOldMessages(Session session, List<Message> messages) {
        RemoteEndpoint.Async asyncRemote = session.getAsyncRemote();
        messages.forEach(asyncRemote::sendObject);
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        System.out.println("on message :" + message);
        message = message
                .setCurrentDateTime()
                .setRoomID(roomID)
                .setUsername(session.getUserPrincipal().getName())
                .setUserID(userID);
        broadcast(message);
        chatDao.saveMessage(message);
    }

    @OnClose
    public void onClose(Session session) {
        activeUsers.get(roomID).remove(session);
        String username = session.getUserPrincipal().getName();
        broadcastUserDisconnected(username);
        broadcastAvailableUsers();
    }

    @OnError
    public void OnError(Session session, Throwable t) {
        System.out.println("on error");
        t.printStackTrace();
    }

    private void welcomeUser(String username) {
        Message message = new Message()
                .setCurrentDateTime()
                .setUserID(userID)
                .setUsername(username)
                .setType(MessageType.WELCOME)
                .setRoomID(roomID)
                .setContent("Welcome " + username);
        broadcast(message);
        chatDao.saveMessage(message);
    }

    private void broadcastUserDisconnected(String username) {
        // Save THIS?
        broadcast(new Message().setUsername(username)
                .setType(MessageType.GOODBYE)
                .setRoomID(roomID)
                .setContent("Goodbye " + username));
    }

    private static class ActiveUsers {
        private Set<String> usernames;
        private MessageType type = MessageType.ACTIVE_USERS;

        public ActiveUsers(Set<String> usernames) {
            this.usernames = usernames;
        }

        public MessageType getType() {
            return type;
        }

        public void setType(MessageType type) {
            this.type = type;
        }

        public Set<String> getUsernames() {
            return usernames;
        }

        public void setUsernames(Set<String> usernames) {
            this.usernames = usernames;
        }
    }

    private void broadcastAvailableUsers() {

        Set<String> usernames = activeUsers.get(roomID).stream()
                .map(Session::getUserPrincipal)
                .map(Principal::getName)
                .collect(Collectors.toSet());
        try {
            String users = ObjectMapperFactory.get().writeValueAsString(new ActiveUsers(usernames));
            Set<Session> sessions = activeUsers.get(roomID);
            sessions.forEach(session -> {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText(users);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(Message message) {
//        synchronized (sessions) {
        Set<Session> sessions = activeUsers.get(roomID);
        sessions.forEach(session -> {
            if (session.isOpen()) {
                session.getAsyncRemote().sendObject(message);
            }
        });
//        }
    }


}
