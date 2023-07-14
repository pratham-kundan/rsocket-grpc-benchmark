# rsocket-grpc-benchmark
### Benchmarking RSockets and gRPC

[Some details here](https://gabby-salute-8bc.notion.site/Intern-Presentation-4fcc61d97c7b44f59d36653b9f57e30f)

### Server

(Change server port in application.properties)

To run the rsocket-server use:
`./gradlew :rsocket-server:bootRun`

To run the grpc-server use:
`./gradlew :grpc-server:bootRun`

### Benchmarks

(Change server host and port in application.properties. Class under benchmark can be changed in the respective runner classes)

To run the benchmark for RSockets use:
`./gradlew :rsocket-client:runBm`


To run the benchmark for gRPC use:
`./gradlew :grpc-client:runBm`

### Requirements

* A running mongodb instance to run the database related benchmarks (change db connection in application.properties)
* java-17.0.7 

###Benchmarks
Benchmarks are present in the `JMHBenchmarks` package in both of `rsocket-client` and `grpc-client`.

Benchmarking suites exist as classes contains 4 benchmark functions executed on 10, 20, 50 and 100
threads respectively. This was managed by the `@Thread` annotation of JMH.

PS : `CustomBenchmarks` package contains benchmarks run by the `custom-bench` sub-project.
They exist just to verify the throughput from JMH and are not used in the results. 

## Results
Result graphs and visualisation python scripts present in : `./Results`

Benchmarks were run with the server as a local process and also as a 
containerized process. Graphs for both are present in respective folders.

![request_response_throughput](https://github.com/pratham-kundan/rsocket-grpc-benchmark/assets/135799108/49c77ca4-68ea-4ac8-87bf-8ac7bff193af)
![request_stream_throughput](https://github.com/pratham-kundan/rsocket-grpc-benchmark/assets/135799108/8e7705be-4ef8-41ff-9823-328e1fdf7ae2)
