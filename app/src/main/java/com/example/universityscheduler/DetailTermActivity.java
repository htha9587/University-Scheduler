package com.example.universityscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.universityscheduler.Model.Courses;
import com.example.universityscheduler.Model.Terms;
import com.example.universityscheduler.ViewModel.SchedulerVM;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller class for the Detailed view of a term. From here, the user can read data
 * about the term, the courses inside of it, remove a term, or go to the add/edit activity if he/she chooses.
 */
public class DetailTermActivity extends AppCompatActivity {

    SchedulerVM schedulerVM;
    private int termID;
    private Terms term;
    public static final String EXTRA_ID = "com.example.universityscheduler.EXTRA_ID";
    private TextView termTitleTextView;
    private TextView termSDTextView;
    private TextView termEDTextView;
    private TextView termCoursesTextView;

    /**
     * Loads the XML and binds the Terms attributes to the UI.
     * @param savedInstanceState = instance of Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        schedulerVM = new ViewModelProvider(this).get(SchedulerVM.class);

        termTitleTextView = findViewById(R.id.termNameDetail);
        termSDTextView = findViewById(R.id.termSDDetail);
        termEDTextView = findViewById(R.id.termEDDetail);
        termCoursesTextView = findViewById(R.id.termCoursesDetail);

        Intent intent = getIntent();
        termID = intent.getIntExtra(EXTRA_ID, -1);
        courseListPopulate();

        //Checks through and validates the ID of each Term entry in the Database.
        if (termID > 0) {
            schedulerVM.getAllTerms().observe(this, new Observer<List<Terms>>() {
                @Override
                public void onChanged(@Nullable List<Terms> terms) {
                    assert terms != null;
                    for (Terms termsToCheck : terms) {
                        if (termsToCheck.getId() == termID) {
                            term = termsToCheck;
                        }
                    }
                    termTitleTextView.setText(term.getTermTitle());
                    termSDTextView.setText(term.getTermStartDate());
                    termEDTextView.setText(term.getTermEndDate());
                }
            });
        }
    }

    /**
     * Has the courses list text view print out the corresponding list of courses that are part of a term.
     */
    private void courseListPopulate() {
        final List<Courses> coursesList = new ArrayList<>();
        schedulerVM.getAllCourses().observe(this, new Observer<List<Courses>>() {
            @Override
            public void onChanged(@Nullable List<Courses> courses) {
                assert courses != null;
                for (Courses course : courses) {
                    if (course.getTermId() == termID) {
                        coursesList.add(course);
                    }
                }

                //Looks through the list of assigned courses to the term and outputs them, if there are any.
                StringBuilder listOfCourses = new StringBuilder();
                for (Courses course : coursesList) {
                    listOfCourses.append(course.getCourseTitle());
                    listOfCourses.append("\n");
                }
                if(listOfCourses.toString().isEmpty()) {
                    listOfCourses.append("This term has no courses!");
                }
                termCoursesTextView.setText(listOfCourses.toString());
            }
        });
    }

    /**
     * Click handler to open the Create/Edit Term page when the user clicks.
     * @param view = instance of View.
     */
    public void termEditButtonClicked(View view) {
        Intent intent = new Intent(this, EditTermActivity.class);
        intent.putExtra(DetailTermActivity.EXTRA_ID, term.getId());
        intent.putExtra(EditTermActivity.EXTRA_TITLE, term.getTermTitle());
        intent.putExtra(EditTermActivity.EXTRA_START_DATE, term.getTermStartDate());
        intent.putExtra(EditTermActivity.EXTRA_END_DATE, term.getTermEndDate());
        startActivity(intent);
    }

    /**
     * Click handler to delete the Term from the RecyclerView and Room Database.
     * Also gives verification if a user tries to drop a term that still has courses assigned to it.
     * @param view = instance of View.
     */
    public void termRemoveButtonClicked(View view) {
        if (termCoursesTextView.getText().toString().trim().equals("This term has no courses!")) {
            Terms termsToDelete = new Terms("", "", "");
            termsToDelete.setId(getIntent().getIntExtra(EXTRA_ID, -1));
            if (termsToDelete.getId() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Term?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage("Are you sure you wish to delete this particular term?"
                        + " Do you wish to proceed?");
                builder.setPositiveButton("Yes", (dialog, id) ->
                {
                    dialog.dismiss();
                    schedulerVM.delete(termsToDelete);
                    Intent intent = new Intent(DetailTermActivity.this, TermsActivity.class);
                    this.startActivity(intent);
                    finish();
                });

                builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } else {
            Toast.makeText(this, "You can't delete this term because there are still courses assigned to it!", Toast.LENGTH_LONG).show();
            return;
        }
    }
}
