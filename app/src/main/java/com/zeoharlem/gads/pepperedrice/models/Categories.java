package com.zeoharlem.gads.pepperedrice.models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories_table")
public class Categories {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "category_title")
    private String categoryTitle;

    public Categories(@NonNull String categoryTitle) {
        this.categoryTitle  = categoryTitle;
    }

    @NonNull
    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(@NonNull String categoryTitle) {
        this.categoryTitle  = categoryTitle;
    }
}
