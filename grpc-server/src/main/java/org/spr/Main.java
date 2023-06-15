package org.spr;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class Main {
    public static void main(String[] args) throws Exception {
        DatabaseHelper.connect("mongodb://localhost:27017");
        Server server = ServerBuilder.forPort(9090)
                .addService(new MessageServiceImpl())
                .build();

        server.start().awaitTermination();
    }
}