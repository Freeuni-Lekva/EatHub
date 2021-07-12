package ge.eathub.websockets;

import com.google.gson.Gson;
import ge.eathub.models.chat.Message;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {
    public static final Gson gson = new Gson();

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public Message decode(String s) throws DecodeException {
        return gson.fromJson(s, Message.class);
    }

    @Override
    public boolean willDecode(String s) {
        return s != null;
    }

}
