package org.spr.CustomBenchmarks;

import io.rsocket.frame.decoder.PayloadDecoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.http.codec.protobuf.ProtobufEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

public class RequesterUtils {
    public static RSocketRequester newProtobufRequester(String host, int port) {
        return RSocketRequester
                .builder()
                .rsocketStrategies(RSocketStrategies
                        .builder()
                        .encoder(new ProtobufEncoder())
                        .decoder(new ProtobufDecoder())
                        .build()
                )
                .rsocketConnector(connector -> connector.payloadDecoder(PayloadDecoder.ZERO_COPY))
                .dataMimeType(new MimeType("application", "x-protobuf"))
                .tcp(host, port);
    }

    public static RSocketRequester newJsonRequester(String host, int port) {
        return RSocketRequester
                .builder()
                .rsocketStrategies(RSocketStrategies
                        .builder()
                        .encoder(new Jackson2JsonEncoder())
                        .decoder(new Jackson2JsonDecoder())
                        .build()
                )
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .tcp(host, port);
    }
}
