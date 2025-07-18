package io.github.felipecarrillo100.ais;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AIS Decoder that processes AIS NMEA sentences,
 * supports multipart messages, and decodes AIS types 1-3 and 5.
 * Uses callbacks to deliver decoded AIS position and static messages.
 */
public class AisDecoder {

    /**
     * Timeout in milliseconds after which incomplete multipart buffers are discarded.
     */
    private static final long MULTIPART_TIMEOUT_MS = 30000;

    /**
     * Map storing multipart message buffers keyed by sequence ID.
     */
    private final Map<String, MultipartBufferEntry> multipartBuffers = new HashMap<>();

    /**
     * Callback consumer invoked on decoded AIS position messages (types 1-3).
     */
    private Consumer<AisPositionMessage> positionCallback;

    /**
     * Callback consumer invoked on decoded AIS static messages (type 5).
     */
    private Consumer<AisStaticMessage> staticCallback;

    /**
     * Sets the callback to receive decoded AIS position messages.
     * @param callback Consumer that accepts AisPositionMessage
     */
    public void setPositionCallback(Consumer<AisPositionMessage> callback) {
        this.positionCallback = callback;
    }

    /**
     * Sets the callback to receive decoded AIS static messages.
     * @param callback Consumer that accepts AisStaticMessage
     */
    public void setStaticCallback(Consumer<AisStaticMessage> callback) {
        this.staticCallback = callback;
    }

