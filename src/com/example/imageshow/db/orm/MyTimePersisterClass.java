package com.example.imageshow.db.orm;

import java.sql.Time;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;

/**
 * Converting String- TIme
 * 
 * @author user
 * 
 */
public class MyTimePersisterClass extends com.j256.ormlite.field.types.LongType {

    private static final MyTimePersisterClass singleTon = new MyTimePersisterClass();

    protected MyTimePersisterClass() {

        super(SqlType.LONG, new Class<?>[] { Time.class });
    }

    public static MyTimePersisterClass getSingleton() {
        return singleTon;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        Time time = (Time) javaObject;
        if (time == null) {
            return null;
        } else {
            return time.getTime();
        }
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return new Time((Long) sqlArg);
    }

}
