package io.github.felipecarrillo100.ais;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * AIS Encoder in Java - encodes data to AIS sentences types 1-3 and 5.
 * supports multipart messages.
 */
public class AisEncoder {

    private static final Random RANDOM = new Random();

    // AIS 6-bit char codes map (char -> 6-bit int)
    private static final Map<Character, Integer> AIS_6BIT_CHAR_CODES = new HashMap<>();
    static {
        String chars = "@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_ !\"#$%&'()*+,-./0123456789:;<=>?";
        for (int i = 0; i < chars.length(); i++) {
            AIS_6BIT_CHAR_CODES.put(chars.charAt(i), i);
        }
    }

    /**
     * Write an unsigned integer as a bit string of length bits.
     * @param value unsigned integer to encode
     * @param bits number of bits for the output bit string
     * @return bit string representing the unsigned integer
     */
    private static String writeInt(int value, int bits) {
        StringBuilder sb = new StringBuilder(bits);
        for (int i = bits - 1; i >= 0; i--) {
            sb.append(((value >> i) & 1) == 1 ? '1' : '0');
        }
        return sb.toString();
    }

    /**
     * Write a signed integer as two's complement bit string of length bits.
     * @param value signed integer to encode
     * @param bits number of bits for the output bit string
     * @return bit string representing the signed integer
     * @throws IllegalArgumentException if bits < 1 or value out of range
     */
    private static String writeSignedInt(int value, int bits) {
        if (bits < 1) throw new IllegalArgumentException("Bits must be >= 1");
        int maxPos = (1 << (bits - 1)) - 1;
        int minNeg = -(1 << (bits - 1));
        if (value > maxPos || value < minNeg) {
            throw new IllegalArgumentException("Value " + value + " does not fit in " + bits + " bits");
        }
        if (value >= 0) {
            return writeInt(value, bits);
        } else {
            int twosComplement = (1 << bits) + value;
            return writeInt(twosComplement, bits);
        }
    }

    /**
     * Encode a string to AIS 6-bit bit string, padded/truncated to maxBits length.
     * @param str input string to encode
     * @param maxBits maximum number of bits in output bit string
     * @return AIS 6-bit encoded bit string
     */
    private static String encodeStringTo6Bit(String str, int maxBits) {
        int maxChars = maxBits / 6;
        StringBuilder bits = new StringBuilder(maxBits);
        for (int i = 0; i < maxChars; i++) {
            char c = i < str.length() ? str.charAt(i) : '@'; // '@' = 0
            Integer val = AIS_6BIT_CHAR_CODES.get(c);
            if (val == null) {
                val = 0; // treat unknown char as '@'
            }
            bits.append(writeInt(val, 6));
        }
        return bits.toString();
    }

    /**
     * Encode AIS Position Message (Type 1) into bit string.
     * @param msg AIS position message object
     * @return bit string representing encoded position message
     */
    public static String encodePositionMessageToBitString(AisPositionMessage msg) {
        int repeat = msg.getRepeat() != null ? msg.getRepeat() : 0;
        int navStatus = msg.getNavStatus() != null ? msg.getNavStatus() : 0;
        int rateOfTurn = msg.getRateOfTurn() != null ? msg.getRateOfTurn() : -128;
        int sog = msg.getSog() != null ? (int) Math.floor(msg.getSog() * 10) : 1023;
        int accuracy = (msg.getAccuracy() != null && msg.getAccuracy()) ? 1 : 0;
        int lon = msg.getLon() != null ? (int) Math.floor(msg.getLon() * 600000) : 0x6791AC0; // 181 * 600000 (invalid)
        int lat = msg.getLat() != null ? (int) Math.floor(msg.getLat() * 600000) : 0x3412140; // 91 * 600000 (invalid)
        int cog = msg.getCog() != null ? (int) Math.floor(msg.getCog() * 10) : 3600;
        int heading = msg.getHeading() != null ? msg.getHeading() : 511;
        int timestamp = msg.getTimestamp() != null ? msg.getTimestamp() : 60;
        int specialManoeuvre = msg.getSpecialManoeuvre() != null ? msg.getSpecialManoeuvre() : 0;
        int raim = (msg.getRaim() != null && msg.getRaim()) ? 1 : 0;
        int radio = msg.getRadio() != null ? msg.getRadio() : 0;

        StringBuilder bits = new StringBuilder(168);
        bits.append(writeInt(1, 6)); // message type
        bits.append(writeInt(repeat, 2));
        bits.append(writeInt(msg.getMmsi(), 30));
        bits.append(writeInt(navStatus, 4));
        bits.append(writeSignedInt(rateOfTurn, 8));
        bits.append(writeInt(sog, 10));
        bits.append(writeInt(accuracy, 1));
        bits.append(writeSignedInt(lon, 28));
        bits.append(writeSignedInt(lat, 27));
        bits.append(writeInt(cog, 12));
        bits.append(writeInt(heading, 9));
        bits.append(writeInt(timestamp, 6));
        bits.append(writeInt(specialManoeuvre, 2));
        bits.append(writeInt(raim, 1));
        bits.append(writeInt(radio, 19));

        return bits.toString();
    }

