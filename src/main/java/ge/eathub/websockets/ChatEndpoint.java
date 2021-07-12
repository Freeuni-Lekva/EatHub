package ge.eathub.websockets;


import ge.eathub.models.chat.Message;
import ge.eathub.models.chat.MessageType;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//@ApplicationScoped
@ServerEndpoint(value = "/chat/",
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class,
        configurator = ChatConfig.class)
public class ChatEndpoint {

    private Session session;

    private static final Map<Long, Session> activeUsers = new ConcurrentHashMap<>();
    Long roomID = 1L;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws EncodeException, IOException {
        roomID = 123L;
        System.out.println("opened room : " + roomID);
        if (!activeUsers.containsKey(roomID)) {
            activeUsers.put(roomID, session);
        }
//        activeUsers.putIfAbsent(roomID, session);
        Message message = new Message(-199L, roomID, MessageType.TEXT, " just opened ");
        broadcastAvailableUsers(roomID);
        broadcast(message, roomID);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        System.out.println("on close " + roomID);

        broadcast(message, roomID);
    }


    @OnClose
    public void onClose(Session session, CloseReason closeReason) throws EncodeException, IOException {
        System.out.println("on close " + roomID);
        Message message = new Message(-1L, roomID, MessageType.TEXT, "user left ");
//        broadcast(message, roomID);
    }

    private static void broadcast(Message message, Long roomID)
            throws IOException, EncodeException {
        System.out.println(message);
        System.out.println(roomID);

        activeUsers.get(roomID).getOpenSessions().forEach(ses -> {
            synchronized (ses) {
                try {
                    ses.getBasicRemote().
                            sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void broadcastAvailableUsers(Long roomID) {

        Set<String> usernames = activeUsers.get(roomID).getOpenSessions().stream()
                .map(Session::getUserPrincipal)
                .map(Principal::getName)
//                .distinct()
                .collect(Collectors.toSet());
        System.out.println(usernames);
    }

    @OnError
    public void error(Session session, Throwable t) {
        System.out.println("on error ");
        t.printStackTrace();
    }
}
