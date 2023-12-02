package com.example.universityscheduler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.example.universityscheduler.Model.Assessments;
import com.example.universityscheduler.Model.Courses;
import com.example.universityscheduler.Util.DPFragment;
import com.example.universityscheduler.ViewModel.SchedulerVM;
import java.util.List;
import java.util.Objects;

/**
 * Controller class for Adding/editing University assessments. From here, the user can add a new
 * Assessment to the RecyclerView/Room Database, or edit and save details
 * about an existing assessment.
 */
public class EditAssessmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SchedulerVM schedulerVM;
    private int courseID;
    private EditText assessmentTitleEdit;
    private TextView editAssessmentSD;
    private TextView editAssessmentGD;
    private Spinner coursesSpinner;
    private RadioButton paRadioButton;
    private RadioButton oaRadioButton;
    String assessmentType = "Performance Assessment";
    private boolean editMode = false;
    public static final String EXTRA_ID = "com.example.universityscheduler.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.universityscheduler.EXTRA_TITLE";
    public static final String EXTRA_START_DATE = "com.example.universityscheduler.EXTRA_START_DATE";
    public static final String EXTRA_DUE_DATE = "com.example.universityscheduler.EXTRA_DUE_DATE";
    public static final String EXTRA_TYPE = "com.example.universityscheduler.EXTRA_TYPE";
    public static final String EXTRA_COURSE_ID = "com.example.universityscheduler.EXTRA_END_DATE";

    /**
     * Loads the XML and binds the Assessment attributes to the UI.
     * @param savedInstanceState = instance of Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        assessmentTitleEdit = findViewById(R.id.assessmentTitleEdit);
        editAssessmentSD = findViewById(R.id.editAssessmentSD);
        editAssessmentGD = findViewById(R.id.editAssessmentGD);
        coursesSpinner = findViewById(R.id.editAssessmentCourse);
        paRadioButton = findViewById(R.id.performanceAssessment);
        oaRadioButton = findViewById(R.id.objectiveAssessment);

        Intent intent = getIntent();

        schedulerVM = new ViewModelProvider(this).get(SchedulerVM.class);

        //Sets up parameters and gets the intents if the user clicks the edit button.
        if (intent.getExtras() != null) {
            editMode = true;
            setTitle("Edit Assessment");
            assessmentTitleEdit.setText(intent.getStringExtra(EXTRA_TITLE));
            editAssessmentSD.setText(intent.getStringExtra(EXTRA_START_DATE));
            editAssessmentGD.setText(intent.getStringExtra(EXTRA_DUE_DATE));
            switch (intent.getStringExtra(EXTRA_TYPE)) {
                case "Objective Assessment":
                    oaRadioButton.setChecked(true);
                    break;
                default:
                    paRadioButton.setChecked(true);
                    break;
            }
        }

        if (coursesSpinner != null) {
            coursesSpinner.setOnItemSelectedListener(this);
        }

        schedulerVM = new ViewModelProvider(this).get(SchedulerVM.class);
        final ArrayAdapter<Courses> coursesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        coursesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schedulerVM.getAllCourses().observe(this, new Observer<List<Courses>>() {
            //Sets up the course spinner for the assessment.
            @Override
            public void onChanged(@Nullable List<Courses> courses) {
                coursesArrayAdapter.addAll(courses);
                coursesSpinner.setAdapter(coursesArrayAdapter);
                if(getIntent().getExtras() != null) {
                    schedulerVM.getAllCourses().observe(EditAssessmentActivity.this, new Observer<List<Courses>>() {
                        @Override
                        public void onChanged(@Nullable List<Courses> courses) {
                            Courses coursesToFind;
                            assert courses != null;
                            for (Courses course: courses) {
                                if (course.getId() == getIntent().getIntExtra(EXTRA_COURSE_ID, -1)) {
                                    coursesToFind = course;
                                    int courseSpinnerPosition = coursesArrayAdapter.getPosition(coursesToFind);
                                    coursesSpinner.setSelection(courseSpinnerPosition);
                                }
                            }
                        }
                    });
                }

            }
        });

    }

    /**
     * Handler that gets the course ID for the Course spinner.
     * @param parent = AdapterView
     * @param view = View
     * @param position = int
     * @param id = long
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        courseID = ((Courses) parent.getItemAtPosition(position)).getId();
    }

    /**
     * Handler for when nothing in the Course spinner is selected.
     * @param parent = AdapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Date Picker click handler for the Assessment Goal/Due date.
     * @param view = instance of View
     */
    public void assessEDClicked(View view) {
        DialogFragment newFragment = new DPFragment("Assessment Due Date");
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Date Picker click handler for the Assessment start date.
     * @param view = instance of View
     */
    public void assessSDClicked(View view) {
        DialogFragment newFragment = new DPFragment("assessmentStartDate");
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Processes the date picker selections and prints the date to the Assessment Goal Date.
     * @param year = calendar year
     * @param month = calendar month
     * @param day = calendar day
     * @param tag = String type variable for DateTag
     */
    public void processDatePickerResult(int year, int month, int day, String tag) {

        String monthString = Integer.toString(month + 1);
        String dayString = Integer.toString(day);
        String yearString = Integer.toString(year);
        String dateMessage = (monthString +
                "/" + dayString + "/" + yearString);
        TextView dateSetter;

        if (tag.equals("assessmentStartDate")) {
            dateSetter = findViewById(R.id.editAssessmentSD);
        } else if (tag.equals("Assessment Due Date")) {
            dateSetter = findViewById(R.id.editAssessmentGD);
        } else {
            dateSetter = null;
        }

        if (dateSetter != null) {
            dateSetter.setText(dateMessage);
        }

    }

    /**
     * Click handler for the Assessment type radio buttons.
     * @param view = instance of View.
     */
    @SuppressLint("NonConstantResourceId")
    public void assessmentTypeClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.performanceAssessment:
                if (checked)
                    this.assessmentType = "Performance Assessment";
                break;
            case R.id.objectiveAssessment:
                if (checked)
                    this.assessmentType = "Objective Assessment";
                break;
        }
    }

    /**
     * Click handler that either Saves a new Assessment, or updates info from a preexisting one.
     * @param view = instance of View.
     */
    public void saveAssessmentClicked(View view) {
        String assessmentTitle = assessmentTitleEdit.getText().toString();
        String assessmentStartDate = editAssessmentSD.getText().toString();
        String assessmentDueDate = editAssessmentGD.getText().toString();
        String assessmentType = this.assessmentType;

        if ( assessmentTitle.trim().isEmpty() || assessmentStartDate.trim().isEmpty() || assessmentDueDate.trim().isEmpty()) {
            Toast.makeText(this, "No fields can be left empty, get that sorted please!", Toast.LENGTH_LONG).show();
            return;
        }

        Assessments assessmentsToSave = new Assessments(assessmentTitle, assessmentStartDate, assessmentDueDate, assessmentType, courseID);

        if (editMode) {
            assessmentsToSave.setId(getIntent().getIntExtra(EXTRA_ID, -1));
            if (assessmentsToSave.getId() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Save Edits To Assessment?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage("Are you sure you wish to save edits to this particular Assessment?"
                        + " Do you wish to proceed?");
                builder.setPositiveButton("Yes", (dialog, id) ->
                {
                    dialog.dismiss();
                    schedulerVM.update(assessmentsToSave);
                    Intent intent = new Intent(this, AssessmentsActivity.class);
                    this.startActivity(intent);
                    finish();
                });

                builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add New Assessment?");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setMessage("Are you sure you wish to add this new Assessment?"
                    + " Do you wish to proceed?");
            builder.setPositiveButton("Yes", (dialog, id) ->
            {
                dialog.dismiss();
                schedulerVM.insert(assessmentsToSave);
                Intent intent = new Intent(this, AssessmentsActivity.class);
                this.startActivity(intent);
                finish();
            });

            builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    /**
     * Click handler for when the user hits the cancel button, redirecting back to the Assessment list page.
     * @param view = instance of View.
     */
    public void cancelButtonClicked(View view) {
        Intent intent = new Intent(this, AssessmentsActivity.class);
        startActivity(intent);
    }

}
