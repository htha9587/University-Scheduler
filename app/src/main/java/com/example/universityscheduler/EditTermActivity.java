package com.example.universityscheduler;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.universityscheduler.Model.Terms;
import com.example.universityscheduler.Util.DPFragment;
import com.example.universityscheduler.ViewModel.SchedulerVM;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Controller class for Adding/editing University terms. From here, the user can add a new
 * Term to the RecyclerView/Room Database, or edit and save details
 * about an existing term.
 */
public class EditTermActivity extends AppCompatActivity {

    private SchedulerVM schedulerVM;
    private EditText termEditTitle;
    private TextView termEditSD;
    private TextView termEditED;
    public static final String EXTRA_ID = "com.example.universityscheduler.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.universityscheduler.EXTRA_TITLE";
    public static final String EXTRA_START_DATE = "com.example.universityscheduler.EXTRA_START_DATE";
    public static final String EXTRA_END_DATE = "com.example.universityscheduler.EXTRA_END_DATE";
    private boolean editMode = false;

    /**
     * Loads the XML and binds the Term attributes to the UI.
     * @param savedInstanceState = instance of Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        termEditTitle = findViewById(R.id.editTermName);
        termEditSD = findViewById(R.id.editTermStartDate);
        termEditED = findViewById(R.id.editTermEndDate);

        //Sets the text for the term attributes if the user clicks the edit term button.
        if (intent.getExtras() != null) {
            editMode = true;
            setTitle("Term Edit");
            termEditTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            termEditSD.setText(intent.getStringExtra(EXTRA_START_DATE));
            termEditED.setText(intent.getStringExtra(EXTRA_END_DATE));
        }
    }

    /**
     * Date Picker click handler for the Term Start date.
     */
    public void termStartDateClicked(View view) {
        DialogFragment newFragment = new DPFragment("startDate");
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    /**
     * Date Picker click handler for the Term End date.
     */
    public void termEndDateClicked(View view) {
        DialogFragment newFragment = new DPFragment("endDate");
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    /**
     * Processes the date picker selections and prints the date to the start or end date.
     * @param year = calendar year
     * @param month = calendar month
     * @param day = calendar day
     * @param tag = String type variable for DateTag
     */
    public void processDatePickerResult(int year, int month, int day, String tag) {

        String monthString = Integer.toString(month+1);
        String dayString = Integer.toString(day);
        String yearString = Integer.toString(year);
        String dateMessage = (monthString +
                "/" + dayString + "/" + yearString);
        TextView dateSetter;

        if (tag.equals("startDate")) {
            dateSetter = findViewById(R.id.editTermStartDate);
        } else if (tag.equals("endDate")) {
            dateSetter = findViewById(R.id.editTermEndDate);
        } else {
            dateSetter = null;
        }

        if(dateSetter != null) {
            dateSetter.setText(dateMessage);
        }

    }

    /**
     * Click handler that either Saves a new Term, or updates info from a preexisting one.
     * @param view = instance of View.
     */
    public void termSaveButtonClicked(View view) {

        String termTitle = termEditTitle.getText().toString();
        String termStartDate = termEditSD.getText().toString();
        String termEndDate = termEditED.getText().toString();

        if (termTitle.trim().isEmpty() || termStartDate.trim().isEmpty() || termEndDate.trim().isEmpty()) {
            Toast.makeText(this, "No fields can be left empty, get that sorted please!", Toast.LENGTH_LONG).show();
            return;
        }

        //Parses through the start and end dates of the term.
        DateTimeFormatter dateTimeFormatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        }

        LocalDate sdParsed = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sdParsed = LocalDate.parse(termStartDate, dateTimeFormatter);
        }

        LocalDate edParsed = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            edParsed = LocalDate.parse(termEndDate, dateTimeFormatter);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (sdParsed.isAfter(edParsed)) {
                Toast.makeText(this, "Please make sure the Term end date is later than that of the start date!", Toast.LENGTH_LONG).show();
                return;
            }
        }

        schedulerVM = new ViewModelProvider(this).get(SchedulerVM.class);

        Terms termsToSave = new Terms(termTitle, termStartDate, termEndDate);

        //Alert dialogs for editing vs adding a new term.
        if (editMode) {
            termsToSave.setId(getIntent().getIntExtra(EXTRA_ID, -1));
            if (termsToSave.getId() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Save Edits To Term?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage("Are you sure you wish to save edits to this particular term?"
                        + " Do you wish to proceed?");
                builder.setPositiveButton("Yes", (dialog, id) ->
                {
                    dialog.dismiss();
                    schedulerVM.update(termsToSave);
                    Intent intent = new Intent(this, TermsActivity.class);
                    this.startActivity(intent);
                    finish();
                });

                builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add New Term?");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setMessage("Are you sure you wish to add this new Term? "
                    + " Do you wish to proceed?");
            builder.setPositiveButton("Yes", (dialog, id) ->
            {
                dialog.dismiss();
                schedulerVM.insert(new Terms(termTitle, termStartDate, termEndDate));
                Intent intent = new Intent(this, TermsActivity.class);
                this.startActivity(intent);
                finish();
            });

            builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    /**
     * Click handler for when the user hits the cancel button, redirecting back to the Term list page.
     * @param view = instance of View.
     */
    public void termCancelClicked(View view) {
        Intent intent = new Intent(this, TermsActivity.class);
        startActivity(intent);
    }

}
