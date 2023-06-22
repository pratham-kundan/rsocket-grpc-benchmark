package org.spr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;

/**
 * Configuration to allow transfer of data as JSON
 */
@Configuration
@Profile("json")
public class JsonConfiguration {
    /**
     * Bean to make a JSON RSocketRequester from the configured RSocketStrategies
     * @param rSocketStrategies wired RSocket Strategies
     * @return RSocketRequester object for JSON transmission
     */
    @Bean
    public RSocketRequester rSocketRequester(RSocketStrategies rSocketStrategies) {
        return RSocketRequester.builder()
                .rsocketStrategies(rSocketStrategies)
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .tcp("localhost", 8989);
    }

    /**
     * Bean that sets the encoder and decoder to Jackson2Json
     * also sets the mimetype to application/json
     * @return RSocketStrategy object for JSON encoding decoding
     */
    @Bean
    public RSocketStrategies customRsocketStrategies() {
        return RSocketStrategies.builder()
                .decoder(new Jackson2JsonDecoder())
                .encoder(new Jackson2JsonEncoder())
                .build();
    }
}
