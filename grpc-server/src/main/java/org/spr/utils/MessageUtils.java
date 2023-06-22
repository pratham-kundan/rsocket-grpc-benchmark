package org.spr.utils;

import org.spr.data.Message;
import org.spr.protos.SimpleMessage;

/**
 * Util class to convert mongo document to protobuf
 */
public class MessageUtils {

    /**
     * Function to convert Message Mongo Document to a Message protobuf
     *
     * @param message Mongo document
     * @return Message protobuf
     */
    public static SimpleMessage messageToProto(Message message) {
        return SimpleMessage
                .newBuilder()
                .setBody(message.getBody())
                .setId(message.getId())
                .build();
    }

    public static Message protoToMessage(SimpleMessage proto) {
        return new Message(proto.getBody());
    }
}
