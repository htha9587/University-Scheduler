package com.example.universityscheduler.Database;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.example.universityscheduler.Model.Assessments;
import com.example.universityscheduler.Model.Courses;
import com.example.universityscheduler.Model.Terms;
import java.util.List;

/**
 * Repository class for the Scheduler Room MVVM database. In addition, this is the only other class
 * with access to the database.
 */
public class SchedulerRepository {

    private AssessmentsDao assessmentsDao;
    private CoursesDao coursesDao;
    private TermsDao termsDao;
    private LiveData<List<Assessments>> allAssessments;
    private LiveData<List<Courses>> allCourses;
    private LiveData<List<Terms>> allTerms;
    private LiveData<List<Terms>> termsByID;

    /**
     * Constructor for the Scheduler Repository.
     * @param application = instance of Application.
     */
    public SchedulerRepository(Application application) {
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getInstance(application);
        assessmentsDao = schedulerDatabase.assessmentsDao();
        coursesDao = schedulerDatabase.coursesDao();
        termsDao = schedulerDatabase.termsDao();
        allAssessments = assessmentsDao.getAllAssessments();
        allCourses = coursesDao.getAllCourses();
        allTerms = termsDao.getAllTerms();
    }

    /**
     *Calls the asynchronous task method which adds an assessment to the RecyclerView and database.
     * @param assessments = instance of Assessments model object.
     */
    public void insert(Assessments assessments) {
        new InsertAssessmentsAsyncTask(assessmentsDao).execute(assessments);
    }

    /**
     * Calls the asynchronous task method which overwrites an assessment in the RecyclerView and database.
     * @param assessments = instance of Assessments model object.
     */
    public void update(Assessments assessments) {
        new UpdateAssessmentsAsyncTask(assessmentsDao).execute(assessments);
    }

    /**
     * Calls the asynchronous task method which removes an assessment from the RecyclerView and database.
     * @param assessments = instance of Assessments model object.
     */
    public void delete(Assessments assessments) {
        new DeleteAssessmentsAsyncTask(assessmentsDao).execute(assessments);
    }

    /**
     * Calls the asynchronous task method which adds a course to the RecyclerView and database.
     * @param courses = instance of Courses model object.
     */
    public void insert(Courses courses) {
        new InsertCoursesAsyncTask(coursesDao).execute(courses);
    }

    /**
     * Calls the asynchronous task method which overwrites a course in the RecyclerView and database.
     * @param courses = instance of Courses model object.
     */
    public void update(Courses courses) {
        new UpdateCoursesAsyncTask(coursesDao).execute(courses);
    }

    /**
     * Calls the asynchronous task method which removes a course from the RecyclerView and database.
     * @param courses = instance of Courses model object.
     */
    public void delete(Courses courses) {
        new DeleteCoursesAsyncTask(coursesDao).execute(courses);
    }

    /**
     * Calls the asynchronous task method which adds a term to the RecyclerView and database.
     * @param terms = instance of Terms model object.
     */
    public void insert(Terms terms) {
        new InsertTermsAsyncTask(termsDao).execute(terms);
    }

    /**
     * Calls the asynchronous task method which overwrites a term in the RecyclerView and database.
     * @param terms = instance of Terms model object.
     */
    public void update(Terms terms) {
        new UpdateTermsAsyncTask(termsDao).execute(terms);
    }

    /**
     * Calls the asynchronous task method which removes a term from the RecyclerView and database.
     * @param terms = instance of Terms model object.
     */
    public void delete(Terms terms) {
        new DeleteTermsAsyncTask(termsDao).execute(terms);
    }

    /**
     *Getter for retrieving all the Assessments in the database.
     * @return allAssessments.
     */
    public LiveData<List<Assessments>> getAllAssessments() {
        return allAssessments;
    }

    /**
     * Getter for retrieving all the Courses in the database.
     * @return allCourses.
     */
    public LiveData<List<Courses>> getAllCourses() { return allCourses; }

    /**
     * Getter for retrieving all the Terms in the database.
     * @return allTerms.
     */
    public LiveData<List<Terms>> getAllTerms() {
        return allTerms;
    }

    /**
     * Getter for the terms by checking their respective IDs.
     * @param id = int
     * @return termsByID
     */
    public LiveData<List<Terms>> getTermById(int id) {
        termsByID = termsDao.getTermById(id);
        return termsByID;
    };

