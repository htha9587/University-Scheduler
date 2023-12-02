package com.example.universityscheduler.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.universityscheduler.Database.SchedulerRepository;
import com.example.universityscheduler.Model.Assessments;
import com.example.universityscheduler.Model.Courses;
import com.example.universityscheduler.Model.Terms;
import java.util.List;

/**
 * ViewModel class for the Scheduler. Links back to the scheduler repository which handles the CRUD asynchronous tasks for
 * the Terms, Courses, and Assessments.
 */
public class SchedulerVM extends AndroidViewModel {

    private SchedulerRepository schedulerRepository;
    private LiveData<List<Assessments>> allAssessments;
    private LiveData<List<Courses>> allCourses;
    private LiveData<List<Terms>> allTerms;

    /**
     * Constructor for the Scheduler ViewModel.
     * @param application = instance of application object.
     */
    public SchedulerVM(@NonNull Application application) {
        super(application);
        schedulerRepository = new SchedulerRepository(application);
        allAssessments = schedulerRepository.getAllAssessments();
        allCourses = schedulerRepository.getAllCourses();
        allTerms = schedulerRepository.getAllTerms();
    }

    /**
     * Dao insert method that inserts new entries into the Assessments database table/RecyclerView.
     * @param assessments = instance of the Assessments model object.
     */
    public void insert(Assessments assessments) {
        schedulerRepository.insert(assessments);
    }

    /**
     * Dao update method that replaces entries from the Assessments database table/RecyclerView.
     * @param assessments = instance of the Assessments model object.
     */
    public void update(Assessments assessments) {
        schedulerRepository.update(assessments);
    }

    /**
     * Dao delete method that replaces entries from the terms database table/RecyclerView.
     * @param assessments = instance of the Assessments model object.
     */
    public void delete(Assessments assessments) {
        schedulerRepository.delete(assessments);
    }

    /**
     * Dao insert method that inserts new entries into the Courses database table/RecyclerView.
     * @param courses = instance of the Courses model object.
     */
    public void insert(Courses courses) {
        schedulerRepository.insert(courses);
    }

    /**
     * Dao update method that replaces entries from the Courses database table/RecyclerView.
     * @param courses = instance of the Courses model object.
     */
    public void update(Courses courses) {
        schedulerRepository.update(courses);
    }

    /**
     * Dao delete method that removes entries from the Courses database table/RecyclerView.
     * @param courses = instance of the Courses model object.
     */
    public void delete(Courses courses) {
        schedulerRepository.delete(courses);
    }

    /**
     * Dao insert method that inserts new entries into the terms database table/RecyclerView.
     * @param terms = instance of the Terms model object.
     */
    public void insert(Terms terms) {
        schedulerRepository.insert(terms);
    }

    /**
     * Dao update method that replaces entries from the terms database table/RecyclerView.
     * @param terms = instance of the Terms model object.
     */
    public void update(Terms terms) {
        schedulerRepository.update(terms);
    }

    /**
     * Dao insert method that removes entries from the terms database table/RecyclerView.
     * @param terms = instance of the Terms model object.
     */
    public void delete(Terms terms) {
        schedulerRepository.delete(terms);
    }

    /**
     * Getter for all entries in the Assessments DB/RecyclerView.
     * @return allAssessments
     */
    public LiveData<List<Assessments>> getAllAssessments() { return allAssessments; }

    /**
     * Getter for all entries in the Courses DB/RecyclerView.
     * @return allCourses
     */
    public LiveData<List<Courses>> getAllCourses() {
        return allCourses;
    }

    /**
     * Getter for all entries in the Terms DB/RecyclerView.
     * @return allTerms
     */
    public LiveData<List<Terms>> getAllTerms() {
        return allTerms;
    }

    /**
     * Getter for Terms entries by checking their ID.
     * @param id= termID
     * @return = getTermById
     */
    public LiveData<List<Terms>> getTermById(int id) {
        return schedulerRepository.getTermById(id);
    }

}
