package com.example.universityscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.universityscheduler.Adapter.AssessmentsAdapter;
import com.example.universityscheduler.Model.Assessments;
import com.example.universityscheduler.ViewModel.SchedulerVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.Objects;

/**
 * Activity class for the Assessments RecyclerView list. From here, the user can add a new assessment, or view/edit one, or delete one altogether.
 */
public class AssessmentsActivity extends AppCompatActivity {

    private SchedulerVM schedulerVM;

    /**
     * Loads the Assessments list XML and initializes the Assessments RecyclerView.
     * @param savedInstanceState = instance of bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //When the floating action button is clicked, the user is redirected to the edit/add assessment activity.
        FloatingActionButton floatingActionButton = findViewById(R.id.assessmentsFab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentsActivity.this, EditAssessmentActivity.class);
                startActivity(intent);
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Loads the layout for the Assessments RecyclerView.
        RecyclerView recyclerView = findViewById(R.id.assessmentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        final AssessmentsAdapter assessmentsAdapter = new AssessmentsAdapter();
        recyclerView.setAdapter(assessmentsAdapter);

        //Loads and initializes the ViewModel and has the Observer get all the assessments and then proceeds to set them.
        schedulerVM = new ViewModelProvider(this).get(SchedulerVM.class);
        schedulerVM.getAllAssessments().observe(this, new Observer<List<Assessments>>() {
            @Override
            public void onChanged(@Nullable List<Assessments> assessments) {
                assessmentsAdapter.setAssessmentsList(assessments);
            }
        });

        //OnClick listener for when the user clicks on an item in the RecyclerView, redirecting them to the Assessments detail activity.
        assessmentsAdapter.setOnItemClickListener(new AssessmentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Assessments assessments) {
                Intent intent = new Intent(AssessmentsActivity.this, DetailAssessmentActivity.class);
                intent.putExtra(DetailAssessmentActivity.EXTRA_ID, assessments.getId());
                intent.putExtra(DetailAssessmentActivity.EXTRA_TITLE, assessments.getAssessmentTitle());
                intent.putExtra(DetailAssessmentActivity.EXTRA_START_DATE, assessments.getAssessmentStartDate());
                intent.putExtra(DetailAssessmentActivity.EXTRA_DUE_DATE, assessments.getAssessmentEndDate());
                intent.putExtra(DetailAssessmentActivity.EXTRA_TYPE, assessments.getAssessmentType());
                intent.putExtra(DetailAssessmentActivity.EXTRA_COURSE_ID, assessments.getCourseId());
                startActivity(intent);
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
     *Adds items to the Assessments Options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
