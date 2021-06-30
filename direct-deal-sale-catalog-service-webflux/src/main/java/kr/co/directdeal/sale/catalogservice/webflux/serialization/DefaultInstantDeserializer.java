package kr.co.directdeal.sale.catalogservice.webflux.serialization;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

public class DefaultInstantDeserializer extends InstantDeserializer<Instant> {
    public DefaultInstantDeserializer() {
        super(Instant.class, DateTimeFormatter.ISO_INSTANT,
                Instant::from,
                a -> Instant.ofEpochMilli(a.value),
                a -> Instant.ofEpochSecond(a.integer, a.fraction),
                null,
                true);
    }
}