    /**
     * Asynchronous task subclass that inserts Assessments into the Room Database with aid of the Dao.
     */
    private static class InsertAssessmentsAsyncTask extends AsyncTask<Assessments, Void, Void> {
        private AssessmentsDao assessmentsDao;

        private InsertAssessmentsAsyncTask(AssessmentsDao assessmentsDao) {
            this.assessmentsDao = assessmentsDao;
        }

        @Override
        protected Void doInBackground(Assessments... assessments) {
            assessmentsDao.insert(assessments[0]);
            return null;
        }
    }

    /**
     * Asynchronous task subclass that overwrites Assessments in the Room Database with aid of the Dao.
     */
    private static class UpdateAssessmentsAsyncTask extends AsyncTask<Assessments, Void, Void> {
        private AssessmentsDao assessmentsDao;

        private UpdateAssessmentsAsyncTask(AssessmentsDao assessmentsDao) {
            this.assessmentsDao = assessmentsDao;
        }

        @Override
        protected Void doInBackground(Assessments... assessments) {
            assessmentsDao.update(assessments[0]);
            return null;
        }
    }

    /**
     * Asynchronous task subclass that removes Assessments from the Room Database with aid of the Dao.
     */
    private static class DeleteAssessmentsAsyncTask extends AsyncTask<Assessments, Void, Void> {
        private AssessmentsDao assessmentsDao;

        private DeleteAssessmentsAsyncTask(AssessmentsDao assessmentsDao) {
            this.assessmentsDao = assessmentsDao;
        }

        @Override
        protected Void doInBackground(Assessments... assessments) {
            assessmentsDao.delete(assessments[0]);
            return null;
        }
    }

    /**
     * Asynchronous task subclass that inserts Courses into the Room Database with aid of the Dao.
     */
    private static class InsertCoursesAsyncTask extends AsyncTask<Courses, Void, Void> {
        private CoursesDao coursesDao;

        private InsertCoursesAsyncTask(CoursesDao coursesDao) {
            this.coursesDao = coursesDao;
        }

        @Override
        protected Void doInBackground(Courses... courses) {
            coursesDao.insert(courses[0]);
            return null;
        }
    }

    /**
     * Asynchronous task subclass that overwrites Courses in the Room Database with aid of the Dao.
     */
    private static class UpdateCoursesAsyncTask extends AsyncTask<Courses, Void, Void> {
        private CoursesDao coursesDao;

        private UpdateCoursesAsyncTask(CoursesDao coursesDao) {
            this.coursesDao = coursesDao;
        }

        @Override
        protected Void doInBackground(Courses... courses) {
            coursesDao.update(courses[0]);
            return null;
        }
    }

    /**
     * Asynchronous task subclass that removes Courses from the Room Database with aid of the Dao.
     */
    private static class DeleteCoursesAsyncTask extends AsyncTask<Courses, Void, Void> {
        private CoursesDao coursesDao;

        private DeleteCoursesAsyncTask(CoursesDao coursesDao) {
            this.coursesDao = coursesDao;
        }

        @Override
        protected Void doInBackground(Courses... courses) {
            coursesDao.delete(courses[0]);
            return null;
        }
    }

    /**
     * Asynchronous task subclass that inserts Terms into the Room Database with aid of the Dao.
     */
    private static class InsertTermsAsyncTask extends AsyncTask<Terms, Void, Void> {
        private TermsDao termsDao;

        private InsertTermsAsyncTask(TermsDao termsDao) {
            this.termsDao = termsDao;
        }

        @Override
        protected Void doInBackground(Terms... terms) {
            termsDao.insert(terms[0]);
            return null;
        }
    }

    /**
     * Asynchronous task subclass that overwrites Terms in the Room Database with aid of the Dao.
     */
    private static class UpdateTermsAsyncTask extends AsyncTask<Terms, Void, Void> {
        private TermsDao termsDao;

        private UpdateTermsAsyncTask(TermsDao termsDao) {
            this.termsDao = termsDao;
        }

        @Override
        protected Void doInBackground(Terms... terms) {
            termsDao.update(terms[0]);
            return null;
        }
    }

    /**
     * Asynchronous task subclass that removes Terms from the Room Database with aid of the Dao.
     */
    private static class DeleteTermsAsyncTask extends AsyncTask<Terms, Void, Void> {
        private TermsDao termsDao;

        private DeleteTermsAsyncTask(TermsDao termsDao) {
            this.termsDao = termsDao;
        }

        @Override
        protected Void doInBackground(Terms... terms) {
            termsDao.delete(terms[0]);
            return null;
        }
    }

}
