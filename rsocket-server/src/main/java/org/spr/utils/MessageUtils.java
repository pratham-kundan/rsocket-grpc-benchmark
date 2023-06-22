package org.spr.utils;

import org.spr.data.Message;
import org.spr.data.MessageDto;
import org.spr.protos.SimpleMessage;

public class MessageUtils {

    /**
     * Function to convert Message Mongo Document to a Message protobuf
     *
     * @param message Mongo document
     * @return Message protobuf
     */
    public static SimpleMessage messageToProto(Message message) {
        return SimpleMessage.newBuilder()
                .setBody(message.getBody())
                .setId(message.getId())
                .build();
    }

    /**
     * Function to convert Message Mongo Document to a Message Data Object
     *
     * @param message Mongo document
     * @return Message data object
     */
    public static MessageDto messageToDto(Message message) {
        return new MessageDto(message.getId(), message.getBody());
    }

}
