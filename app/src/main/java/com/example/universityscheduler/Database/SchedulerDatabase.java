package com.example.universityscheduler.Database;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.universityscheduler.Model.Assessments;
import com.example.universityscheduler.Model.Courses;
import com.example.universityscheduler.Model.Terms;

/**
 * Initializes the database for the Scheduler App. The SchedulerRepository refers and builds off of
 * this class. It's also the only class that can access this one.
 */

@Database(entities = {Terms.class, Courses.class, Assessments.class}, version = 9, exportSchema = false)
public abstract class SchedulerDatabase extends RoomDatabase {

    private static SchedulerDatabase schedulerDatabase;

    public abstract TermsDao termsDao();
    public abstract CoursesDao coursesDao();
    public abstract AssessmentsDao assessmentsDao();

    /**
     * Creates and initializes the scheduler database.
     * @param context = instance of the Context object.
     * @return = schedulerDatabase.
     */
    public static synchronized SchedulerDatabase getInstance(Context context) {
        //if (schedulerDatabase == null) {
            schedulerDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    SchedulerDatabase.class, "university_scheduler_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        //}
        return schedulerDatabase;
    }

    /**
     * Room Database method that calls the asynchronous database population method.
     */
    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase sqLiteDatabase) {
            super.onCreate(sqLiteDatabase);
            new PopulateDbAsyncTask(schedulerDatabase).execute();
        }
    };

    /**
     * Asynchronous subclass that links back to the DAOs of each model object and populates their selected database tables
     * with my chosen sample data.
     */
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private TermsDao termsDao;
        private CoursesDao coursesDao;
        private AssessmentsDao assessmentsDao;

        /**
         * Links the data members to the DAOs of each model object.
         * @param schedulerDatabase = instance of SchedulerDatabase.
         */
        private PopulateDbAsyncTask(SchedulerDatabase schedulerDatabase) {
            termsDao = schedulerDatabase.termsDao();
            coursesDao = schedulerDatabase.coursesDao();
            assessmentsDao = schedulerDatabase.assessmentsDao();
        }

        /**
         * Loads the sample data into the DAOs of the terms, courses, and assessments.
         * @param voids = required parameter of Void.
         * @return = null
         */
        @Override
        protected Void doInBackground(Void... voids) {
            termsDao.insert(new Terms("Fall Term", "8/1/2021", "10/31/2021"));
            termsDao.insert(new Terms("Winter Term", "11/1/2022", "2/31/2022"));
            termsDao.insert(new Terms("Spring Term", "3/1/2022", "4/30/2022"));
            termsDao.insert(new Terms("Summer Term", "5/1/2022", "8/31/2022"));
            coursesDao.insert(new Courses(1, "Android Development", "9/10/2022", "11/28/2022", "In Progress", "Harrison Thacker", "801-372-3177", "harrison.thacker69@gmail.com", "This course will take a great deal of time."));
            coursesDao.insert(new Courses(2, "Intro To User Experience Design", "10/5/2022", "10/28/2022", "Completed", "Alan R Pearlman", "901-245-0451", "arp73@gmail.com", "This was my first real understanding of the UX industry."));
            coursesDao.insert(new Courses(3, "Advanced Databases", "2/4/2022", "3/15/2022", "In Progress", "Bob Moog", "675-804-1239", "bob.moog68@gmail.com", "Data Analysts/Engineers do a lot of this stuff."));
            coursesDao.insert(new Courses(4, "Java 1", "8/1/2021", "8/28/2022", "Dropped", "Dave Smith", "629-544-1978", "dave.smith78@gmail.com", "Luckily I was no stranger to JavaFX."));
            coursesDao.insert(new Courses(4, "Leadership Skills", "9/1/2021", "9-20-2021", "Planning to Take", "Tom Oberheim", "936-501-2022", "tom.oberheim22@gmail.com", "This was a general I finished in a week or two."));
            assessmentsDao.insert(new Assessments("1001 PA", "10/10/2027", "11/10/2027", "Performance Assessment", 1));
            assessmentsDao.insert(new Assessments("3004 OA", "11/5/2028", "12/5/2028", "Objective Assessment", 2));
            assessmentsDao.insert(new Assessments("5003 PA", "6/31/2022", "7/31/2022", "Performance Assessment", 3));
            assessmentsDao.insert(new Assessments("2098 PA", "12/19,2022", "1/19/2023", "Performance Assessment", 4));
            assessmentsDao.insert(new Assessments("0451 OA", "9/31/2024", "10/31/2024", "Objective Assessment", 5));

            return null;
        }
    }
}
