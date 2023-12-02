package com.example.universityscheduler.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Model Class for the Course Assessments.
 */
@Entity(
        tableName = "assessments_table",
        foreignKeys = {
                @ForeignKey(entity = Courses.class, parentColumns = "id", childColumns = "courseId"),}, indices = {@Index("courseId")})
public class Assessments {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String assessmentTitle;
    private String assessmentStartDate;
    private String assessmentEndDate;
    private String assessmentType;
    private int courseId;



    //Constructor for the Assessments model object.
    public Assessments(String assessmentTitle, String assessmentStartDate, String assessmentEndDate, String assessmentType, int courseId) {
        this.assessmentTitle = assessmentTitle;
        this.assessmentStartDate = assessmentStartDate;
        this.assessmentEndDate = assessmentEndDate;
        this.assessmentType = assessmentType;
        this.courseId = courseId;
    }

    //Getter for the assessment ID.
    public int getId() {
        return id;
    }

    //Setter for the assessment ID.
    public void setId(int id) {
        this.id = id;
    }

    //Getter for the assessment title.
    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    //Getter for the assessment type.
    public String getAssessmentType() {
        return assessmentType;
    }

    //Getter for the assessment start date.
    public String getAssessmentStartDate() {
        return assessmentStartDate;
    }

    //Getter for the assessment end date.
    public String getAssessmentEndDate() {
        return assessmentEndDate;
    }

    //Getter for the course id.
    public int getCourseId() {
        return courseId;
    }
}
