package com.example.universityscheduler;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.universityscheduler.Model.Courses;
import com.example.universityscheduler.Model.Terms;
import com.example.universityscheduler.Util.DPFragment;
import com.example.universityscheduler.ViewModel.SchedulerVM;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * Controller class for Adding/editing University courses. From here, the user can add a new
 * course to the RecyclerView/Room Database, or edit and save details
 * about an existing course.
 */
public class EditCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SchedulerVM schedulerVM;
    private EditText editCourseName;
    private Spinner editCourseTermSpinner;
    private TextView editCourseStartDate;
    private TextView editCourseEndDate;
    private Spinner courseStatusSpinner;
    private TextView editInstructorName;
    private TextView editInstructorPhone;
    private TextView editInstructorEmail;
    private EditText editCourseNotes;
    private int termID;
    private boolean editMode = false;
    public static final String EXTRA_ID = "com.example.universityscheduler.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.universityscheduler.EXTRA_TITLE";
    public static final String EXTRA_START_DATE = "com.example.universityscheduler.EXTRA_START_DATE";
    public static final String EXTRA_END_DATE = "com.example.universityscheduler.EXTRA_END_DATE";
    public static final String EXTRA_TERM_ID = "com.example.universityscheduler.EXTRA_TERM_ID";
    public static final String EXTRA_STATUS = "com.example.universityscheduler.EXTRA_STATUS";
    public static final String EXTRA_NAME = "com.example.universityscheduler.EXTRA_NAME";
    public static final String EXTRA_PHONE = "com.example.universityscheduler.EXTRA_PHONE";
    public static final String EXTRA_EMAIL = "com.example.universityscheduler.EXTRA_EMAIL";
    public static final String EXTRA_NOTES = "com.example.universityscheduler.EXTRA_NOTES";

    /**
     * Loads the XML and binds the Course attributes to the UI.
     * @param savedInstanceState = instance of Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        editCourseName = findViewById(R.id.editCourseName);
        editCourseTermSpinner = findViewById(R.id.editCourseTermSpinner);
        editCourseStartDate = findViewById(R.id.editCourseStartDate);
        editCourseEndDate = findViewById(R.id.editCourseEndDate);
        editInstructorName = findViewById(R.id.editInstructorName);
        editInstructorPhone = findViewById(R.id.editInstructorPhone);
        editInstructorEmail = findViewById(R.id.editInstructorEmail);
        courseStatusSpinner = findViewById(R.id.courseStatusSpinner);
        editCourseNotes = findViewById(R.id.editCourseNotes);

        if (intent.getExtras() != null) {
            editMode = true;
            setTitle("Edit Course");
            editCourseName.setText(intent.getStringExtra(EXTRA_TITLE));
            editCourseStartDate.setText(intent.getStringExtra(EXTRA_START_DATE));
            editCourseEndDate.setText(intent.getStringExtra(EXTRA_END_DATE));
            editCourseNotes.setText(intent.getStringExtra(EXTRA_NOTES));
            editInstructorName.setText(intent.getStringExtra(EXTRA_NAME));
            editInstructorPhone.setText(intent.getStringExtra(EXTRA_PHONE));
            editInstructorEmail.setText(intent.getStringExtra(EXTRA_EMAIL));
        }

        if (editCourseTermSpinner != null) {
            editCourseTermSpinner.setOnItemSelectedListener(this);
        }

        //Initializes the terms spinner.
        schedulerVM = new ViewModelProvider(this).get(SchedulerVM.class);
        final ArrayAdapter<Terms> termsSpinner = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        termsSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schedulerVM.getAllTerms().observe(this, new Observer<List<Terms>>() {
            @Override
            public void onChanged(@Nullable List<Terms> terms) {
                termsSpinner.addAll(terms);
                editCourseTermSpinner.setAdapter(termsSpinner);
                if(getIntent().getExtras() != null) {
                    schedulerVM.getTermById(getIntent().getIntExtra(EXTRA_TERM_ID, -1)).observe(EditCourseActivity.this, new Observer<List<Terms>>() {
                        @Override
                        public void onChanged(@Nullable List<Terms> terms) {
                            Terms retrieveTerms;
                            assert terms != null;
                            for(Terms term: terms)
                            {
                                if(term.getId() == getIntent().getIntExtra(EXTRA_TERM_ID, -1))
                                {
                                    retrieveTerms = term;
                                    int termSpinnerPosition = termsSpinner.getPosition(retrieveTerms);
                                    editCourseTermSpinner.setSelection(termSpinnerPosition);
                                }
                            }
                        }
                    });
                }

            }
        });

        if (courseStatusSpinner != null) {
            courseStatusSpinner.setOnItemSelectedListener(this);
        }

        //Sets up the course status spinner.
        String[] statusOptions = new String[]{"In Progress", "Completed", "Dropped", "Planning to Take"};
        final ArrayAdapter<String> statusSpinner = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statusOptions);
        statusSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (statusSpinner != null) {
            courseStatusSpinner.setAdapter(statusSpinner);
            if(getIntent().getExtras() != null) {
                int statusSpinnerPosition = statusSpinner.getPosition(getIntent().getStringExtra(EXTRA_STATUS));
                courseStatusSpinner.setSelection(statusSpinnerPosition);
            }
        }

    }

    /**
     * Handler that gets the term ID for the Term spinner.
     * @param parent = AdapterView
     * @param view = View
     * @param position = int
     * @param id = long
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getItemAtPosition(position).getClass().getSimpleName()) {
            case "Terms":
                termID = ((Terms) parent.getItemAtPosition(position)).getId();
                break;
        }
    }

    /**
     * Handler for when nothing in the Term spinner is selected.
     * @param parent = AdapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Date Picker click handler for the Course start date.
     * @param view = instance of View
     */
    public void editCourseStartDate(View view) {
        DialogFragment newFragment = new DPFragment("Course Start Date");
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Date Picker click handler for the Course end date.
     * @param view = instance of View
     */
    public void editCourseEndDate(View view) {
        DialogFragment newFragment = new DPFragment("Course End Date");
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Processes the date picker selections and prints the date to the Course start/end dates.
     * @param year = calendar year
     * @param month = calendar month
     * @param day = calendar day
     * @param tag = String type variable for DateTag
     */
    public void processDatePickerResult(int year, int month, int day, String tag) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string +
                "/" + day_string + "/" + year_string);

        TextView dateToSet;

        if (tag.equals("Course Start Date")) {
            dateToSet = findViewById(R.id.editCourseStartDate);
        } else if (tag.equals("Course End Date")) {
            dateToSet = findViewById(R.id.editCourseEndDate);
        } else {
            dateToSet = null;
        }

        if (dateToSet != null) {
            dateToSet.setText(dateMessage);
        }

    }

    /**
     * Click handler that either Saves a new Course, or updates info from a preexisting one.
     * @param view = instance of View.
     */
    public void saveCourseButtonClicked(View view) {
        String courseTitle = editCourseName.getText().toString();
        String courseStartDate = editCourseStartDate.getText().toString();
        String courseEndDate = editCourseEndDate.getText().toString();
        String courseNotes = editCourseNotes.getText().toString();
        String instructorName = editInstructorName.getText().toString();
        String instructorPhone = editInstructorPhone.getText().toString();
        String instructorEmail = editInstructorEmail.getText().toString();
        String coursesStatus = courseStatusSpinner.getSelectedItem().toString();

        if (
                courseTitle.trim().isEmpty()
                        || courseStartDate.trim().isEmpty()
                        || courseEndDate.trim().isEmpty()
                        || courseNotes.trim().isEmpty()
                        || instructorName.trim().isEmpty()
                        || instructorPhone.trim().isEmpty()
                        || instructorEmail.trim().isEmpty()
                        || coursesStatus.trim().isEmpty()
        ) {
            Toast.makeText(this, "No fields can be left empty, get that sorted please!", Toast.LENGTH_LONG).show();
            return;
        }

        DateTimeFormatter dateTimeFormatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        }
        LocalDate sdParsed = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sdParsed = LocalDate.parse(courseStartDate, dateTimeFormatter);
        }
        LocalDate edParsed = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            edParsed = LocalDate.parse(courseEndDate, dateTimeFormatter);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (sdParsed.isAfter(edParsed)) {
                Toast.makeText(this, "Please make sure the Course end date is later than that of the start date!", Toast.LENGTH_LONG).show();
                return;
            }
        }

        Courses coursesToSave = new Courses(termID, courseTitle,  courseStartDate, courseEndDate, coursesStatus,
                 instructorName, instructorPhone,  instructorEmail,  courseNotes);

        //Alert dialogs for editing vs adding a new course.
        if (editMode) {
            coursesToSave.setId(getIntent().getIntExtra(EXTRA_ID, -1));
            if (coursesToSave.getId() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Save Edits To Course?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage("Are you sure you wish to save edits to this particular course?"
                        + " Do you wish to proceed?");
                builder.setPositiveButton("Yes", (dialog, id) ->
                {
                    dialog.dismiss();
                    schedulerVM.update(coursesToSave);
                    Intent intent = new Intent(this, CoursesActivity.class);
                    this.startActivity(intent);
                    finish();
                });

                builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add New Course?");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setMessage("Are you sure you wish to add this new Course?"
                    + " Do you wish to proceed?");
            builder.setPositiveButton("Yes", (dialog, id) ->
            {
                dialog.dismiss();
                schedulerVM.insert(coursesToSave);
                Intent intent = new Intent(this, CoursesActivity.class);
                this.startActivity(intent);
                finish();
            });

            builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    /**
     * Click handler for when the user hits the cancel button, redirecting back to the Course list page.
     * @param view = instance of View.
     */
    public void cancelCourseButtonClicked(View view) {
        Intent intent = new Intent(this, CoursesActivity.class);
        startActivity(intent);
    }

}
