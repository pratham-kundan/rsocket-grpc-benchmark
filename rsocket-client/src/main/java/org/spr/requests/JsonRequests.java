package org.spr.requests;

import org.spr.data.MessageDto;
import org.spr.data.SizedMessage;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.util.List;

/**
 * Class to make requests to server and return objects
 */
public class JsonRequests {
    public static MessageDto requestResponse(RSocketRequester rSocketRequester, String requestText) {
        return rSocketRequester.route("request-response-json")
                .data(new MessageDto("", requestText))
                .retrieveMono(MessageDto.class)
                .block();
    }

    public static List<MessageDto> requestStream(RSocketRequester rSocketRequester, String requestText) {
        return rSocketRequester.route("request-stream-json")
                .data(new MessageDto("", requestText))
                .retrieveFlux(MessageDto.class)
                .collectList()
                .block();
    }

    public static String createMessage(RSocketRequester rSocketRequester, String message) {
        return rSocketRequester.route("create")
                .data(message)
                .retrieveMono(String.class)
                .block();
    }

    public static List<MessageDto> getAllMessages(RSocketRequester rSocketRequester) {
        return rSocketRequester.route("get-all")
                .retrieveFlux(MessageDto.class)
                .collectList()
                .block();
    }

    public static SizedMessage sizedRequestResponse(RSocketRequester rSocketRequester, int sizeMb) {
        return rSocketRequester.route("sized-request-response")
                .data(sizeMb)
                .retrieveMono(SizedMessage.class)
                .block();
    }

    public static List<SizedMessage> sizedRequestStream(RSocketRequester rSocketRequester, int sizeMb) {
        return rSocketRequester.route("sized-request-stream")
                .data(sizeMb)
                .retrieveFlux(SizedMessage.class)
                .collectList()
                .block();
    }
}
