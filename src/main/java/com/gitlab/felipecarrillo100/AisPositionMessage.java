package com.gitlab.felipecarrillo100;

/**
 * Represents an AIS Position Message (Type 1, 2, or 3).
 * Contains dynamic navigation and positional information of a vessel.
 */
public class AisPositionMessage {
    private Integer messageType;
    private Integer repeat;
    private Integer mmsi;
    private Integer navStatus;
    private Integer rateOfTurn;
    private Double sog;
    private Boolean accuracy;
    private Double lon;
    private Double lat;
    private Double cog;
    private Integer heading;
    private Integer timestamp;
    private Integer specialManoeuvre;
    private Boolean raim;
    private Integer radio;
    private String channel;

    /**
     * Gets the AIS message type number.
     * Typically 1, 2, or 3 for position reports.
     * @return message type as Integer
     */
    public Integer getMessageType() { return messageType; }

    /**
     * Sets the AIS message type number.
     * @param messageType message type as Integer
     */
    public void setMessageType(Integer messageType) { this.messageType = messageType; }

    /**
     * Gets the repeat indicator (0-3).
     * Indicates how many times message has been repeated.
     * @return repeat indicator
     */
    public Integer getRepeat() { return repeat; }

    /**
     * Sets the repeat indicator (0-3).
     * @param repeat repeat indicator
     */
    public void setRepeat(Integer repeat) { this.repeat = repeat; }

    /**
     * Gets the MMSI (Maritime Mobile Service Identity) number.
     * Unique vessel identifier.
     * @return MMSI number
     */
    public Integer getMmsi() { return mmsi; }

    /**
     * Sets the MMSI (Maritime Mobile Service Identity) number.
     * @param mmsi MMSI number
     */
    public void setMmsi(Integer mmsi) { this.mmsi = mmsi; }

    /**
     * Gets navigation status (0-15).
     * Status values such as underway, at anchor, etc.
     * @return navigation status
     */
    public Integer getNavStatus() { return navStatus; }

    /**
     * Sets navigation status (0-15).
     * @param navStatus navigation status
     */
    public void setNavStatus(Integer navStatus) { this.navStatus = navStatus; }

    /**
     * Gets rate of turn (ROT) in AIS encoding (-128 to 127).
     * Value represents rate of turn in degrees per minute.
     * @return rate of turn
     */
    public Integer getRateOfTurn() { return rateOfTurn; }

    /**
     * Sets rate of turn (ROT).
     * @param rateOfTurn rate of turn
     */
    public void setRateOfTurn(Integer rateOfTurn) { this.rateOfTurn = rateOfTurn; }

    /**
     * Gets speed over ground (SOG) in knots.
     * May be null if not available.
     * @return speed over ground
     */
    public Double getSog() { return sog; }

    /**
     * Sets speed over ground (SOG) in knots.
     * @param sog speed over ground
     */
    public void setSog(Double sog) { this.sog = sog; }

    /**
     * Gets position accuracy flag.
     * True if accuracy is high, false otherwise.
     * @return position accuracy
     */
    public Boolean getAccuracy() { return accuracy; }

    /**
     * Sets position accuracy flag.
     * @param accuracy position accuracy
     */
    public void setAccuracy(Boolean accuracy) { this.accuracy = accuracy; }

    /**
     * Gets longitude in decimal degrees.
     * May be null if not available.
     * @return longitude
     */
    public Double getLon() { return lon; }

    /**
     * Sets longitude in decimal degrees.
     * @param lon longitude
     */
    public void setLon(Double lon) { this.lon = lon; }

    /**
     * Gets latitude in decimal degrees.
     * May be null if not available.
     * @return latitude
     */
    public Double getLat() { return lat; }

    /**
     * Sets latitude in decimal degrees.
     * @param lat latitude
     */
    public void setLat(Double lat) { this.lat = lat; }

    /**
     * Gets course over ground (COG) in degrees.
     * May be null if not available.
     * @return course over ground
     */
    public Double getCog() { return cog; }

    /**
     * Sets course over ground (COG) in degrees.
     * @param cog course over ground
     */
    public void setCog(Double cog) { this.cog = cog; }

    /**
     * Gets true heading in degrees (0-359).
     * 511 means not available.
     * @return heading
     */
    public Integer getHeading() { return heading; }

    /**
     * Sets true heading in degrees.
     * @param heading heading
     */
    public void setHeading(Integer heading) { this.heading = heading; }

    /**
     * Gets UTC second timestamp (0-59).
     * 60 means not available.
     * @return timestamp seconds
     */
    public Integer getTimestamp() { return timestamp; }

    /**
     * Sets UTC second timestamp.
     * @param timestamp timestamp seconds
     */
    public void setTimestamp(Integer timestamp) { this.timestamp = timestamp; }

    /**
     * Gets special manoeuvre indicator (0-3).
     * @return special manoeuvre
     */
    public Integer getSpecialManoeuvre() { return specialManoeuvre; }

    /**
     * Sets special manoeuvre indicator.
     * @param specialManoeuvre special manoeuvre
     */
    public void setSpecialManoeuvre(Integer specialManoeuvre) { this.specialManoeuvre = specialManoeuvre; }

    /**
     * Gets RAIM flag indicating if receiver autonomous integrity monitoring is in use.
     * @return true if RAIM is in use, false otherwise
     */
    public Boolean getRaim() { return raim; }

    /**
     * Sets RAIM flag.
     * @param raim RAIM usage flag
     */
    public void setRaim(Boolean raim) { this.raim = raim; }

    /**
     * Gets radio status field.
     * @return radio status
     */
    public Integer getRadio() { return radio; }

    /**
     * Sets radio status field.
     * @param radio radio status
     */
    public void setRadio(Integer radio) { this.radio = radio; }

    /**
     * Gets the VHF channel used to send this message (e.g., "A" or "B").
     * According to ITU-R M.1371, valid values are typically single characters: "A", "B", or null.
     * @return AIS radio channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the channel ("A" or "B"). Must be one character or null.
     * @param channel AIS radio channel
     * @throws IllegalArgumentException if channel length is not 1 or null
     */
    public void setChannel(String channel) {
        if (channel != null && channel.length() != 1) {
            throw new IllegalArgumentException("AIS channel must be a single character: 'A', 'B', or null");
        }
        this.channel = channel;
    }

    /**
     * Returns a string representation of the AIS position message.
     * @return string representation
     */
    @Override
    public String toString() {
        return "AisPositionMessage{" +
                "messageType=" + messageType +
                ", repeat=" + repeat +
                ", mmsi=" + mmsi +
                ", navStatus=" + navStatus +
                ", rateOfTurn=" + rateOfTurn +
                ", sog=" + sog +
                ", accuracy=" + accuracy +
                ", lon=" + lon +
                ", lat=" + lat +
                ", cog=" + cog +
                ", heading=" + heading +
                ", timestamp=" + timestamp +
                ", specialManoeuvre=" + specialManoeuvre +
                ", raim=" + raim +
                ", radio=" + radio +
                ", channel='" + channel + '\'' +
                '}';
    }
}
