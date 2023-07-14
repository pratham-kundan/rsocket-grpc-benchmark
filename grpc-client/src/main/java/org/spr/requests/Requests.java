package org.spr.requests;

import io.grpc.stub.StreamObserver;
import org.spr.protos.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Class to make requests to server and return protobuf objects
 */
public class Requests {
    /**
     * Sends a request to the server and accepts a response
     *
     * @param stub stub to make requests
     * @param requestText Text to send in the request
     * @return ProtoMessage containing server response
     */
    public static ProtoMessage requestResponse(MessageServiceGrpc.MessageServiceBlockingStub stub, String requestText) {
        ProtoMessage message = ProtoMessage
                .newBuilder()
                .setBody(requestText + Instant.now())
                .build();

        return stub.requestResponse(message);
    }


    /**
     * Sends a request to the server and accepts a stream
     *
     * @param stub stub to make requests
     * @param requestText Text to send in the request
     * @return List of ProtoMessage objects containing server response
     */
    public static List<ProtoMessage> requestStream(MessageServiceGrpc.MessageServiceBlockingStub stub, String requestText) {
        ProtoMessage message = ProtoMessage
                .newBuilder()
                .setBody(requestText + Instant.now())
                .build();
        List<ProtoMessage> protoMessageList = new ArrayList<>();

        stub.requestStream(message)
                .forEachRemaining(protoMessageList::add);

        return protoMessageList;
    }

    /**
     * Sends a stream to the server containing request text and accepts a response
     *
     * @param stub stub to make requests
     * @param requestText Text to send in each request of the stream
     * @return ProtoMessage containing server response
     */
    public static ProtoMessage streamResponse(MessageServiceGrpc.MessageServiceStub stub, String requestText) throws InterruptedException {
        final ProtoMessage[] response = new ProtoMessage[1];
        CountDownLatch cdl = new CountDownLatch(1);
        StreamObserver<ProtoMessage> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(ProtoMessage value) {
                response[0] = value;
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                cdl.countDown();
            }
        };

        StreamObserver<ProtoMessage> requestObserver = stub.streamResponse(responseObserver);
        for (int i = 0; i < 200; i++) requestObserver.onNext(ProtoMessage.newBuilder().setBody(requestText).build());

        requestObserver.onCompleted();
        boolean received = cdl.await(20, TimeUnit.SECONDS);
        return response[0];
    }

    /**
     * Sends a stream to the server containing request text and accepts a stream
     *
     * @param stub stub to make requests
     * @param requestText Text to send in each request of the stream
     * @return List of ProtoMessage objects containing server response
     */
    public static List<ProtoMessage> biStream(MessageServiceGrpc.MessageServiceStub stub, String requestText) throws InterruptedException {
        List<ProtoMessage> response = new ArrayList<>();
        CountDownLatch cdl = new CountDownLatch(1);
        StreamObserver<ProtoMessage> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(ProtoMessage value) {
                response.add(value);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                cdl.countDown();
            }
        };

        StreamObserver<ProtoMessage> requestObserver = stub.biStream(responseObserver);

        for (int i = 0; i < 200; i++) requestObserver.onNext(ProtoMessage.newBuilder().setBody(requestText).build());
        requestObserver.onCompleted();

        boolean received = cdl.await(20, TimeUnit.SECONDS);
        return response;
    }

    /**
     * Sends a request to the server and accepts a response containing document from the DB
     *
     * @param stub stub to make requests
     * @param messageId Id of document to send in the request
     * @return ProtoMessage containing document
     */
    public static ProtoMessage getById(MessageDbServiceGrpc.MessageDbServiceBlockingStub stub, String messageId) {
        return stub.getById(ProtoMessage.newBuilder().setBody(messageId).build());
    }

    /**
     * Sends a request to the server and accepts a stream of all docs in the DB
     *
     * @param stub stub to make requests
     * @return List of ProtoMessage objects containing all documents
     */
    public static List<ProtoMessage> getAllMessages(MessageDbServiceGrpc.MessageDbServiceBlockingStub stub) {
        List<ProtoMessage> protoMessageList = new ArrayList<>();

        stub
                .getAll(ProtoMessage.newBuilder().build())
                .forEachRemaining(protoMessageList::add);

        return protoMessageList;
    }

    /**
     * Sends a list of messages for the server to add to DB and accepts a stream
     *
     * @param stub stub to make requests
     * @param messages List of message text to store in the database
     * @return List of ProtoMessage objects containing all document object ids
     */
    public static List<ProtoMessage> pushAll(MessageDbServiceGrpc.MessageDbServiceStub stub, List<String> messages) throws InterruptedException {
        List<ProtoMessage> response = new ArrayList<>();
        CountDownLatch cdl = new CountDownLatch(1);
        StreamObserver<ProtoMessage> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(ProtoMessage value) {
                response.add(value);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                cdl.countDown();
            }
        };

        StreamObserver<ProtoMessage> requestObserver = stub.pushAll(responseObserver);

        for (String message : messages) {
            requestObserver.onNext(ProtoMessage
                    .newBuilder()
                    .setBody(message)
                    .build());
        }
        requestObserver.onCompleted();

        boolean received = cdl.await(20, TimeUnit.SECONDS);
        return response;
    }

    /**
     * Sends a list of message ids for server to delete and accepts a response
     *
     * @param stub stub to make requests
     * @param messageIds List of message ids to delete from the database
     * @return ProtoMessage containing number of deleted objects
     */
    public static ProtoMessage removeAll(MessageDbServiceGrpc.MessageDbServiceStub stub, List<String> messageIds) throws InterruptedException {
        final ProtoMessage[] response = new ProtoMessage[1];
        CountDownLatch cdl = new CountDownLatch(1);
        StreamObserver<ProtoMessage> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(ProtoMessage value) {
                response[0] = value;
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                cdl.countDown();
            }
        };

        StreamObserver<ProtoMessage> requestObserver = stub.removeAll(responseObserver);

        for (String messageId : messageIds) {
            requestObserver.onNext(ProtoMessage
                    .newBuilder()
                    .setBody(messageId)
                    .build());
        }
        requestObserver.onCompleted();

        boolean received = cdl.await(20, TimeUnit.SECONDS);
        return response[0];
    }

    /**
     * Sends a request and returns a sized message from the server
     *
     * @param stub stub to make requests
     * @param sizeMb size of the objects the client needs
     * @return A Protobuf Object of required size
     */
    public static ProtoSizedMessage sizedRequestResponse(SizedMessageServiceGrpc.SizedMessageServiceBlockingStub stub, int sizeMb) {
        return stub.sizedRequestResponse(SizedMessageRequest
                .newBuilder()
                .setSizeMb(sizeMb)
                .build()
        );
    }

    /**
     * Sends a request and returns a list of sized messages from the server
     *
     * @param stub stub to make requests
     * @param sizeMb size of the objects the client needs
     * @return A list of Protobuf Object of 1MB each with total required size
     */
    public static List<ProtoSizedMessage> sizedRequestStream(SizedMessageServiceGrpc.SizedMessageServiceBlockingStub stub, int sizeMb) {
        List<ProtoSizedMessage> response = new ArrayList<>();
        stub.sizedRequestStream(SizedMessageRequest
                .newBuilder()
                .setSizeMb(sizeMb)
                .build()
        ).forEachRemaining(response::add);

        return response;
    }
}
