package com.example.imageshow.db.orm;

import java.sql.Time;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Application settings object 
 * @author user
 *
 */

@DatabaseTable
public class Settings {
    
    @DatabaseField(generatedId = true)
    private long id; //UID
    
    @DatabaseField( persisterClass = MyTimePersisterClass.class)
    private Time startupTime; //startup time
   
    @DatabaseField( persisterClass = MyTimePersisterClass.class)
    private Time shutdownTime; //shutdown time
   
    @DatabaseField(dataType = DataType.INTEGER)
    private int delay; //switch delay
    
    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean avtoChargeStart; //start on charge on
   
    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean avtoResetStart; //start on reboot
   
    @DatabaseField(dataType = DataType.STRING)
    private String name; //profile name
    
    @DatabaseField(dataType = DataType.INTEGER)
    private int transformation; //
    
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

    public boolean getAvtoChargeStart() {
        return avtoChargeStart;
    }

    public void setAvtoChargeStart(boolean avtoChargeStart) {
        this.avtoChargeStart = avtoChargeStart;
    }

    public boolean getAvtoResetStart() {
        return avtoResetStart;
    }

    public void setAvtoResetStart(boolean avtoResetStart) {
        this.avtoResetStart = avtoResetStart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTransformation() {
        return transformation;
    }

    public void setTransformation(int transformation) {
        this.transformation = transformation;
    }
}
