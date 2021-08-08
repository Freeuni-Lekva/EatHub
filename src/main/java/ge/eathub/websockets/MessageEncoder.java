package ge.eathub.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ge.eathub.models.chat.Message;
import ge.eathub.utils.ObjectMapperFactory;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {
    public static final ObjectMapper mapper = ObjectMapperFactory.get();

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(Message object) throws EncodeException {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new EncodeException(object, e.getMessage(), e);
        }
    }
}
