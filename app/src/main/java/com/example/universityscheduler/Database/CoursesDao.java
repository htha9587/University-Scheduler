package com.example.universityscheduler.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.universityscheduler.Model.Courses;
import java.util.List;

/**
 * DAO Android Room Database interface class for the Courses model Object.
 */
@Dao
public interface CoursesDao {

    //Dao insert method to add a course to the scheduler database.
    @Insert
    void insert(Courses courses);

    //Dao update method to overwrite/edit a course in the scheduler database.
    @Update
    void update(Courses courses);

    //Dao delete method to remove a course in the scheduler database.
    @Delete
    void delete(Courses courses);

    //SQL Select query to view all entries in the courses table.
    @Query(("SELECT * FROM courses_table"))
    LiveData<List<Courses>> getAllCourses();
}
