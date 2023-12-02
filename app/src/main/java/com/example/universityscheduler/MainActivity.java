package com.example.universityscheduler;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import com.example.universityscheduler.Model.Assessments;
import com.example.universityscheduler.Model.Courses;
import com.example.universityscheduler.Model.Terms;
import com.example.universityscheduler.ViewModel.SchedulerVM;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Activity class for the scheduler. Initializes and starts the main application.
 */
public class MainActivity extends AppCompatActivity {

    public static final String PRIMARY_NOTIFICATION_CHANNEL = "primary_notification_channel";
    private SchedulerVM schedulerVM;
    public NotificationManager notificationManager;
    private NotificationManagerCompat notificationManagerCompat;
    TextView schedulerProgress;
    TextView schedulerInformation;
    private List<Courses> coursesList = new ArrayList<>();
    private List<Assessments> assessmentsList = new ArrayList<>();
    private List<Terms> termsList = new ArrayList<>();

    /**
     * Loads the XML Main Activity and calls the Scheduler ViewModel and initializes it.
     * @param savedInstanceState = instance of Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        //Everytime the home page is visited, the database gets wiped/refreshed. To change this for testing purposes, comment out the line of code below.
        getApplicationContext().deleteDatabase("c196_scheduler_db");

        schedulerProgress = findViewById(R.id.progressTextView);
        schedulerInformation  = findViewById(R.id.schedulerInformation);

        schedulerVM = new ViewModelProvider(this).get(SchedulerVM.class);
        schedulerVM.getAllCourses().observe(this, new Observer<List<Courses>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(@Nullable List<Courses> courses) {
                StringBuilder progressString = new StringBuilder();
                int inProgress = 0;
                int completed = 0;
                int dropped = 0;
                int planToTake = 0;

                //Switch case for the course status options.
                assert courses != null;
                for (Courses course : courses) {
                    switch(course.getCoursesStatus()) {
                        case "In Progress":
                            inProgress++;
                            break;
                        case "Completed":
                            completed++;
                            break;
                        case "Dropped":
                            dropped++;
                            break;
                        case "Planning to Take":
                            planToTake++;
                            break;
                    }
                }

                //Outputs the progress of courses.
                progressString.append(inProgress + " Courses are currently in progress.");
                progressString.append("\n");
                progressString.append(completed + " Courses completed.");
                progressString.append("\n");
                progressString.append(dropped + " Courses dropped.");
                progressString.append("\n");
                progressString.append(planToTake + " Courses that are being planned on.");
                progressString.append("\n");

                schedulerProgress.setText(progressString.toString());
                schedulerInformation.setText("Welcome to the University Scheduler app!" + "\n" +
                        "Here you can manage your terms, the courses in your term, and the assessments your courses have." + "\n" +
                        "Click on the navigation tab in the top right corner." + "\n" +
                        "From there, you can click on a page of your choosing" + "\n" +
                        "to add, edit, or delete your terms, courses, and assessments." + "To reset the database from scratch," + "\n" +
                        "keep coming back to this home page.");

                //Scheduler information TextView that guides the user open first opening the Scheduler App.
                LinearLayout linearLayout = findViewById(R.id.MainActivityLinearLayout);

                LinearLayout.LayoutParams schedulerTextParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                //  schedulerTextParams.setMargins(42,42,42,42);
                //schedulerInformation.setLayoutParams(schedulerTextParams);
                //if(schedulerInformation.getParent() != null){
                //  ((ViewGroup)schedulerInformation.getParent()).removeView(schedulerInformation);
                //}
                //linearLayout.addView(schedulerInformation);
            }
        });
    }

    /**
     * Click handler for the options navigation menu, allowing the user to navigate more easily throughout the Scheduler App.
     * @param menuItem = instance of MenuItem
     * @return = menuItem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_navigation) {
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(menuItem);
    }

    /**
     *Adds items to the Home Page Options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}