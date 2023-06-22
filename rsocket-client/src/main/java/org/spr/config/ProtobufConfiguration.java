package org.spr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.http.codec.protobuf.ProtobufEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeType;

/**
 * Configuration to allow transfer of data as Protobuf
 */
@Configuration
@Profile("protobuf")
public class ProtobufConfiguration {

    /**
     * Bean to make a Protobuf RSocketRequester from the configured RSocketStrategies
     * @param rSocketStrategies wired RSocket Strategies
     * @return RSocketRequester object for Protobuf transmission
     */
    @Bean
    public RSocketRequester rSocketRequester(RSocketStrategies rSocketStrategies) {
        return RSocketRequester.builder()
                .rsocketStrategies(rSocketStrategies)

                .dataMimeType(new MimeType("application", "x-protobuf"))
                .tcp("localhost", 8989);
    }

    /**
     * Bean that sets the encoder and decoder to Jackson2Json
     * also sets the mimetype to application/json
     * @return RSocketStrategy object for Protobuf encoding decoding
     */
    @Bean
    public RSocketStrategies customRsocketStrategies() {
        return RSocketStrategies.builder()
                .decoder(new ProtobufDecoder())
                .encoder(new ProtobufEncoder())
                .build();
    }
}