    /**
     * Processes a single AIS NMEA sentence.
     * Handles single and multipart messages, verifies checksum, decodes bits,
     * and calls appropriate callbacks.
     * @param sentence AIS NMEA sentence string
     */
    public void onSentence(String sentence) {
        if (sentence == null) return;

        Pattern pattern = Pattern.compile("^!(AIVDM|AIVDO),(\\d+),(\\d+),([^,]*),([AB]),([^,]*),(\\d+)\\*([0-9A-Fa-f]{2})");
        Matcher matcher = pattern.matcher(sentence.trim());
        if (!matcher.find()) return;

        if (!verifyChecksum(sentence)) return;

        int total = Integer.parseInt(matcher.group(2));
        int part = Integer.parseInt(matcher.group(3));
        String seqId = matcher.group(4);
        String channel = matcher.group(5);
        String payload = matcher.group(6);
        int fillBits = Integer.parseInt(matcher.group(7));

        String key = (seqId == null || seqId.isEmpty()) ? "noprefix" : seqId;

        if (total == 1) {
            String bits = payloadToBits(payload, fillBits == 6 ? 0 : fillBits);
            processBits(bits, channel);
            return;
        }

        MultipartBufferEntry entry = multipartBuffers.get(key);
        if (entry == null) {
            entry = new MultipartBufferEntry(total);
            multipartBuffers.put(key, entry);

            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    multipartBuffers.remove(key);
                }
            }, MULTIPART_TIMEOUT_MS);
            entry.timer = timer;
        }

        entry.receivedParts.put(part, payload);

        if (part == total) {
            entry.fillBits = fillBits;
        }

        if (entry.receivedParts.size() == total) {
            StringBuilder fullPayload = new StringBuilder();
            for (int i = 1; i <= total; i++) {
                String p = entry.receivedParts.get(i);
                if (p == null) return;
                fullPayload.append(p);
            }
            multipartBuffers.remove(key);
            if (entry.timer != null) {
                entry.timer.cancel();
            }
            int effectiveFillBits = (entry.fillBits == 6) ? 0 : entry.fillBits;
            String bits = payloadToBits(fullPayload.toString(), effectiveFillBits);
            processBits(bits, channel);
        }
    }

    /**
     * Internal buffer structure to hold multipart message pieces.
     */
    private static class MultipartBufferEntry {
        int total;
        Map<Integer, String> receivedParts = new HashMap<>();
        int fillBits;
        Timer timer;

        MultipartBufferEntry(int total) {
            this.total = total;
        }
    }

    /**
     * Verifies the checksum of an AIS NMEA sentence.
     * @param sentence AIS sentence string
     * @return true if checksum matches, false otherwise
     */
    private boolean verifyChecksum(String sentence) {
        int starIndex = sentence.indexOf('*');
        if (starIndex == -1 || starIndex + 3 > sentence.length()) return false;
        String toCheck = sentence.substring(1, starIndex);
        int checksum = 0;
        for (char c : toCheck.toCharArray()) {
            checksum ^= c;
        }
        String expected = sentence.substring(starIndex + 1, starIndex + 3).toUpperCase();
        String calculated = String.format("%02X", checksum);
        return calculated.equals(expected);
    }

    /**
     * Converts AIS 6-bit ASCII payload to a bit string,
     * removing specified number of fill bits at the end.
     * @param payload AIS 6-bit ASCII encoded payload
     * @param fillBits number of fill bits to remove from the end
     * @return bit string representation of the payload
     */
    private String payloadToBits(String payload, int fillBits) {
        StringBuilder bits = new StringBuilder();
        for (char c : payload.toCharArray()) {
            int val = c - 48;
            if (val > 40) val -= 8;
            bits.append(String.format("%6s", Integer.toBinaryString(val)).replace(' ', '0'));
        }
        if (fillBits > 0 && bits.length() >= fillBits) {
            return bits.substring(0, bits.length() - fillBits);
        }
        return bits.toString();
    }

    /**
     * Processes decoded bits string by determining message type,
     * decoding into the appropriate message object,
     * and invoking the registered callbacks.
     * @param bits decoded AIS bit string
     * @param channel NMEA channel ("A" or "B")
     */
    private void processBits(String bits, String channel) {
        if (bits.length() < 40) return;

        int type = readUInt(bits, 0, 6);
        int mmsi = readUInt(bits, 8, 30);

        if (type == 5) {
            AisStaticMessage msg = decodeType5(bits, mmsi, channel);
            if (msg != null && staticCallback != null) staticCallback.accept(msg);
        } else if (type >= 1 && type <= 3) {
            AisPositionMessage msg = decodePosition(bits, type, mmsi, channel);
            if (msg != null && positionCallback != null) positionCallback.accept(msg);
        }
    }

    /**
     * Decodes AIS position message (types 1-3) from bit string.
     * @param bits AIS bit string
     * @param type AIS message type (1-3)
     * @param mmsi MMSI number
     * @param channel NMEA channel
     * @return decoded AisPositionMessage or null if bits insufficient
     */
    private AisPositionMessage decodePosition(String bits, int type, int mmsi, String channel) {
        if (bits.length() < 168) return null;
        AisPositionMessage msg = new AisPositionMessage();

        msg.setMessageType(type);
        msg.setChannel(channel);
        msg.setRepeat(readUInt(bits, 6, 2));
        msg.setMmsi(mmsi);
        msg.setNavStatus(readUInt(bits, 38, 4));
        msg.setRateOfTurn(readInt(bits, 42, 8));
        msg.setSog(readUInt(bits, 50, 10) / 10.0);
        msg.setAccuracy(readUInt(bits, 60, 1) == 1);
        msg.setLon(readInt(bits, 61, 28) / 600000.0);
        msg.setLat(readInt(bits, 89, 27) / 600000.0);
        msg.setCog(readUInt(bits, 116, 12) / 10.0);
        msg.setHeading(readUInt(bits, 128, 9));
        msg.setTimestamp(readUInt(bits, 137, 6));
        msg.setSpecialManoeuvre(readUInt(bits, 143, 2));
        msg.setRaim(readUInt(bits, 145, 1) == 1);
        msg.setRadio(readUInt(bits, 146, 19));

        return msg;
    }

    /**
     * Decodes AIS static message (type 5) from bit string.
     * @param bits AIS bit string
     * @param mmsi MMSI number
     * @param channel NMEA channel
     * @return decoded AisStaticMessage or null if bits insufficient
     */
    private AisStaticMessage decodeType5(String bits, int mmsi, String channel) {
        if (bits.length() < 424) return null;
        AisStaticMessage msg = new AisStaticMessage();

        msg.setMessageType(5);
        msg.setMmsi(mmsi);
        msg.setRepeat(readUInt(bits, 6, 2));
        msg.setAisVersion(readUInt(bits, 38, 2));
        msg.setImo(readUInt(bits, 40, 30));
        msg.setCallsign(decodeText(bits, 70, 7));
        msg.setName(decodeText(bits, 112, 20));
        msg.setShipType(readUInt(bits, 232, 8));
        msg.setDimensionToBow(readUInt(bits, 240, 9));
        msg.setDimensionToStern(readUInt(bits, 249, 9));
        msg.setDimensionToPort(readUInt(bits, 258, 6));
        msg.setDimensionToStarboard(readUInt(bits, 264, 6));
        // epfd is set after ship type per original POJO order
        msg.setEpfd(readUInt(bits, 230, 4));
        msg.setEtaMonth(readUInt(bits, 274, 4));
        msg.setEtaDay(readUInt(bits, 278, 5));
        msg.setEtaHour(readUInt(bits, 283, 5));
        msg.setEtaMinute(readUInt(bits, 288, 6));
        msg.setDraught(readUInt(bits, 294, 8) / 10.0);
        msg.setDestination(decodeText(bits, 302, 20));
        msg.setDteAvailable(readUInt(bits, 422, 1) == 0);
        msg.setChannel(channel);

        return msg;
    }

    /**
     * Reads an unsigned integer from bits substring.
     * @param bits bit string
     * @param start start index (inclusive)
     * @param length number of bits
     * @return unsigned integer value
     */
    private int readUInt(String bits, int start, int length) {
        if (start + length > bits.length()) return 0;
        return Integer.parseInt(bits.substring(start, start + length), 2);
    }

    /**
     * Reads a signed integer from bits substring using two's complement.
     * @param bits bit string
     * @param start start index (inclusive)
     * @param length number of bits
     * @return signed integer value
     */
    private int readInt(String bits, int start, int length) {
        if (start + length > bits.length()) return 0;
        String segment = bits.substring(start, start + length);
        if (segment.charAt(0) == '0') {
            return Integer.parseInt(segment, 2);
        } else {
            StringBuilder inverted = new StringBuilder();
            for (char c : segment.toCharArray()) {
                inverted.append(c == '0' ? '1' : '0');
            }
            int val = Integer.parseInt(inverted.toString(), 2) + 1;
            return -val;
        }
    }

    /**
     * Decodes 6-bit ASCII encoded text from bits.
     * Uses the AIS character table.
     * @param bits bit string
     * @param start start index (inclusive)
     * @param lengthChars number of characters to decode
     * @return decoded text string with trailing '@' characters removed and trimmed
     */
    private String decodeText(String bits, int start, int lengthChars) {
        final String table = "@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_ !\"#$%&'()*+,-./0123456789:;<=>?";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lengthChars; i++) {
            int bitIndex = start + i * 6;
            if (bitIndex + 6 > bits.length()) break;
            int val = Integer.parseInt(bits.substring(bitIndex, bitIndex + 6), 2);
            sb.append(val >= 0 && val < table.length() ? table.charAt(val) : ' ');
        }
        return sb.toString().replaceAll("@+$", "").trim();
    }
}
