package com.gitlab.felipecarrillo100;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

public class AisEncoderDecoderTest {

    @Test
    public void testEncodeDecodePositionMessage() {
        AisPositionMessage original = new AisPositionMessage();
        original.setMmsi(123456789);
        original.setRepeat(0);
        original.setNavStatus(0);
        original.setRateOfTurn(0);
        original.setSog(12.3);
        original.setAccuracy(true);
        original.setLon(-74.0060);
        original.setLat(40.7128);
        original.setCog(85.5);
        original.setHeading(90);
        original.setTimestamp(50);
        original.setSpecialManoeuvre(0);
        original.setRaim(false);
        original.setRadio(123456);
        original.setChannel("A");

        // Encode
        List<String> sentences = AisEncoder.encodePositionMessage(original);
        assertNotNull(sentences);
        assertFalse(sentences.isEmpty());

        System.out.println("Encoded Position Sentences:");
        for (String s : sentences) {
            System.out.println(s);
        }

        AisDecoder decoder = new AisDecoder();
        AtomicReference<AisPositionMessage> decodedRef = new AtomicReference<>();

        decoder.setPositionCallback(msg -> {
            System.out.println("Position callback triggered with MMSI: " + msg.getMmsi());
            decodedRef.set(msg);
        });

        for (String s : sentences) {
            System.out.println("Feeding sentence to decoder: " + s);
            decoder.onSentence(s);
        }

        AisPositionMessage decoded = decodedRef.get();
        System.out.println("Decoded position message: " + decoded);

        assertNotNull(decoded, "Decoded position message should not be null");

        // Assert fields, allowing small differences due to encoding rounding
        assertEquals(original.getMmsi(), decoded.getMmsi());
        assertEquals(original.getRepeat(), decoded.getRepeat());
        assertEquals(original.getNavStatus(), decoded.getNavStatus());
        assertEquals(original.getRateOfTurn(), decoded.getRateOfTurn());
        assertEquals(original.getSog(), decoded.getSog(), 0.1);
        assertEquals(original.getAccuracy(), decoded.getAccuracy());
        assertEquals(original.getLon(), decoded.getLon(), 0.0001);
        assertEquals(original.getLat(), decoded.getLat(), 0.0001);
        assertEquals(original.getCog(), decoded.getCog(), 0.1);
        assertEquals(original.getHeading(), decoded.getHeading());
        assertEquals(original.getTimestamp(), decoded.getTimestamp());
        assertEquals(original.getSpecialManoeuvre(), decoded.getSpecialManoeuvre());
        assertEquals(original.getRaim(), decoded.getRaim());
        assertEquals(original.getRadio(), decoded.getRadio());
    }

    @Test
    public void testEncodeDecodeStaticMessage() {
        AisStaticMessage original = new AisStaticMessage();
        original.setMessageType(5);
        original.setRepeat(0);
        original.setMmsi(123456789);
        original.setAisVersion(0);
        original.setImo(9876543);
        original.setCallsign("CALL123");
        original.setName("TEST SHIP");
        original.setShipType(70);
        original.setDimensionToBow(10);
        original.setDimensionToStern(20);
        original.setDimensionToPort(5);
        original.setDimensionToStarboard(7);
        original.setEpfd(1);
        original.setEtaMonth(12);
        original.setEtaDay(31);
        original.setEtaHour(23);
        original.setEtaMinute(59);
        original.setDraught(6.5);
        original.setDestination("PORT OF CALL");
        original.setDteAvailable(true);
        original.setChannel("A");

        // Encode
        List<String> sentences = AisEncoder.encodeStaticMessage(original);
        assertNotNull(sentences);
        assertFalse(sentences.isEmpty());

        System.out.println("Encoded Static Sentences:");
        for (String s : sentences) {
            System.out.println(s);
        }

        AisDecoder decoder = new AisDecoder();
        AtomicReference<AisStaticMessage> decodedRef = new AtomicReference<>();

        decoder.setStaticCallback(msg -> {
            System.out.println("Static callback triggered with MMSI: " + msg.getMmsi());
            decodedRef.set(msg);
        });

        for (String s : sentences) {
            System.out.println("Feeding sentence to decoder: " + s);
            decoder.onSentence(s);
        }

        AisStaticMessage decoded = decodedRef.get();
        System.out.println("Decoded static message: " + decoded);

        assertNotNull(decoded, "Decoded static message should not be null");

        // Check fields (string trims done in decode)
        assertEquals(original.getMessageType(), decoded.getMessageType());
        assertEquals(original.getRepeat(), decoded.getRepeat());
        assertEquals(original.getMmsi(), decoded.getMmsi());
        assertEquals(original.getAisVersion(), decoded.getAisVersion());
        assertEquals(original.getImo(), decoded.getImo());
        assertEquals(original.getCallsign().trim(), decoded.getCallsign());
        assertEquals(original.getName().trim(), decoded.getName());
        assertEquals(original.getShipType(), decoded.getShipType());
        assertEquals(original.getDimensionToBow(), decoded.getDimensionToBow());
        assertEquals(original.getDimensionToStern(), decoded.getDimensionToStern());
        assertEquals(original.getDimensionToPort(), decoded.getDimensionToPort());
        assertEquals(original.getDimensionToStarboard(), decoded.getDimensionToStarboard());
        assertEquals(original.getEpfd(), decoded.getEpfd());
        assertEquals(original.getEtaMonth(), decoded.getEtaMonth());
        assertEquals(original.getEtaDay(), decoded.getEtaDay());
        assertEquals(original.getEtaHour(), decoded.getEtaHour());
        assertEquals(original.getEtaMinute(), decoded.getEtaMinute());
        assertEquals(original.getDraught(), decoded.getDraught(), 0.1);
        assertEquals(original.getDestination().trim(), decoded.getDestination());
        assertEquals(original.getDteAvailable(), decoded.getDteAvailable());
    }
}
