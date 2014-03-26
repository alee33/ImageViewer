package com.example.imageshow.db.orm;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Source page detail data
 * 
 * @author user
 * 
 */
@DatabaseTable(tableName = "detailes")
public class Detail {
    @DatabaseField(dataType = DataType.LONG)
    private long pageId;

    @DatabaseField(dataType = DataType.STRING)
    private String title;

    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean isGroup;

    public Boolean isGroup() {
        return isGroup;
    }

    public void itsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, foreign = true)
    private Source source;

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

}
