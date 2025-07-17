package com.gitlab.felipecarrillo100;

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

    // Getters and setters for all fields

    public Integer getMessageType() { return messageType; }
    public void setMessageType(Integer messageType) { this.messageType = messageType; }

    public Integer getRepeat() { return repeat; }
    public void setRepeat(Integer repeat) { this.repeat = repeat; }

    public Integer getMmsi() { return mmsi; }
    public void setMmsi(Integer mmsi) { this.mmsi = mmsi; }

    public Integer getNavStatus() { return navStatus; }
    public void setNavStatus(Integer navStatus) { this.navStatus = navStatus; }

    public Integer getRateOfTurn() { return rateOfTurn; }
    public void setRateOfTurn(Integer rateOfTurn) { this.rateOfTurn = rateOfTurn; }

    public Double getSog() { return sog; }
    public void setSog(Double sog) { this.sog = sog; }

    public Boolean getAccuracy() { return accuracy; }
    public void setAccuracy(Boolean accuracy) { this.accuracy = accuracy; }

    public Double getLon() { return lon; }
    public void setLon(Double lon) { this.lon = lon; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getCog() { return cog; }
    public void setCog(Double cog) { this.cog = cog; }

    public Integer getHeading() { return heading; }
    public void setHeading(Integer heading) { this.heading = heading; }

    public Integer getTimestamp() { return timestamp; }
    public void setTimestamp(Integer timestamp) { this.timestamp = timestamp; }

    public Integer getSpecialManoeuvre() { return specialManoeuvre; }
    public void setSpecialManoeuvre(Integer specialManoeuvre) { this.specialManoeuvre = specialManoeuvre; }

    public Boolean getRaim() { return raim; }
    public void setRaim(Boolean raim) { this.raim = raim; }

    public Integer getRadio() { return radio; }
    public void setRadio(Integer radio) { this.radio = radio; }

    /**
     * Gets the VHF channel used to send this message (e.g., "A" or "B").
     * According to ITU-R M.1371, valid values are typically single characters: "A", "B", or null.
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the channel ("A" or "B"). Must be one character or null.
     */
    public void setChannel(String channel) {
        if (channel != null && channel.length() != 1) {
            throw new IllegalArgumentException("AIS channel must be a single character: 'A', 'B', or null");
        }
        this.channel = channel;
    }

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
