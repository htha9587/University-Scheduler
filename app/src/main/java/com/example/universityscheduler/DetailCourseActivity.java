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
import com.example.universityscheduler.Model.Terms;
import com.example.universityscheduler.Util.AlarmsReceiver;
import com.example.universityscheduler.ViewModel.SchedulerVM;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Controller class for the Detailed view of a course. From here, the user can read data
 * about the course, its term and assessment details, remove a course, or go to the add/edit activity if he/she chooses.
 */
public class DetailCourseActivity extends AppCompatActivity {

    private static int COURSE_SD_NOTIFICATION_ID;
    private static int COURSE_ED_NOTIFICATION_ID;
    private static final String primary_notification_channel = "primary_notification_channel";
    private NotificationManager notificationManager;
    boolean sdAlarmInit;
    boolean edAlarmInit;
    PendingIntent sdPendingIntent;
    PendingIntent edPendingIntent;
    Courses course;
    int courseID;
    SchedulerVM schedulerVM;
    private TextView detailCourseTitle;
    private TextView detailCourseSD;
    private TextView detailCourseED;
    private TextView detailCourseTerm;
    private TextView detailCourseStatus;
    private TextView detailCourseAssessments;
    private TextView detailCourseNotes;
    private TextView instructorNameDetail;
    private TextView instructorPhoneDetail;
    private TextView instructorEmailDetail;
    public static final String EXTRA_ID = "com.example.universityscheduler.EXTRA_ID";
    public static final String NOTIFICATION_COURSE_TITLE = "com.example.universityscheduler.NOTIFICATION_COURSE_TITLE";
    public static final String NOTIFICATION_COURSE_TEXT = "com.example.universityscheduler.NOTIFICATION_COURSE_TEXT";
    private NotificationManagerCompat notificationManagerCompat;

