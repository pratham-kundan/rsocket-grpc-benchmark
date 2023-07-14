package org.spr.requests;

import org.spr.protos.ProtoMessage;
import org.spr.protos.ProtoSizedMessage;
import org.spr.protos.SizedMessageRequest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Class to make requests to server and return protobuf objects
 */
public class ProtoRequests {
    /**
     * Sends a request to the server and accepts a response
     * 
     * @param rSocketRequester Requester to make requests
     * @param requestText Text to send in the request
     * @return ProtoMessage containing server response
     */
    public static ProtoMessage requestResponse(RSocketRequester rSocketRequester, String requestText) {
        return rSocketRequester.route("request-response-proto")
                .data(ProtoMessage.newBuilder().setBody(requestText).build())
                .retrieveMono(ProtoMessage.class)
                .block();
    }

    /**
     * Sends a request to the server and accepts a stream
     *
     * @param rSocketRequester Requester to make requests
     * @param requestText Text to send in the request
     * @return List of ProtoMessage objects containing server response
     */
    public static List<ProtoMessage> requestStream(RSocketRequester rSocketRequester, String requestText) {
        return rSocketRequester.route("request-stream-proto")
                .data(ProtoMessage.newBuilder().setBody(requestText).build())
                .retrieveFlux(ProtoMessage.class)
                .collectList()
                .block();
    }

    /**
     * Sends a stream to the server containing request text and accepts a response
     *
     * @param rSocketRequester Requester to make requests
     * @param requestText Text to send in each request of the stream
     * @return ProtoMessage containing server response
     */
    public static ProtoMessage streamResponse(RSocketRequester rSocketRequester, String requestText) {
        Flux<ProtoMessage> request = Flux.range(1, 200)
                .map((__) -> ProtoMessage.newBuilder()
                        .setBody(requestText)
                        .build());
        return rSocketRequester
                .route("stream-response-proto")
                .data(request)
                .retrieveFlux(ProtoMessage.class)
                .blockLast();

    }

    /**
     * Sends a stream to the server containing request text and accepts a stream
     *
     * @param rSocketRequester Requester to make requests
     * @param requestText Text to send in each request of the stream
     * @return List of ProtoMessage objects containing server response
     */
    public static List<ProtoMessage> biStream(RSocketRequester rSocketRequester, String requestText) {
        Flux<ProtoMessage> request = Flux.range(1, 200)
                .map((__) -> ProtoMessage.newBuilder()
                        .setBody(requestText)
                        .build());
        return rSocketRequester
                .route("bi-stream-proto")
                .data(request)
                .retrieveFlux(ProtoMessage.class)
                .collectList()
                .block();
    }

    /**
     * Sends a request to the server and accepts a response containing document from the DB
     *
     * @param rSocketRequester Requester to make requests
     * @param messageId Id of document to send in the request
     * @return ProtoMessage containing document
     */
    public static ProtoMessage getById(RSocketRequester rSocketRequester, String messageId) {
        return rSocketRequester
                .route("get-proto")
                .data(ProtoMessage.newBuilder().setBody(messageId).build())
                .retrieveMono(ProtoMessage.class)
                .block();
    }

    /**
     * Sends a request to the server and accepts a stream of all docs in the DB
     *
     * @param rSocketRequester Requester to make requests
     * @return List of ProtoMessage objects containing all documents
     */
    public static List<ProtoMessage> getAllMessages(RSocketRequester rSocketRequester) {
        return rSocketRequester
                .route("get-all-proto")
                .retrieveFlux(ProtoMessage.class)
                .collectList()
                .block();
    }

    /**
     * Sends a list of messages for the server to add to DB and accepts a stream
     *
     * @param rSocketRequester Requester to make requests
     * @param messages List of message text to store in the database
     * @return List of ProtoMessage objects containing all document object ids
     */
    public static List<ProtoMessage> pushAll(RSocketRequester rSocketRequester, List<String> messages) {
        return rSocketRequester
                .route("push-all-proto")
                .data(Flux.fromIterable(messages).map(message -> ProtoMessage.newBuilder().setBody(message).build()))
                .retrieveFlux(ProtoMessage.class)
                .collectList()
                .block();
    }

    /**
     * Sends a list of message ids for server to delete and accepts a response
     *
     * @param rSocketRequester Requester to make requests
     * @param messageIds List of message ids to delete from the database
     * @return ProtoMessage containing number of deleted objects
     */
    public static ProtoMessage removeAll(RSocketRequester rSocketRequester, List<String> messageIds) {
        return rSocketRequester
                .route("remove-all-proto")
                .data(Flux
                        .fromIterable(messageIds)
                        .map(message -> ProtoMessage.newBuilder().setBody(message).build()))
                .retrieveFlux(ProtoMessage.class)
                .blockLast();
    }

    /**
     * Sends a request and returns a sized message from the server
     *
     * @param rSocketRequester Requester to make requests
     * @param sizeMb size of the objects the client needs
     * @return A Protobuf Object of required size
     */
    public static ProtoSizedMessage sizedRequestResponse(RSocketRequester rSocketRequester, int sizeMb) {
        return rSocketRequester.route("sized-request-response-proto")
                .data(SizedMessageRequest.newBuilder().setSizeMb(sizeMb).build())
                .retrieveMono(ProtoSizedMessage.class)
                .block();
    }

    /**
     * Sends a request and returns a list of sized messages from the server
     *
     * @param rSocketRequester Requester to make requests
     * @param sizeMb size of the objects the client needs
     * @return A list of Protobuf Object of 1MB each with total required size
     */
    public static List<ProtoSizedMessage> sizedRequestStream(RSocketRequester rSocketRequester, int sizeMb) {
        return rSocketRequester.route("sized-request-stream-proto")
                .data(SizedMessageRequest.newBuilder().setSizeMb(sizeMb).build())
                .retrieveFlux(ProtoSizedMessage.class)
                .collectList()
                .block();
    }
}
