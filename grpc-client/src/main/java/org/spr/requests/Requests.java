package org.spr.requests;

import io.grpc.stub.StreamObserver;
import org.spr.protos.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Requests {
    public static ProtoMessage requestResponse(MessageServiceGrpc.MessageServiceBlockingStub stub, String requestText) {
        ProtoMessage message = ProtoMessage
                .newBuilder()
                .setBody(requestText + Instant.now())
                .build();

        return stub.requestResponse(message);
    }

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

    public static ProtoMessage getById(MessageDbServiceGrpc.MessageDbServiceBlockingStub stub, String messageId) {
        return stub.getById(ProtoMessage.newBuilder().setBody(messageId).build());
    }

    public static List<ProtoMessage> getAllMessages(MessageDbServiceGrpc.MessageDbServiceBlockingStub stub) {
        List<ProtoMessage> protoMessageList = new ArrayList<>();

        stub
                .getAll(ProtoMessage.newBuilder().build())
                .forEachRemaining(protoMessageList::add);

        return protoMessageList;
    }

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

    public static ProtoSizedMessage sizedRequestResponse(SizedMessageServiceGrpc.SizedMessageServiceBlockingStub stub, int sizeMb) {
        return stub.sizedRequestResponse(SizedMessageRequest
                .newBuilder()
                .setSizeMb(sizeMb)
                .build()
        );
    }

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
