package ge.eathub.websockets;

import javax.websocket.HandshakeResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ChatConfig extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig conf,
                                HandshakeRequest req,
                                HandshakeResponse resp) {
        System.out.println(" started handshake");
//        TODO validate
        System.out.println(req.getHeaders());
        System.out.println("user principal: " + req.getUserPrincipal());
        System.out.println("http session:" + req.getHttpSession());
        System.out.println(resp.getHeaders());

//        conf.getUserProperties().put("handshakereq", req);
    }

}
