package com.example.universityscheduler.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Model Class for the courses in a term.
 */
@Entity(
        tableName = "courses_table",
        foreignKeys = {
            @ForeignKey(entity = Terms.class, parentColumns = "id", childColumns = "termId")
        },
        indices = {@Index("termId")})

public class Courses {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int termId;

    String courseTitle;
    String courseStartDate;
    String courseEndDate;
    String coursesStatus;
    String instructorName;
    String instructorPhone;
    String instructorEmail;
    String courseNotes;

    //Constructor for the Courses model object.
    public Courses(int termId, String courseTitle, String courseStartDate, String courseEndDate, String coursesStatus,
                   String instructorName, String instructorPhone, String instructorEmail, String courseNotes) {
        this.termId = termId;
        this.courseTitle = courseTitle;
        this.courseStartDate = courseStartDate;
        this.courseEndDate = courseEndDate;
        this.coursesStatus = coursesStatus;
        this.instructorName = instructorName;
        this.instructorPhone = instructorPhone;
        this.instructorEmail = instructorEmail;
        this.courseNotes = courseNotes;
    }

    //Sets the course title to a string type.
    @NonNull
    @Override
    public String toString() {
        return this.courseTitle;
    }

    //Getter for the Course ID.
    public int getId() {
        return id;
    }

    //Setter for the Course ID.
    public void setId(int id) {
        this.id = id;
    }

    //Getter for the term ID.
    public int getTermId() {
        return termId;
    }

    //Getter for the course title.
    public String getCourseTitle() {
        return courseTitle;
    }

    //Getter for the course start date.
    public String getCourseStartDate() {
        return courseStartDate;
    }

    //Getter for the course end date.
    public String getCourseEndDate() {
        return courseEndDate;
    }

    //Getter for the course status.
    public String getCoursesStatus() {
        return coursesStatus;
    }

    //Getter for the course instructor name.
    public String getInstructorName() {
        return instructorName;
    }

    //Getter for the course instructor phone.
    public String getInstructorPhone() {
        return instructorPhone;
    }

    //Getter for the course instructor email.
    public String getInstructorEmail() {
        return instructorEmail;
    }

    //Getter for the course notes.
    public String getCourseNotes() {
        return courseNotes;
    }

}
