# CustomBench

A collection of classes meant to benchmark the throughput of a function.
It is not as sophisticated as JMH. Just something made to verify the results from it.

## Usage:

Define a class that implements the BmTask interface

```java
public class BaseGrpcBenchMark implements BmTask {
    // setup
    public SizedMessageServiceGrpc.SizedMessageServiceBlockingStub sizedMessageServiceStub;
    public MessageServiceGrpc.MessageServiceBlockingStub messageServiceStub;
    public MessageDbServiceGrpc.MessageDbServiceBlockingStub dbServiceStub;
    ManagedChannel channel;

    @Override
    public void run() {
    }

    // Run before running the function for provided duration
    @Override
    public void beforeAll() {
        channel = ManagedChannelBuilder
                .forAddress("localhost", 8787)
                .usePlaintext()
                .build();

        sizedMessageServiceStub = SizedMessageServiceGrpc.newBlockingStub(channel);

        messageServiceStub = MessageServiceGrpc.newBlockingStub(channel);

        dbServiceStub = MessageDbServiceGrpc.newBlockingStub(channel);
    }
    
    // Run after running the function for provided duration
    @Override
    public void afterAll() {
        channel.shutdown();
    }
}

public class ReqResBenchmark extends BaseGrpcBenchMark {
    @Override
    public void run() {
        Requests.requestResponse(messageServiceStub, "Hello from Client");
    }
}

public class ReqStreamBenchmark extends BaseGrpcBenchMark {
    @Override
    public void run() {
        Requests.requestStream(messageServiceStub, "Hello from Client");
    }
}

```

To run the benchmark:
```java
public class BmRunner {
    public static void simpleBenchMark() throws Exception {
        BmCompoundRunner simpleRequestResponseRunner = new BmCompoundRunner.Builder()
                .addBenchmark(ReqResBenchmark.class, Duration.ofMinutes(1), 10, 2)
                .addBenchmark(ReqResBenchmark.class, Duration.ofMinutes(1), 20, 2)
                .addBenchmark(ReqStreamBenchmark.class, Duration.ofMinutes(1), 10, 2)
                .addBenchmark(ReqStreamBenchmark.class, Duration.ofMinutes(1), 20, 2)
                .build();
        simpleRequestResponseRunner.runBenchMark();
    }

    public static void main(String[] args) throws Exception {
        simpleBenchMark();
    }
}
```