    /**
     * Loads the XML and binds the Courses attributes to the UI. In addition, loads the alarms for the course start/end dates.
     * @param savedInstanceState = instance of Bundle.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        final ToggleButton courseSDAlert = findViewById(R.id.courseSDAlertTrigger);
        final ToggleButton courseEDAlert = findViewById(R.id.courseEDAlertTrigger);

        Intent sdNotifyIntent = new Intent(DetailCourseActivity.this, AlarmsReceiver.class);
        Intent edNotifyIntent = new Intent(DetailCourseActivity.this, AlarmsReceiver.class);
        Intent intent = getIntent();

        courseID = intent.getIntExtra(EXTRA_ID, -1);
        COURSE_SD_NOTIFICATION_ID = courseID;
        COURSE_ED_NOTIFICATION_ID = courseID + 1000;
        sdNotifyIntent.putExtra("notification_id", COURSE_SD_NOTIFICATION_ID);
        edNotifyIntent.putExtra("notification_id", COURSE_ED_NOTIFICATION_ID);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            sdAlarmInit = (PendingIntent.getBroadcast(this, COURSE_SD_NOTIFICATION_ID,
                    sdNotifyIntent, PendingIntent.FLAG_IMMUTABLE) != null);
        }
        else
        {
            sdAlarmInit = (PendingIntent.getBroadcast(this, COURSE_SD_NOTIFICATION_ID,
                    sdNotifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            edAlarmInit = (PendingIntent.getBroadcast(this, COURSE_ED_NOTIFICATION_ID,
                    edNotifyIntent, PendingIntent.FLAG_IMMUTABLE) != null);
        }
        else {
            edAlarmInit = (PendingIntent.getBroadcast(this, COURSE_ED_NOTIFICATION_ID,
                    edNotifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        }

        courseSDAlert.setChecked(sdAlarmInit);
        courseEDAlert.setChecked(edAlarmInit);

       final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //ChangeListener/onClick for the Course Start Date.
        courseSDAlert.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (courseSDAlert.isChecked()) {

                            String notificationTitle = "Course Start Date Reminder";
                            String notificationText = "Your course '" + course.getCourseTitle() + "' has just begun. Keep up the good work!";

                            sdNotifyIntent.putExtra("mNotificationTitle", notificationTitle);
                            sdNotifyIntent.putExtra("mNotificationText", notificationText);

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                            {
                                sdPendingIntent = PendingIntent.getBroadcast
                                        (DetailCourseActivity.this, COURSE_SD_NOTIFICATION_ID, sdNotifyIntent, PendingIntent.FLAG_IMMUTABLE);
                            }
                            else
                            {
                                sdPendingIntent = PendingIntent.getBroadcast
                                        (DetailCourseActivity.this, COURSE_SD_NOTIFICATION_ID, sdNotifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            }

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, 8);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            String[] courseSDArray = detailCourseSD.getText().toString().split("/");
                            calendar.set(Calendar.MONTH, Integer.parseInt(courseSDArray[0]) - 1);
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(courseSDArray[1]));
                            calendar.set(Calendar.YEAR, Integer.parseInt(courseSDArray[2]));

                            if (alarmManager != null) {
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sdPendingIntent);
                            }

                            //Toast alert for when the Course Start Date alert button is on.
                            String sdAlertToast = "The Course start alert will show up on the start date. ";
                            Toast.makeText(DetailCourseActivity.this, sdAlertToast, Toast.LENGTH_LONG).show();

                        } else {

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                            {
                                sdPendingIntent = PendingIntent.getBroadcast
                                        (DetailCourseActivity.this, COURSE_SD_NOTIFICATION_ID, sdNotifyIntent, PendingIntent.FLAG_IMMUTABLE);
                            }
                            else
                            {
                                sdPendingIntent = PendingIntent.getBroadcast
                                        (DetailCourseActivity.this, COURSE_SD_NOTIFICATION_ID, sdNotifyIntent, PendingIntent.FLAG_NO_CREATE);
                            }

                            sdPendingIntent.cancel();

                            if (alarmManager != null) {
                                alarmManager.cancel(sdPendingIntent);
                            }

                            //Toast alert for when the Course Start Date alert button is off.
                            String sdAlertToast = "The Course start date alert has been disabled.";
                            Toast.makeText(DetailCourseActivity.this, sdAlertToast, Toast.LENGTH_LONG).show();
                        }
                }
                });

        //ChangeListener/onClick for the Course End Date.
        courseEDAlert.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (courseEDAlert.isChecked()) {

                            String notificationTitle = "Course Due Date Reminder";
                            String notificationText = "Your course '" + course.getCourseTitle() + "' is due today. Keep up the good work!";

                            edNotifyIntent.putExtra("mNotificationTitle", notificationTitle);
                            edNotifyIntent.putExtra("mNotificationText", notificationText);

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                            {
                                edPendingIntent = PendingIntent.getBroadcast
                                        (DetailCourseActivity.this, COURSE_ED_NOTIFICATION_ID, edNotifyIntent, PendingIntent.FLAG_IMMUTABLE);
                            }
                            else
                            {
                                edPendingIntent = PendingIntent.getBroadcast
                                        (DetailCourseActivity.this, COURSE_ED_NOTIFICATION_ID, edNotifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            }

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, 8);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            String[] courseEDArray = detailCourseED.getText().toString().split("/");
                            calendar.set(Calendar.MONTH, Integer.parseInt(courseEDArray[0]) - 1);
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(courseEDArray[1]));
                            calendar.set(Calendar.YEAR, Integer.parseInt(courseEDArray[2]));

                            if (alarmManager != null) {
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), edPendingIntent);
                            }

                            //Toast alert for when the Course End Date alert button is on.
                            String edAlertToast = "The Course end alert will show up on the end date. ";
                            Toast.makeText(DetailCourseActivity.this, edAlertToast, Toast.LENGTH_LONG).show();

                        } else {

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                            {
                                edPendingIntent = PendingIntent.getBroadcast
                                        (DetailCourseActivity.this, COURSE_ED_NOTIFICATION_ID, edNotifyIntent, PendingIntent.FLAG_IMMUTABLE);
                            }
                            else
                            {
                                edPendingIntent = PendingIntent.getBroadcast
                                        (DetailCourseActivity.this, COURSE_ED_NOTIFICATION_ID, edNotifyIntent, PendingIntent.FLAG_NO_CREATE);
                            }

                            edPendingIntent.cancel();

                            if (alarmManager != null) {
                                alarmManager.cancel(edPendingIntent);
                            }

                            //Toast alert for when the Course End Date alert button is off.
                            String edAlertToast = "The Course end date alert has been disabled.";
                            Toast.makeText(DetailCourseActivity.this, edAlertToast, Toast.LENGTH_LONG).show();
                        }
                    }
                });

        detailCourseTitle = findViewById(R.id.detailCourseTitle);
        detailCourseSD = findViewById(R.id.detailCourseSD);
        detailCourseED = findViewById(R.id.detailCourseED);
        detailCourseTerm = findViewById(R.id.detailCourseTerm);
        detailCourseStatus = findViewById(R.id.detailCourseStatus);
        detailCourseNotes = findViewById(R.id.detailCourseNotes);
        detailCourseAssessments = findViewById(R.id.detailCourseAssessments);
        instructorNameDetail = findViewById(R.id.instructorNameDetail);
        instructorPhoneDetail = findViewById(R.id.instructorPhoneDetail);
        instructorEmailDetail = findViewById(R.id.instructorEmailDetail);

        schedulerVM = new ViewModelProvider(this).get(SchedulerVM.class);

        //Sets the text for each of the Course attributes.
        if (courseID > 0) {
            schedulerVM.getAllCourses().observe(this, new Observer<List<Courses>>() {
                @Override
                public void onChanged(@Nullable List<Courses> courses) {
                    assert courses != null;
                    for (Courses coursesIDChecking : courses) {
                        if (coursesIDChecking.getId() == courseID) {
                            course = coursesIDChecking;
                            detailCourseTitle.setText(course.getCourseTitle());
                            detailCourseSD.setText(course.getCourseStartDate());
                            detailCourseED.setText(course.getCourseEndDate());
                            detailCourseStatus.setText(course.getCoursesStatus());
                            detailCourseNotes.setText(course.getCourseNotes());
                            instructorNameDetail.setText(course.getInstructorName());
                            instructorPhoneDetail.setText(course.getInstructorPhone());
                            instructorEmailDetail.setText(course.getInstructorEmail());
                            initTermTextView();
                            detailCourseAssessments.setText("");
                            initAssessmentTextView();
                        }
                    }
                }
            });
        }
    }


    /**
     * Sets the Term TextView by checking that the term ID and course term ID match.
     */
    private void initTermTextView() {
        schedulerVM.getAllTerms().observe(this, new Observer<List<Terms>>() {
            @Override
            public void onChanged(@Nullable List<Terms> terms) {
                assert terms != null;
                for (Terms term : terms) {
                    if (course.getTermId() == term.getId()) {
                        String termTitle = term.getTermTitle();
                        detailCourseTerm.setText(termTitle);
                    }
                }
            }
        });
    }

