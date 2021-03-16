package com.zeoharlem.gads.pepperedrice.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "menus")
public class Menus {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "menu_title")
    private String menuTitle;

    public Menus(@NonNull String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(@NonNull String menuTitle) {
        this.menuTitle = menuTitle;
    }
}