    /**
     * Encode AIS Static Message (Type 5) into bit string.
     * @param msg AIS static message object
     * @return bit string representing encoded static message
     */
    public static String encodeStaticMessageToBitString(AisStaticMessage msg) {
        int repeat = msg.getRepeat() != null ? msg.getRepeat() : 0;
        int aisVersion = msg.getAisVersion() != null ? msg.getAisVersion() : 0;
        int epfd = msg.getEpfd() != null ? msg.getEpfd() : 0;
        int dteAvailable = (msg.getDteAvailable() != null && !msg.getDteAvailable()) ? 1 : 0; // inverted per spec

        String callsign = msg.getCallsign() != null ? msg.getCallsign() : "";
        String name = msg.getName() != null ? msg.getName() : "";
        String destination = msg.getDestination() != null ? msg.getDestination() : "";

        int imo = msg.getImo() != null ? msg.getImo() : 0;
        int shipType = msg.getShipType() != null ? msg.getShipType() : 0;
        int dimToBow = msg.getDimensionToBow() != null ? msg.getDimensionToBow() : 0;
        int dimToStern = msg.getDimensionToStern() != null ? msg.getDimensionToStern() : 0;
        int dimToPort = msg.getDimensionToPort() != null ? msg.getDimensionToPort() : 0;
        int dimToStarboard = msg.getDimensionToStarboard() != null ? msg.getDimensionToStarboard() : 0;
        int etaMonth = msg.getEtaMonth() != null ? msg.getEtaMonth() : 0;
        int etaDay = msg.getEtaDay() != null ? msg.getEtaDay() : 0;
        int etaHour = msg.getEtaHour() != null ? msg.getEtaHour() : 24;    // 24 = not available
        int etaMinute = msg.getEtaMinute() != null ? msg.getEtaMinute() : 60; // 60 = not available
        double draughtVal = msg.getDraught() != null ? msg.getDraught() : 0.0;

        StringBuilder bits = new StringBuilder(424);
        bits.append(writeInt(5, 6));
        bits.append(writeInt(repeat, 2));
        bits.append(writeInt(msg.getMmsi(), 30));
        bits.append(writeInt(aisVersion, 2));
        bits.append(writeInt(imo, 30));
        bits.append(encodeStringTo6Bit(callsign, 42));
        bits.append(encodeStringTo6Bit(name, 120));
        bits.append(writeInt(shipType, 8));
        bits.append(writeInt(dimToBow, 9));
        bits.append(writeInt(dimToStern, 9));
        bits.append(writeInt(dimToPort, 6));
        bits.append(writeInt(dimToStarboard, 6));
        bits.append(writeInt(epfd, 4));
        bits.append(writeInt(etaMonth, 4));
        bits.append(writeInt(etaDay, 5));
        bits.append(writeInt(etaHour, 5));
        bits.append(writeInt(etaMinute, 6));
        bits.append(writeInt((int) Math.floor(draughtVal * 10), 8));
        bits.append(encodeStringTo6Bit(destination, 120));
        bits.append(writeInt(dteAvailable, 1));
        bits.append(writeInt(0, 1)); // spare

        return bits.toString();
    }

