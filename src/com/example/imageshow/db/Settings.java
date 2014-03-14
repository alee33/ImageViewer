package com.example.imageshow.db;

import java.sql.Time;

/**
 * Application settings object 
 * @author user
 *
 */
public class Settings {
    private long id; //UID
    private Time startupTime; //startup time
    private Time shutdownTime; //shutdown time
    private int delay; //switch delay
    private Boolean avtoChargeStart; //start on charge on
    private Boolean avtoResetStart; //start on reboot
    private String name; //profile name

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Time getStartupDelay() {
        return startupTime;
    }

    public void setStartupDelay(Time startupDelay) {
        this.startupTime = startupDelay;
    }

    public Time getShutdownDelay() {
        return shutdownTime;
    }

    public void setShutdownDelay(Time shutdownDelay) {
        this.shutdownTime = shutdownDelay;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Boolean getAvtoChargeStart() {
        return avtoChargeStart;
    }

    public void setAvtoChargeStart(Boolean avtoChargeStart) {
        this.avtoChargeStart = avtoChargeStart;
    }

    public Boolean getAvtoResetStart() {
        return avtoResetStart;
    }

    public void setAvtoResetStart(Boolean avtoResetStart) {
        this.avtoResetStart = avtoResetStart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
