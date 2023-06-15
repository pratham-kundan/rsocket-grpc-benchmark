package org.spr.utils;

import org.spr.data.Message;
import org.spr.protos.SimpleMessage;

public class MessageUtils {
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
