package com.gitlab.felipecarrillo100;

/**
 * Represents AIS message type 5 (Static and Voyage Related Data).
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

    // Getters and setters for all fields

    public Integer getMessageType() { return messageType; }
    public void setMessageType(Integer messageType) { this.messageType = messageType; }

    public Integer getRepeat() { return repeat; }
    public void setRepeat(Integer repeat) { this.repeat = repeat; }

    public Integer getMmsi() { return mmsi; }
    public void setMmsi(Integer mmsi) { this.mmsi = mmsi; }

    public Integer getAisVersion() { return aisVersion; }
    public void setAisVersion(Integer aisVersion) { this.aisVersion = aisVersion; }

    public Integer getImo() { return imo; }
    public void setImo(Integer imo) { this.imo = imo; }

    public String getCallsign() { return callsign; }
    public void setCallsign(String callsign) { this.callsign = callsign; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getShipType() { return shipType; }
    public void setShipType(Integer shipType) { this.shipType = shipType; }

    public Integer getDimensionToBow() { return dimensionToBow; }
    public void setDimensionToBow(Integer dimensionToBow) { this.dimensionToBow = dimensionToBow; }

    public Integer getDimensionToStern() { return dimensionToStern; }
    public void setDimensionToStern(Integer dimensionToStern) { this.dimensionToStern = dimensionToStern; }

    public Integer getDimensionToPort() { return dimensionToPort; }
    public void setDimensionToPort(Integer dimensionToPort) { this.dimensionToPort = dimensionToPort; }

    public Integer getDimensionToStarboard() { return dimensionToStarboard; }
    public void setDimensionToStarboard(Integer dimensionToStarboard) { this.dimensionToStarboard = dimensionToStarboard; }

    public Integer getEpfd() { return epfd; }
    public void setEpfd(Integer epfd) { this.epfd = epfd; }

    public Integer getEtaMonth() { return etaMonth; }
    public void setEtaMonth(Integer etaMonth) { this.etaMonth = etaMonth; }

    public Integer getEtaDay() { return etaDay; }
    public void setEtaDay(Integer etaDay) { this.etaDay = etaDay; }

    public Integer getEtaHour() { return etaHour; }
    public void setEtaHour(Integer etaHour) { this.etaHour = etaHour; }

    public Integer getEtaMinute() { return etaMinute; }
    public void setEtaMinute(Integer etaMinute) { this.etaMinute = etaMinute; }

    public Double getDraught() { return draught; }
    public void setDraught(Double draught) { this.draught = draught; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Boolean getDteAvailable() { return dteAvailable; }
    public void setDteAvailable(Boolean dteAvailable) { this.dteAvailable = dteAvailable; }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        if (channel != null && channel.length() != 1) {
            throw new IllegalArgumentException("Channel must be a single character (e.g., 'A' or 'B')");
        }
        this.channel = channel;
    }
}
