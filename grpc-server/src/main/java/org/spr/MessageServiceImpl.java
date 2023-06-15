package org.spr;

import com.mongodb.client.FindIterable;
import com.mongodb.client.result.InsertOneResult;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.spr.protos.MessageServiceGrpc;
import org.spr.protos.SimpleMessage;

import java.time.Instant;
import java.util.stream.Stream;

public class MessageServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {
    @Override
    public void ping(SimpleMessage request, StreamObserver<SimpleMessage> responseObserver) {
        responseObserver.onNext(
                SimpleMessage.newBuilder()
                        .setBody("Acknowledged: " + request.getBody())
                        .build()
        );

        responseObserver.onCompleted();
    }

    @Override
    public void echoStream(SimpleMessage request, StreamObserver<SimpleMessage> responseObserver) {
        Stream<SimpleMessage> mStream = Stream.iterate(SimpleMessage
                .newBuilder()
                .setBody("Responding to: " + request.getBody() + " at:" + Instant.now())
                .build(), a -> a).limit(100);

        mStream.forEach(responseObserver::onNext);

        responseObserver.onCompleted();
    }

    @Override
    public void create(SimpleMessage request, StreamObserver<SimpleMessage> responseObserver) {
        InsertOneResult inserted = DatabaseHelper.getMongoClient()
                .getDatabase("myDb")
                .getCollection("messages")
                .insertOne(new Document().append("body", request.getBody()));

        responseObserver.onNext(SimpleMessage.newBuilder()
                .setId(inserted.getInsertedId().toString())
                .build());
    }

    @Override
    public void getAll(SimpleMessage request, StreamObserver<SimpleMessage> responseObserver) {
        FindIterable<Document> all = DatabaseHelper.getMongoClient()
                .getDatabase("myDb")
                .getCollection("message")
                .find();

        all.iterator().forEachRemaining(doc -> responseObserver
                .onNext(SimpleMessage.newBuilder()
                        .setId(doc.getString("_id"))
                        .setBody(doc.getString("body"))
                        .build())
        );
    }
}
