package com.example.imageshow.db.orm;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Vk source object
 * 
 * @author user
 * 
 */
@DatabaseTable(tableName = "sources")
public class Source {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(dataType = DataType.STRING)
    private String text;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Detail> details;

    public ForeignCollection<Detail> getDetails() {
        return details;
    }

    public void setDetails(ForeignCollection<Detail> details) {
        this.details = details;
    }

    public Source() {

    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
