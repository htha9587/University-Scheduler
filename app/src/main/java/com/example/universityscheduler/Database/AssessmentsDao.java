package com.example.universityscheduler.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.universityscheduler.Model.Assessments;
import java.util.List;

/**
 * DAO Android Room Database interface class for the Assessment model Object.
 */
@Dao
public interface AssessmentsDao {

    //Dao insert method to add an assessment to the scheduler database.
    @Insert
    void insert(Assessments assessments);

    //Dao update method to overwrite/edit an assessment in the scheduler database.
    @Update
    void update(Assessments assessments);

    //Dao delete method to remove an assessment in the scheduler database.
    @Delete
    void delete(Assessments assessments);

    //SQL Select query to view all entries in the assessments table.
    @Query(("SELECT * FROM assessments_table"))
    LiveData<List<Assessments>> getAllAssessments();
    
}
