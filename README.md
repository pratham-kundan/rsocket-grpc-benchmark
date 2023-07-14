# rsocket-grpc-benchmark
### Benchmarking RSockets and gRPC

[Some details here](https://gabby-salute-8bc.notion.site/Intern-Presentation-4fcc61d97c7b44f59d36653b9f57e30f)

To run the rsocket-server use:
`./gradlew :rsocket-server:bootRun`

To run the benchmark for rsockets use:

`./gradlew :rsocket-client:runBm`

To run the grpc-server use:

`./gradlew :grpc-server:bootRun`

To run the benchmark for rsockets use:

`./gradlew :grpc-client:runBm`

(Requires a running mongodb instance to run the database related benchmarks.)

Made in java-17.0.7 gradle 8.1.1
