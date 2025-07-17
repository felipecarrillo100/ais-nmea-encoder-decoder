package com.gitlab.felipecarrillo100;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AisEncoderStaticMessageTest {
    // Helper: validate AIS sentence format and checksum
    private boolean isValidAisSentence(String sentence) {
        if (sentence == null || !(sentence.startsWith("!AIVDM") || sentence.startsWith("!AIVDO"))) {
            return false;
        }
        int asteriskIndex = sentence.indexOf('*');
        if (asteriskIndex < 0 || asteriskIndex + 3 > sentence.length()) {
            return false;
        }
        String checksumStr = sentence.substring(asteriskIndex + 1, asteriskIndex + 3);
        int expectedChecksum;
        try {
            expectedChecksum = Integer.parseInt(checksumStr, 16);
        } catch (NumberFormatException e) {
            return false;
        }
        int computedChecksum = 0;
        for (int i = 1; i < asteriskIndex; i++) {
            computedChecksum ^= sentence.charAt(i);
        }
        return computedChecksum == expectedChecksum;
    }

    @Test
    public void testEncodeStaticMessage_Valid() {
        AisStaticMessage staticMsg = new AisStaticMessage();
        staticMsg.setMessageType(5);
        staticMsg.setRepeat(0);
        staticMsg.setMmsi(123456789);
        staticMsg.setAisVersion(0);
        staticMsg.setImo(9876543);
        staticMsg.setCallsign("CALL123");
        staticMsg.setName("TEST SHIP");
        staticMsg.setShipType(70);
        staticMsg.setDimensionToBow(10);
        staticMsg.setDimensionToStern(20);
        staticMsg.setDimensionToPort(5);
        staticMsg.setDimensionToStarboard(7);
        staticMsg.setEpfd(1);
        staticMsg.setEtaMonth(12);
        staticMsg.setEtaDay(31);
        staticMsg.setEtaHour(23);
        staticMsg.setEtaMinute(59);
        staticMsg.setDraught(6.5);
        staticMsg.setDestination("PORT OF CALL");
        staticMsg.setDteAvailable(true);
        staticMsg.setChannel("A");

        List<String> sentences = AisEncoder.encodeStaticMessage(staticMsg);
        assertNotNull(sentences);
        assertFalse(sentences.isEmpty());

        for (String sentence : sentences) {
            assertTrue(isValidAisSentence(sentence), "Invalid AIS sentence: " + sentence);
        }
    }
}
