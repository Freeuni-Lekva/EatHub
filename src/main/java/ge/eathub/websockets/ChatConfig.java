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
//        try {
//            Long roomID = Long.valueOf(req.getParameterMap().get("room-id").get(0));
//            conf.getUserProperties().put("room-id",roomID);
//        } catch (Exception e){
                // how to stop ??
//        }

        System.out.println(req.getParameterMap());

    }

}
