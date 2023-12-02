package com.example.universityscheduler;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.universityscheduler.Model.Assessments;
import com.example.universityscheduler.Model.Courses;
import com.example.universityscheduler.Util.AlarmsReceiver;
import com.example.universityscheduler.ViewModel.SchedulerVM;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Controller class for the Detailed view of an assessment. From here, the user can read data
 * about the assessment, the course it's apart of, remove an assessment, or go to the add/edit activity if he/she chooses.
 */
public class DetailAssessmentActivity extends AppCompatActivity {

    private static int ASSESSMENT_ED_NOTIFICATION_ID;
    private static int ASSESSMENT_SD_NOTIFICATION_ID;
    private static final String primary_notification_channel = "primary_notification_channel";
    private NotificationManager notificationManager;
    boolean alarmInit;
    boolean sdAlarmInit;
    PendingIntent startDatePendingIntent;
    PendingIntent endDatePendingIntent;
    SchedulerVM schedulerVM;
    Assessments assessments;
    int assessmentID;
    private TextView detailAssessmentTitle;
    private TextView detailAssessmentSD;
    private TextView detailAssessmentGD;
    private TextView detailAssessmentType;
    private TextView detailAssessmentCourse;
    public static final String EXTRA_ID = "com.example.universityscheduler.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.universityscheduler.EXTRA_TITLE";
    public static final String EXTRA_START_DATE = "com.example.universityscheduler.EXTRA_START_DATE";
    public static final String EXTRA_DUE_DATE = "com.example.universityscheduler.EXTRA_DUE_DATE";
    public static final String EXTRA_TYPE = "com.example.universityscheduler.EXTRA_TYPE";
    public static final String EXTRA_COURSE_ID = "com.example.universityscheduler.EXTRA_COURSE_ID";
    private NotificationManagerCompat notificationManagerCompat;

