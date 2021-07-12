package ge.eathub.websockets;

import com.google.gson.Gson;
import ge.eathub.models.chat.Message;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {
    public static final Gson gson = new Gson();
    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(Message object) throws EncodeException {
         return gson.toJson(object);
    }
}
