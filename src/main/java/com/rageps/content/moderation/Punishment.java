package com.rageps.content.moderation;


public class Punishment {

    private String offender;
    private String agent;
    private long expireDate;
    private long offenderSession;
    private String duration;
    private String mac;
    private String host;
    private PunishmentPolicy punishmentPolicy;
    private String reason;
    private PunishmentType punishmentType;
    private String uid;


    public Punishment(String player, String agent, long offenderSession, long expireDate, String duration, PunishmentPolicy punishmentPolicy, String reason, PunishmentType punishmentType, String mac, String host, String uid) {
        this.offender = player;
        this.agent = agent;
        this.offenderSession = offenderSession;
        this.expireDate = expireDate;
        this.duration = duration;
        this.punishmentPolicy = punishmentPolicy;
        this.reason = reason;
        this.punishmentType = punishmentType;
        this.mac = mac;
        this.uid = uid;
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public String getMac() {
        return mac;
    }

    public String getUid() {
        return uid;
    }

    public long getOffenderSession() {
        return offenderSession;
    }

    public String getOffender() {
        return offender;
    }

    public String getAgent() {
        return agent;
    }

    public long getExpireDate() {
        return expireDate;
    }

    public String getDuration() {
        return duration;
    }

    public PunishmentPolicy getPunishmentPolicy() {
        return punishmentPolicy;
    }

    public String getReason() {
        return reason;
    }

    public PunishmentType getPunishmentType() {
        return punishmentType;
    }


    public boolean hasExpired() {
        return getExpireDate() > 0 && getExpireDate() < System.currentTimeMillis();
    }

    public long getTimeLeft() {
        return getExpireDate() - System.currentTimeMillis();
    }


}
