package ge.eathub.websockets;


import com.fasterxml.jackson.core.JsonProcessingException;
import ge.eathub.models.chat.MessageType;
import ge.eathub.utils.ObjectMapperFactory;
import ge.eathub.models.chat.SocketMessage;

import javax.enterprise.context.Dependent;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Dependent
@ServerEndpoint(
        value = "/chat",
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class,
        configurator = ChatConfig.class)
public class ChatEndpoint {

    private static final Map<Long, Set<Session>> activeUsers = new ConcurrentHashMap<>();
    Long roomID;

    @OnOpen
    public void onOpen(Session session) {
        roomID = Long.valueOf(session.getRequestParameterMap().get("room-id").get(0));
        Set<Session> ses = activeUsers.getOrDefault(roomID, null);
        if (ses == null) {
            ses = new CopyOnWriteArraySet<>();
            ses.add(session);
            activeUsers.put(roomID, ses);
        } else {
            ses.add(session);
        }

        String username = session.getUserPrincipal().getName();

        welcomeUser(session, username);
        broadcastAvailableUsers();
    }

    @OnMessage
    public void onMessage(Session session, SocketMessage message) {
        System.out.println("on message :" + message);
        broadcast(message.setUsername(session.getUserPrincipal().getName()));
    }

    @OnClose
    public void onClose(Session session) {
        activeUsers.get(roomID).remove(session);
        String username = session.getUserPrincipal().getName();
        broadcastUserDisconnected(username);
        broadcastAvailableUsers();
    }

    private void welcomeUser(Session currentSession, String username) {
        currentSession.getAsyncRemote()
                .sendObject(new SocketMessage()
                        .setUsername(username)
                        .setType(MessageType.WELCOME)
                        .setRoomID(roomID)
                        .setContent("Welcome " + username));
    }

    private void broadcastUserDisconnected(String username) {
        broadcast(new SocketMessage().setUsername(username)
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

    private void broadcast(SocketMessage message) {
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
