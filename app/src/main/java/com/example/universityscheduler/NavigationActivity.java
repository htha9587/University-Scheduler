package com.example.universityscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Activity that handles the Navigation tab/page for the Scheduler App.
 */
public class NavigationActivity extends AppCompatActivity {

    /**
     * Loads the XML File and initializes the Toolbar.
     * @param savedInstanceState = instance of Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Click handler for when the user clicks on the Open Assessments button.
     * @param view = instance of View object.
     */
    public void openAssessmentsButtonClicked(View view) {
        Intent intent = new Intent(this, AssessmentsActivity.class);
        startActivity(intent);
    }

    /**
     * Click handler for when the user clicks on the Open Courses button.
     * @param view = instance of View object.
     */
    public void openCoursesButtonClicked(View view) {
        Intent intent = new Intent(this, CoursesActivity.class);
        startActivity(intent);
    }

    /**
     * Click handler for when the user clicks on the Open Terms button.
     * @param view = instance of View object.
     */
    public void openTermsButtonClicked(View view) {
        Intent intent = new Intent(this, TermsActivity.class);
        startActivity(intent);
    }

    /**
     * Click handler for when the user clicks on the Home Page button.
     * @param view = instance of View object.
     */
    public void openHomeButtonClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