    /**
     * Sets the Assessment TextView by checking that the course ID matches and checks whether it's a
     * performance assessment or objective assessment.
     */
    private void initAssessmentTextView() {

        final List<Assessments> assessmentsList = new ArrayList<>();
        schedulerVM.getAllAssessments().observe(this, new Observer<List<Assessments>>() {
            @Override
            public void onChanged(@Nullable List<Assessments> assessments) {
                assert assessments != null;
                for (Assessments assessment : assessments) {
                    if (assessment.getCourseId() == courseID) {
                        assessmentsList.add(assessment);
                    }
                }
                StringBuilder assessListFormatted = new StringBuilder();
                for (Assessments assessment : assessmentsList) {
                    String assessmentType;
                    if (assessment.getAssessmentType().equals("Performance Assessment")) {
                        assessmentType = "Performance Assessment";
                    } else {
                        assessmentType = "Objective Assessment";
                    }
                    assessListFormatted.append(assessment.getAssessmentTitle() + " " + assessmentType);
                    assessListFormatted.append("\n");
                }
                if (assessListFormatted.toString().isEmpty()) {
                    assessListFormatted.append("This course has no assessments!");
                }
                detailCourseAssessments.setText(assessListFormatted.toString());
            }
        });
    }

    /**
     * Click handler for when the user clicks the Share Course Notes button.
     * @param view = instance of View.
     */
    @SuppressLint("QueryPermissionsNeeded")
    public void shareCourseNotesClicked(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, course.getCourseNotes());
        intent.setType("text/plain");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Click handler to open the Create/Edit Course page when the user clicks.
     * @param view = instance of View.
     */
    public void editCourseClicked(View view) {
        Intent intent = new Intent(this, EditCourseActivity.class);
        intent.putExtra(DetailCourseActivity.EXTRA_ID, course.getId());
        intent.putExtra(EditCourseActivity.EXTRA_TITLE, course.getCourseTitle());
        intent.putExtra(EditCourseActivity.EXTRA_START_DATE, course.getCourseStartDate());
        intent.putExtra(EditCourseActivity.EXTRA_END_DATE, course.getCourseEndDate());
        intent.putExtra(EditCourseActivity.EXTRA_STATUS, course.getCoursesStatus());
        intent.putExtra(EditCourseActivity.EXTRA_NAME, course.getInstructorName());
        intent.putExtra(EditCourseActivity.EXTRA_PHONE, course.getInstructorPhone());
        intent.putExtra(EditCourseActivity.EXTRA_EMAIL, course.getInstructorEmail());
        intent.putExtra(EditCourseActivity.EXTRA_NOTES, course.getCourseNotes());
        intent.putExtra(EditCourseActivity.EXTRA_TERM_ID, course.getTermId());
        startActivity(intent);
    }

    /**
     * Click handler to delete the Course from the RecyclerView and Room Database.
     * @param view = instance of View.
     */
    public void removeCourseClicked(View view) {
        if (detailCourseAssessments.getText().toString().equals("This course has no assessments!")) {
            Courses coursesToDelete = new Courses(-1, "", "", "", "", "", "", "", "");
            coursesToDelete.setId(getIntent().getIntExtra(EXTRA_ID, -1));
            if (coursesToDelete.getId() > 0) {
                schedulerVM = new ViewModelProvider(this).get(SchedulerVM.class);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Course?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage("Are you sure you wish to delete this particular course?"
                        + " Do you wish to proceed?");
                builder.setPositiveButton("Yes", (dialog, id) ->
                {
                    dialog.dismiss();
                    schedulerVM.delete(coursesToDelete);
                    Intent intent = new Intent(this, CoursesActivity.class);
                    this.startActivity(intent);
                    finish();
                });

                builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        } else {
            Toast.makeText(this, "You can't delete this course because there are assessment(s) still assigned to it!", Toast.LENGTH_LONG).show();
            return;
        }
    }

}
