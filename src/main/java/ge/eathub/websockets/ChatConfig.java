package ge.eathub.websockets;

import ge.eathub.dao.ChatDao;
import ge.eathub.models.UserPrincipal;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import static ge.eathub.listener.NameConstants.CHAT_DAO;

public class ChatConfig extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig conf,
                                HandshakeRequest req,
                                HandshakeResponse resp) {
        System.out.println(" started handshake");
        HttpSession ses = (HttpSession) req.getHttpSession();
        ChatDao chatDao = (ChatDao) ses.getServletContext().getAttribute(CHAT_DAO);

        UserPrincipal user = (UserPrincipal) req.getUserPrincipal();
        String username = user.getName();
        Long userID = user.getUserID();
        Long roomID = Long.valueOf(req.getParameterMap().get("room-id").get(0));

        conf.getUserProperties().put(CHAT_DAO, chatDao);
    }

}
