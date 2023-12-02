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
import com.example.universityscheduler.Adapter.CoursesAdapter;
import com.example.universityscheduler.Model.Courses;
import com.example.universityscheduler.ViewModel.SchedulerVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.Objects;

/**
 * Activity class for the Courses RecyclerView list. From here, the user can add a new course, or view/edit one, or delete one altogether.
 */
public class CoursesActivity extends AppCompatActivity {

    private SchedulerVM schedulerVM;

    /**
     * Loads the Course list XML and initializes the Course RecyclerView.
     * @param savedInstanceState = instance of bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //When the floating action button is clicked, the user is redirected to the edit/add course activity.
        FloatingActionButton floatingActionButton = findViewById(R.id.coursesFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditCourseActivity.class);
                startActivity(intent);
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Loads the layout for the Course RecyclerView.
        RecyclerView recyclerView = findViewById(R.id.courses_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        final CoursesAdapter coursesAdapter = new CoursesAdapter();
        recyclerView.setAdapter(coursesAdapter);

        //Loads and initializes the ViewModel and has the Observer get all the courses and then proceeds to set them.
        schedulerVM = new ViewModelProvider(this).get(SchedulerVM.class);
        schedulerVM.getAllCourses().observe(this, new Observer<List<Courses>>() {
            @Override
            public void onChanged(@Nullable List<Courses> coursesList) {
                coursesAdapter.setCourses(coursesList);
            }
        });

        //OnClick listener for when the user clicks on an item in the RecyclerView, redirecting them to the Course detail activity.
        coursesAdapter.setOnItemClickListener(new CoursesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Courses courses) {
                Intent intent = new Intent(CoursesActivity.this, DetailCourseActivity.class);
                intent.putExtra(DetailCourseActivity.EXTRA_ID, courses.getId());
                //intent.putExtra(DetailCourseActivity.EXTRA_NAME, courses.getInstructorName());
                //intent.putExtra(DetailCourseActivity.EXTRA_PHONE, courses.getInstructorPhone());
                //intent.putExtra(DetailCourseActivity.EXTRA_EMAIL, courses.getInstructorEmail());
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
        int itemId = menuItem.getItemId();

        if (itemId == R.id.action_navigation) {
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(menuItem);
    }

    /**
     *Adds items to the Courses Options menu.
      */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
