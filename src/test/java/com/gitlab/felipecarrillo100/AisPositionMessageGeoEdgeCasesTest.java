package com.gitlab.felipecarrillo100;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class AisPositionMessageGeoEdgeCasesTest {

    private AisEncoder encoder;
    private AisDecoder decoder;

    // To capture decoded messages from the async callbacks
    private final BlockingQueue<AisPositionMessage> positionMessages = new LinkedBlockingQueue<>();
    private final BlockingQueue<AisStaticMessage> staticMessages = new LinkedBlockingQueue<>();

    @BeforeEach
    public void setup() {
        encoder = new AisEncoder();
        decoder = new AisDecoder();

        // Setup callbacks to capture decoded messages
        decoder.setPositionCallback(positionMessages::offer);
        decoder.setStaticCallback(staticMessages::offer);

        positionMessages.clear();
        staticMessages.clear();
    }

    @Test
    public void testPositionMessagesWithWorldLocations() throws InterruptedException {
        List<AisPositionMessage> testMessages = List.of(
                createPositionMessage(0.0, 0.0),          // Equator & Prime Meridian
                createPositionMessage(90.0, 0.0),         // North Pole
                createPositionMessage(-90.0, 0.0),        // South Pole
                createPositionMessage(0.0, 180.0),        // Intl Date Line East
                createPositionMessage(0.0, -180.0),       // Intl Date Line West
                createPositionMessage(45.0, 45.0),        // NE Hemisphere
                createPositionMessage(45.0, -45.0),       // NW Hemisphere
                createPositionMessage(-45.0, 45.0),       // SE Hemisphere
                createPositionMessage(-45.0, -45.0),      // SW Hemisphere
                createPositionMessage(89.9999, 135.0),    // Near North Pole
                createPositionMessage(-89.9999, -135.0),  // Near South Pole
                createPositionMessage(10.0, 179.9999),    // Near Date Line East
                createPositionMessage(-10.0, -179.9999)   // Near Date Line West
        );

        for (AisPositionMessage original : testMessages) {
            // Encode into NMEA sentences (usually one sentence for position messages)
            List<String> nmeaSentences = encoder.encodePositionMessage(original);

            // Feed all sentences to the decoder in sequence (handles multipart internally)
            for (String sentence : nmeaSentences) {
                decoder.onSentence(sentence);
            }

            // Wait and retrieve decoded message from the queue (with timeout)
            AisPositionMessage decoded = positionMessages.poll(1, TimeUnit.SECONDS);
            assertNotNull(decoded, "Decoder did not produce a position message in time");

            // Assert values with tolerance for floating point rounding errors
            assertEquals(original.getMmsi(), decoded.getMmsi());
            assertEquals(original.getNavStatus(), decoded.getNavStatus());
            assertEquals(original.getSog(), decoded.getSog(), 0.1);
            assertEquals(original.getAccuracy(), decoded.getAccuracy());
            assertEquals(original.getCog(), decoded.getCog(), 0.1);
            assertEquals(original.getHeading(), decoded.getHeading());

            // Lat/lon tolerance (AIS uses fixed point with ~0.0001 deg resolution)
            assertEquals(original.getLat(), decoded.getLat(), 0.0002);
            assertEquals(original.getLon(), decoded.getLon(), 0.0002);
        }
    }

    private AisPositionMessage createPositionMessage(double lat, double lon) {
        AisPositionMessage msg = new AisPositionMessage();
        msg.setMmsi(123456789);
        msg.setMessageType(1);
        msg.setRepeat(0);
        msg.setNavStatus(0);
        msg.setRateOfTurn(-128);
        msg.setSog(12.3);
        msg.setAccuracy(true);
        msg.setLon(lon);
        msg.setLat(lat);
        msg.setCog(123.4);
        msg.setHeading(89);
        msg.setTimestamp(59);
        msg.setSpecialManoeuvre(0);
        msg.setRaim(false);
        msg.setRadio(12345);
        msg.setChannel("A");
        return msg;
    }
}
