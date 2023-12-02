package com.example.universityscheduler.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.universityscheduler.Model.Terms;
import java.util.List;

/**
 * DAO Android Room Database interface class for the Terms model Object.
 */
@Dao
public interface TermsDao {

    //Dao insert method to add a term to the scheduler database.
    @Insert
    void insert(Terms terms);

    //Dao update method to overwrite/edit a term in the scheduler database.
    @Update
    void update(Terms terms);

    //Dao delete method to remove a term in the scheduler database.
    @Delete
    void delete(Terms terms);

    //SQL Select query to view all entries in the terms table where IDs match.
    @Query("SELECT * FROM terms_table WHERE id = :id")
    LiveData<List<Terms>> getTermById (int id);

    //SQL Select query to view all entries in the terms table.
    @Query("SELECT * FROM terms_table")
    LiveData<List<Terms>> getAllTerms();

}
