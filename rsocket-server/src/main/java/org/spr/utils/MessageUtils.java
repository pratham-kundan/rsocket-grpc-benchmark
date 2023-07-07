package org.spr.utils;

import org.spr.data.Message;
import org.spr.protos.ProtoMessage;

public class MessageUtils {

    /**
     * Function to convert Message MongoDB Document to a Message protobuf
     *
     * @param message Mongo document
     * @return Message protobuf
     */
    public static ProtoMessage messageToProto(Message message) {
        return ProtoMessage.newBuilder()
                .setBody(message.getBody())
                .setId(message.getId())
                .build();
    }
}
