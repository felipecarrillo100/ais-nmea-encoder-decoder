package com.gitlab.felipecarrillo100;

/**
 * Represents AIS message type 5 (Static and Voyage Related Data).
 * Contains static vessel information and voyage details.
 */
public class AisStaticMessage {
    private Integer messageType;
    private Integer repeat;
    private Integer mmsi;
    private Integer aisVersion;
    private Integer imo;
    private String callsign;
    private String name;
    private Integer shipType;
    private Integer dimensionToBow;
    private Integer dimensionToStern;
    private Integer dimensionToPort;
    private Integer dimensionToStarboard;
    private Integer epfd;
    private Integer etaMonth;
    private Integer etaDay;
    private Integer etaHour;
    private Integer etaMinute;
    private Double draught;
    private String destination;
    private Boolean dteAvailable;

    /**
     * NMEA channel (A, B, etc.). Must be a single character or null.
     */
    private String channel;

    /**
     * Gets the AIS message type number.
     * Usually fixed to 5 for static data messages.
     * @return message type
     */
    public Integer getMessageType() { return messageType; }

    /**
     * Sets the AIS message type number.
     * @param messageType message type
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
     * Gets AIS version number.
     * Typically 0.
     * @return AIS version
     */
    public Integer getAisVersion() { return aisVersion; }

    /**
     * Sets AIS version number.
     * @param aisVersion AIS version
     */
    public void setAisVersion(Integer aisVersion) { this.aisVersion = aisVersion; }

    /**
     * Gets IMO number.
     * International Maritime Organization vessel identifier.
     * @return IMO number
     */
    public Integer getImo() { return imo; }

    /**
     * Sets IMO number.
     * @param imo IMO number
     */
    public void setImo(Integer imo) { this.imo = imo; }

    /**
     * Gets callsign string.
     * @return vessel callsign
     */
    public String getCallsign() { return callsign; }

    /**
     * Sets callsign string.
     * @param callsign vessel callsign
     */
    public void setCallsign(String callsign) { this.callsign = callsign; }

    /**
     * Gets vessel name.
     * @return vessel name
     */
    public String getName() { return name; }

    /**
     * Sets vessel name.
     * @param name vessel name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets ship type code.
     * @return ship type
     */
    public Integer getShipType() { return shipType; }

    /**
     * Sets ship type code.
     * @param shipType ship type
     */
    public void setShipType(Integer shipType) { this.shipType = shipType; }

    /**
     * Gets dimension from GPS antenna to bow in meters.
     * @return dimension to bow
     */
    public Integer getDimensionToBow() { return dimensionToBow; }

    /**
     * Sets dimension from GPS antenna to bow.
     * @param dimensionToBow dimension to bow
     */
    public void setDimensionToBow(Integer dimensionToBow) { this.dimensionToBow = dimensionToBow; }

    /**
     * Gets dimension from GPS antenna to stern in meters.
     * @return dimension to stern
     */
    public Integer getDimensionToStern() { return dimensionToStern; }

    /**
     * Sets dimension from GPS antenna to stern.
     * @param dimensionToStern dimension to stern
     */
    public void setDimensionToStern(Integer dimensionToStern) { this.dimensionToStern = dimensionToStern; }

    /**
     * Gets dimension from GPS antenna to port side in meters.
     * @return dimension to port
     */
    public Integer getDimensionToPort() { return dimensionToPort; }

    /**
     * Sets dimension from GPS antenna to port side.
     * @param dimensionToPort dimension to port
     */
    public void setDimensionToPort(Integer dimensionToPort) { this.dimensionToPort = dimensionToPort; }

    /**
     * Gets dimension from GPS antenna to starboard side in meters.
     * @return dimension to starboard
     */
    public Integer getDimensionToStarboard() { return dimensionToStarboard; }

    /**
     * Sets dimension from GPS antenna to starboard side.
     * @param dimensionToStarboard dimension to starboard
     */
    public void setDimensionToStarboard(Integer dimensionToStarboard) { this.dimensionToStarboard = dimensionToStarboard; }

    /**
     * Gets EPFD (Electronic Position Fixing Device) type.
     * @return EPFD type code
     */
    public Integer getEpfd() { return epfd; }

    /**
     * Sets EPFD type.
     * @param epfd EPFD type code
     */
    public void setEpfd(Integer epfd) { this.epfd = epfd; }

    /**
     * Gets ETA month (1-12).
     * @return ETA month
     */
    public Integer getEtaMonth() { return etaMonth; }

    /**
     * Sets ETA month (1-12).
     * @param etaMonth ETA month
     */
    public void setEtaMonth(Integer etaMonth) { this.etaMonth = etaMonth; }

    /**
     * Gets ETA day (1-31).
     * @return ETA day
     */
    public Integer getEtaDay() { return etaDay; }

    /**
     * Sets ETA day (1-31).
     * @param etaDay ETA day
     */
    public void setEtaDay(Integer etaDay) { this.etaDay = etaDay; }

    /**
     * Gets ETA hour (0-23).
     * @return ETA hour
     */
    public Integer getEtaHour() { return etaHour; }

    /**
     * Sets ETA hour (0-23).
     * @param etaHour ETA hour
     */
    public void setEtaHour(Integer etaHour) { this.etaHour = etaHour; }

    /**
     * Gets ETA minute (0-59).
     * @return ETA minute
     */
    public Integer getEtaMinute() { return etaMinute; }

    /**
     * Sets ETA minute (0-59).
     * @param etaMinute ETA minute
     */
    public void setEtaMinute(Integer etaMinute) { this.etaMinute = etaMinute; }

    /**
     * Gets draught in meters.
     * @return draught
     */
    public Double getDraught() { return draught; }

    /**
     * Sets draught in meters.
     * @param draught draught
     */
    public void setDraught(Double draught) { this.draught = draught; }

    /**
     * Gets destination string.
     * @return destination
     */
    public String getDestination() { return destination; }

    /**
     * Sets destination string.
     * @param destination destination
     */
    public void setDestination(String destination) { this.destination = destination; }

    /**
     * Gets DTE (Data Terminal Equipment) availability flag.
     * True if data terminal is available.
     * @return DTE availability
     */
    public Boolean getDteAvailable() { return dteAvailable; }

    /**
     * Sets DTE (Data Terminal Equipment) availability.
     * @param dteAvailable DTE availability
     */
    public void setDteAvailable(Boolean dteAvailable) { this.dteAvailable = dteAvailable; }

    /**
     * Gets the NMEA channel (e.g., "A" or "B").
     * @return NMEA channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the NMEA channel.
     * Must be a single character or null.
     * @param channel NMEA channel
     * @throws IllegalArgumentException if channel length is not 1 or null
     */
    public void setChannel(String channel) {
        if (channel != null && channel.length() != 1) {
            throw new IllegalArgumentException("Channel must be a single character (e.g., 'A' or 'B')");
        }
        this.channel = channel;
    }
}
