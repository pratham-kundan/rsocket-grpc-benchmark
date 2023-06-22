package org.spr.config;

import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.http.codec.protobuf.ProtobufEncoder;
import org.springframework.messaging.rsocket.RSocketStrategies;

/**
 * Configuration for the rsocket server
 */
@Configuration
public class ServerConfig {
    /**
     * Bean that returns a customised rsocket server with set fragment size
     * (Needed for transferring very large objects >16MB)
     *
     * @return rsocket server with set fragment size
     */
    @Bean
    public RSocketServerCustomizer serverCustomizer() {
        return (server) -> server.fragment(16777215);
    }

    /**
     * Returns Rsocket strategies with suitable encoders and decoders set
     *
     * @return RSocket strategies with suitable encoders and decoders
     */
    @Bean
    public RSocketStrategies customRsocketStrategies() {
        return RSocketStrategies.builder()
                .decoder(new ProtobufDecoder())
                .decoder(new Jackson2JsonDecoder())
                .encoder(new ProtobufEncoder())
                .encoder(new Jackson2JsonEncoder())
                .build();
    }
}