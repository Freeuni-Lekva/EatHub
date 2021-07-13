package ge.eathub.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ge.eathub.utils.ObjectMapperFactory;
import ge.eathub.models.chat.SocketMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<SocketMessage> {

    public static final ObjectMapper mapper = ObjectMapperFactory.get();

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public SocketMessage decode(String s) throws DecodeException {
        try {
            return mapper.readValue(s, SocketMessage.class);
        } catch (JsonProcessingException e) {
            throw new DecodeException(s, e.getMessage(), e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return s != null;
    }

}
