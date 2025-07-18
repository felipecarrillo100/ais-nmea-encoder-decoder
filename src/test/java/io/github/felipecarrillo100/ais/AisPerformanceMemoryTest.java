package io.github.felipecarrillo100.ais;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AisPerformanceMemoryTest {

    private final AisEncoder encoder = new AisEncoder();
    private final AisDecoder decoder = new AisDecoder();

    @Test
    public void testPerformanceAndMemoryLeakMultipart() throws InterruptedException {
        // Prepare a static message with long name and destination to generate multipart
        AisStaticMessage msg = new AisStaticMessage();
        msg.setMmsi(123456789);
        msg.setImo(987654321);
        msg.setCallsign("CALL123");
        msg.setName("LONGSHIPNAMEEXCEEDIN"); // long name forces multipart
        msg.setShipType(70);
        msg.setDimensionToBow(50);
        msg.setDimensionToStern(50);
        msg.setDimensionToPort(15);
        msg.setDimensionToStarboard(15);
        msg.setEpfd(1);
        msg.setEtaMonth(12);
        msg.setEtaDay(31);
        msg.setEtaHour(23);
        msg.setEtaMinute(59);
        msg.setDraught(7.5);
        msg.setDestination("DESTINATION20CHARS!!"); // long destination forces multipart
        msg.setDteAvailable(true);
        msg.setRepeat(0);
        msg.setAisVersion(0);
        msg.setChannel("A");

        // Encode to multipart sentences using your encoder
        List<String> multipartSentences = encoder.encodeStaticMessage(msg);
        assertTrue(multipartSentences.size() > 1, "Test requires multipart sentences");

        AtomicInteger decodedCount = new AtomicInteger(0);

        decoder.setStaticCallback(decodedMsg -> {
            assertEquals(msg.getMmsi(), decodedMsg.getMmsi());
            assertEquals(msg.getName(), decodedMsg.getName());
            assertEquals(msg.getDestination(), decodedMsg.getDestination());
            decodedCount.incrementAndGet();
        });

        // Force GC and get baseline memory usage
        System.gc();
        Thread.sleep(100);
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long start = System.currentTimeMillis();

        final int iterations = 5000;

        for (int i = 0; i < iterations; i++) {
            for (String sentence : multipartSentences) {
                decoder.onSentence(sentence);
            }
        }

        long end = System.currentTimeMillis();

        System.gc();
        Thread.sleep(100);
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long memoryDiff = (afterUsedMem - beforeUsedMem) / (1024 * 1024); // MB
        long durationMs = end - start;

        System.out.println("Decoded messages: " + decodedCount.get());
        System.out.println("Elapsed time: " + durationMs + " ms");
        System.out.println("Memory diff: " + memoryDiff + " MB");

        // Every iteration should decode one message fully
        assertEquals(iterations, decodedCount.get());

        // Assert memory usage increase is reasonable (e.g. less than 20 MB)
        assertTrue(memoryDiff < 20, "Memory usage increased too much - possible leak");

        // Check multipartBuffers map is empty (reflection or accessor may be needed if private)
        try {
            var field = AisDecoder.class.getDeclaredField("multipartBuffers");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            var buffers = (java.util.Map<?, ?>) field.get(decoder);
            assertTrue(buffers.isEmpty(), "Multipart buffers not cleaned up");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Cannot access multipartBuffers for cleanup verification");
        }
    }
}