    /**
     * Encode position message into AIS NMEA sentences.
     * @param msg AIS position message to encode
     * @return list of AIS NMEA sentences encoding the position message
     */
    public static List<String> encodePositionMessage(AisPositionMessage msg) {
        String bits = encodePositionMessageToBitString(msg);
        char channel = 'A';
        if (msg.getChannel() != null && !msg.getChannel().isEmpty()) {
            channel = msg.getChannel().charAt(0);
        }
        return encodePayload(bits, 1, channel);
    }

    /**
     * Encode static message into AIS NMEA sentences.
     * @param msg AIS static message to encode
     * @return list of AIS NMEA sentences encoding the static message
     */
    public static List<String> encodeStaticMessage(AisStaticMessage msg) {
        String bits = encodeStaticMessageToBitString(msg);
        char channel = 'A';
        if (msg.getChannel() != null && !msg.getChannel().isEmpty()) {
            channel = msg.getChannel().charAt(0);
        }
        return encodePayload(bits, 5, channel);
    }

    /**
     * Encode a bit string payload into one or multiple AIS NMEA sentences.
     * @param bits bit string payload to encode
     * @param messageType AIS message type number
     * @param channel AIS radio channel (usually 'A' or 'B')
     * @return list of NMEA AIS sentences encoding the payload
     */
    private static List<String> encodePayload(String bits, int messageType, char channel) {
        bits = padBitsToMultipleOf6(bits);
        String payload = bitsTo6BitAscii(bits);

        List<String> sentences = new ArrayList<>();

        int maxPayloadLength = 60;
        int totalSentences = (int) Math.ceil((double) payload.length() / maxPayloadLength);
        int seqId = RANDOM.nextInt(9) + 1; // 1-9

        for (int i = 0; i < totalSentences; i++) {
            String part = payload.substring(i * maxPayloadLength, Math.min(payload.length(), (i + 1) * maxPayloadLength));
            int totalBits = part.length() * 6;
            int fillBits = (8 - (totalBits % 8)) % 8;

            String line = String.format("!AIVDM,%d,%d,%d,%c,%s,%d",
                    totalSentences, i + 1, seqId, channel, part, fillBits);
            String checksum = calculateChecksum(line);
            sentences.add(line + "*" + checksum);
        }

        return sentences;
    }

    /**
     * Pad bits with '0' to multiple of 6 length.
     * @param bits input bit string
     * @return bit string padded to multiple of 6 bits
     */
    private static String padBitsToMultipleOf6(String bits) {
        int remainder = bits.length() % 6;
        if (remainder == 0) return bits;
        int padLen = 6 - remainder;
        StringBuilder sb = new StringBuilder(bits);
        for (int i = 0; i < padLen; i++) sb.append('0');
        return sb.toString();
    }

    /**
     * Convert bit string to AIS 6-bit ASCII payload string.
     * @param bits input bit string
     * @return AIS 6-bit ASCII encoded string
     */
    private static String bitsTo6BitAscii(String bits) {
        StringBuilder sb = new StringBuilder(bits.length() / 6);
        for (int i = 0; i < bits.length(); i += 6) {
            String chunk = bits.substring(i, i + 6);
            int val = Integer.parseInt(chunk, 2);
            sb.append(sixBitToChar(val));
        }
        return sb.toString();
    }

    /**
     * Map 6-bit value to AIS 6-bit ASCII char per ITU-R M.1371.
     * @param val 6-bit integer value (0-63)
     * @return AIS 6-bit ASCII character
     * @throws IllegalArgumentException if val out of range
     */
    private static char sixBitToChar(int val) {
        if (val < 0 || val > 63) throw new IllegalArgumentException("6-bit value out of range: " + val);
        if (val < 40) return (char) (val + 48);
        else return (char) (val + 56);
    }

    /**
     * Calculate XOR checksum for NMEA sentence excluding leading '!' and trailing checksum.
     * @param sentence NMEA sentence without checksum part
     * @return two-hex-digit checksum string
     */
    private static String calculateChecksum(String sentence) {
        int checksum = 0;
        // start after '!' (index 1), stop before '*'
        for (int i = 1; i < sentence.length(); i++) {
            char c = sentence.charAt(i);
            if (c == '*') break;
            checksum ^= c;
        }
        return String.format("%02X", checksum);
    }
}
