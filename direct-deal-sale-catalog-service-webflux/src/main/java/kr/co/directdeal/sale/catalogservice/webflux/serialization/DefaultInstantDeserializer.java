package kr.co.directdeal.sale.catalogservice.webflux.serialization;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

/**
 * Custom Instant deserializer for JSON serialization/deserialization.
 * This deserializer uses ISO_INSTANT format for parsing Instant objects.
 * It extends Jackson's InstantDeserializer to provide default Instant parsing behavior.
 *
 * Author: Cheol Jeon
 */
public class DefaultInstantDeserializer extends InstantDeserializer<Instant> {

    /**
     * Constructs a DefaultInstantDeserializer with predefined parsing logic
     * for ISO_INSTANT formatted strings and epoch-based inputs.
     */
    public DefaultInstantDeserializer() {
        super(Instant.class, DateTimeFormatter.ISO_INSTANT,
                Instant::from,
                a -> Instant.ofEpochMilli(a.value),
                a -> Instant.ofEpochSecond(a.integer, a.fraction),
                null,
                true);
    }
}