    /**
     * Loads the XML and binds the Assessments attributes to the UI.
     * @param savedInstanceState = instance of Bundle.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"UnspecifiedImmutableFlag", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        //Has the alarm toggle button set up.
        final ToggleButton assessmentSDToggle = findViewById(R.id.assessmentSDAlert);
        final ToggleButton assessmentAlarmToggle = findViewById(R.id.assessmentGDAlert);

        Intent notificationIntent = new Intent(DetailAssessmentActivity.this, AlarmsReceiver.class);
        Intent sdNotificationIntent = new Intent(DetailAssessmentActivity.this, AlarmsReceiver.class);
        Intent intent = getIntent();

        assessmentID = intent.getIntExtra(EXTRA_ID, -1);
        ASSESSMENT_SD_NOTIFICATION_ID = assessmentID;
        ASSESSMENT_ED_NOTIFICATION_ID = assessmentID + 10000;
        sdNotificationIntent.putExtra("notification_id", ASSESSMENT_SD_NOTIFICATION_ID);
        notificationIntent.putExtra("notification_id", ASSESSMENT_ED_NOTIFICATION_ID);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            alarmInit = (PendingIntent.getBroadcast(this, ASSESSMENT_ED_NOTIFICATION_ID,
                    notificationIntent, PendingIntent.FLAG_IMMUTABLE) != null);
        }
        else
        {
            alarmInit = (PendingIntent.getBroadcast(this, ASSESSMENT_ED_NOTIFICATION_ID,
                    notificationIntent, PendingIntent.FLAG_NO_CREATE) != null);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            sdAlarmInit = (PendingIntent.getBroadcast(this, ASSESSMENT_SD_NOTIFICATION_ID,
                    sdNotificationIntent, PendingIntent.FLAG_IMMUTABLE) != null);
        }
        else
        {
            sdAlarmInit = (PendingIntent.getBroadcast(this, ASSESSMENT_SD_NOTIFICATION_ID,
                    sdNotificationIntent, PendingIntent.FLAG_NO_CREATE) != null);
        }

        assessmentAlarmToggle.setChecked(alarmInit);
        assessmentSDToggle.setChecked(sdAlarmInit);

        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //Change listener/onClick for the assessment alarm button.
        assessmentAlarmToggle.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (assessmentAlarmToggle.isChecked()) {

                            String notificationTitle = "Assessment Due Date Reminder";
                            String notificationText = "Your assessment '" + assessments.getAssessmentTitle() + "' is due today. Keep up the good work!";

                            notificationIntent.putExtra("mNotificationTitle", notificationTitle);
                            notificationIntent.putExtra("mNotificationText", notificationText);

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                endDatePendingIntent = PendingIntent.getBroadcast
                                        (DetailAssessmentActivity.this, ASSESSMENT_ED_NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
                            }
                            else {
                                endDatePendingIntent = PendingIntent.getBroadcast
                                        (DetailAssessmentActivity.this, ASSESSMENT_ED_NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            }


                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, 8);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            String[] assessmentEDArray = detailAssessmentGD.getText().toString().split("/");
                            calendar.set(Calendar.MONTH, Integer.parseInt(assessmentEDArray[0]) - 1);
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(assessmentEDArray[1]));
                            calendar.set(Calendar.YEAR, Integer.parseInt(assessmentEDArray[2]));

                            if (alarmManager != null) {
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), endDatePendingIntent);
                            }

                            //Outputs the toast alert for the assessment due/goal date.
                            String assessmentAlarmToast = "The Assessment due alert will show up on the end date. ";
                            Toast.makeText(DetailAssessmentActivity.this, assessmentAlarmToast, Toast.LENGTH_LONG).show();

                        } else {

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                            {
                                endDatePendingIntent = PendingIntent.getBroadcast
                                        (DetailAssessmentActivity.this, ASSESSMENT_ED_NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
                            }
                            else
                            {
                                endDatePendingIntent = PendingIntent.getBroadcast
                                        (DetailAssessmentActivity.this, ASSESSMENT_ED_NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_NO_CREATE);
                            }

                            endDatePendingIntent.cancel();

                            if (alarmManager != null) {
                                alarmManager.cancel(endDatePendingIntent);
                            }

                            //Outputs the toast alert for the assessment due/goal date.
                            String assessmentAlarmToast = "The Assessment due/goal date alert has been disabled.";
                            Toast.makeText(DetailAssessmentActivity.this, assessmentAlarmToast, Toast.LENGTH_LONG).show();
                        }
                    }
                });

        //Change listener/onClick for the assessment start date alarm button.
        assessmentSDToggle.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (assessmentSDToggle.isChecked()) {

                            String notificationTitle = "Assessment Start Date Reminder";
                            String notificationText = "Your assessment '" + assessments.getAssessmentTitle() + "' has just begun. Keep up the good work!";

                            sdNotificationIntent.putExtra("mNotificationTitle", notificationTitle);
                            sdNotificationIntent.putExtra("mNotificationText", notificationText);

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                            {
                                startDatePendingIntent = PendingIntent.getBroadcast(DetailAssessmentActivity.this, ASSESSMENT_SD_NOTIFICATION_ID, sdNotificationIntent, PendingIntent.FLAG_IMMUTABLE);
                            }

                            else {
                                startDatePendingIntent = PendingIntent.getBroadcast(DetailAssessmentActivity.this, ASSESSMENT_SD_NOTIFICATION_ID, sdNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            }

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, 8);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            String[] assessmentSDArray = detailAssessmentSD.getText().toString().split("/");
                            calendar.set(Calendar.MONTH, Integer.parseInt(assessmentSDArray[0]) - 1);
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(assessmentSDArray[1]));
                            calendar.set(Calendar.YEAR, Integer.parseInt(assessmentSDArray[2]));

                            if (alarmManager != null) {
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), startDatePendingIntent);
                            }

                            //Outputs the toast alert for the assessment due/goal date.
                            String assessmentSDAlarmToast = "The Assessment start alert will show up on the start date. ";
                            Toast.makeText(DetailAssessmentActivity.this, assessmentSDAlarmToast, Toast.LENGTH_LONG).show();

                        } else {

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                            {
                                startDatePendingIntent = PendingIntent.getBroadcast(DetailAssessmentActivity.this, ASSESSMENT_SD_NOTIFICATION_ID, sdNotificationIntent, PendingIntent.FLAG_IMMUTABLE);
                            }
                            else
                            {
                                startDatePendingIntent = PendingIntent.getBroadcast(DetailAssessmentActivity.this, ASSESSMENT_SD_NOTIFICATION_ID, sdNotificationIntent, PendingIntent.FLAG_NO_CREATE);
                            }

                            startDatePendingIntent.cancel();


                            if (alarmManager != null) {
                                alarmManager.cancel(startDatePendingIntent);
                            }

                            //Outputs the toast alert for the assessment start date.
                            String assessmentSDAlarmToast = "The Assessment start date alert has been disabled.";
                            Toast.makeText(DetailAssessmentActivity.this, assessmentSDAlarmToast, Toast.LENGTH_LONG).show();
                        }
                    }
                });

        //Binds the XML ids together with the TextViews.
        detailAssessmentTitle = findViewById(R.id.detailAssessmentTitle);
        detailAssessmentSD = findViewById(R.id.detailAssessmentSD);
        detailAssessmentGD = findViewById(R.id.detailAssessmentGD);
        detailAssessmentType = findViewById(R.id.detailAssessmentType);
        detailAssessmentCourse = findViewById(R.id.detailAssessmentCourse);

        schedulerVM = new ViewModelProvider(this).get(SchedulerVM.class);

        //Gathers the results from the assessment attributes and sets them.
        if (intent.getExtras() != null) {
            detailAssessmentTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            detailAssessmentSD.setText(intent.getStringExtra(EXTRA_START_DATE));
            detailAssessmentGD.setText(intent.getStringExtra(EXTRA_DUE_DATE));
            String assessmentTypeUnformatted = intent.getStringExtra(EXTRA_TYPE);
            //String assessmentTypeFormatted = assessmentTypeUnformatted.substring(0, 1).toUpperCase() + " " + assessmentTypeUnformatted.substring(1);
            detailAssessmentType.setText(assessmentTypeUnformatted);
            final int courseID = intent.getIntExtra(EXTRA_COURSE_ID, -1);

            //Loop that goes through a course ID that matches the given assessment then sets the course TextView.
            if (courseID > 0) {
                schedulerVM.getAllCourses().observe(this, new Observer<List<Courses>>() {
                    @Override
                    public void onChanged(@Nullable List<Courses> courses) {
                        assert courses != null;
                        for (Courses course : courses) {
                            if (course.getId() == courseID) {
                                detailAssessmentCourse.setText(course.getCourseTitle());
                            }
                        }
                    }
                });

                //Loop that goes through an assessment ID that matches the given assessment.
                if (assessmentID > 0) {
                    schedulerVM.getAllAssessments().observe(this, new Observer<List<Assessments>>() {
                        @Override
                        public void onChanged(@Nullable List<Assessments> assessment) {
                            assert assessment != null;
                            for (Assessments assessmentsIDChecking : assessment) {
                                if (assessmentsIDChecking.getId() == assessmentID) {
                                    assessments = assessmentsIDChecking;
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    /**
     * Click handler to open the Create/Edit Assessment page when the user clicks.
     * @param view = instance of View.
     */
    public void editAssessmentClicked(View view) {
        Intent intent = new Intent(this, EditAssessmentActivity.class);
        intent.putExtra(EditAssessmentActivity.EXTRA_ID, getIntent().getIntExtra(EXTRA_ID, -1));
        intent.putExtra(EditAssessmentActivity.EXTRA_TITLE, getIntent().getStringExtra(EXTRA_TITLE));
        intent.putExtra(EditAssessmentActivity.EXTRA_START_DATE, getIntent().getStringExtra(EXTRA_START_DATE));
        intent.putExtra(EditAssessmentActivity.EXTRA_DUE_DATE, getIntent().getStringExtra(EXTRA_DUE_DATE));
        intent.putExtra(EditAssessmentActivity.EXTRA_TYPE, getIntent().getStringExtra(EXTRA_TYPE));
        intent.putExtra(EditAssessmentActivity.EXTRA_COURSE_ID, getIntent().getIntExtra(EXTRA_COURSE_ID, -1));
        startActivity(intent);
    }

    /**
     * Click handler to delete the Assessment from the RecyclerView and Room Database.
     * @param view = instance of View.
     */
    public void removeAssessmentClicked(View view) {
        Assessments assessmentsToDelete = new Assessments("", "", "","", -1);
        assessmentsToDelete.setId(getIntent().getIntExtra(EXTRA_ID, -1));
        if (assessmentsToDelete.getId() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Assessment?");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setMessage("Are you sure you wish to delete this particular assessment?"
                    + " Do you wish to proceed?");
            builder.setPositiveButton("Yes", (dialog, id) ->
            {
                dialog.dismiss();
                schedulerVM.delete(assessmentsToDelete);
                Intent intent = new Intent(this, AssessmentsActivity.class);
                this.startActivity(intent);
                finish();
            });

            builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

}
