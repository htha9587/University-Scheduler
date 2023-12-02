package com.example.universityscheduler.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Model Class for the University Term.
 */
@Entity(tableName = "terms_table")
public class Terms {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String termTitle;
    private String termStartDate;
    private String termEndDate;

    //Constructor for the Terms model object.
    public Terms(String termTitle, String termStartDate, String termEndDate) {
        this.termTitle = termTitle;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
    }

    //Sets the term title to a string type.
    @NonNull
    @Override
    public String toString() {
        return this.termTitle;
    }

    //Getter for the term ID.
    public int getId() {
        return id;
    }

    //Setter for the term ID.
    public void setId(int id) {
        this.id = id;
    }

    //Getter for the term title.
    public String getTermTitle() {
        return termTitle;
    }

    //Getter for the term start date.
    public String getTermStartDate() {
        return termStartDate;
    }

    //Getter for the term end date.
    public String getTermEndDate() {
        return termEndDate;
    }

    //Looks and makes sure the term id isn't null.
    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) return true;
        assert object != null;
        if (((Terms) object).getId() == this.id) return true;
        return false;
    }
}
