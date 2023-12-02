package com.example.universityscheduler.Util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.example.universityscheduler.EditAssessmentActivity;
import com.example.universityscheduler.EditCourseActivity;
import com.example.universityscheduler.EditTermActivity;
import java.util.Calendar;

/**
 * Subclass that deals with the Date dialog pickers for the Terms, Courses, and Assessments.
 */
public class DPFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private String dateTag;

    /**
     * Constructor for the fragment class.
     * @param dateTag = tag of the selected date.
     */
    public DPFragment(String dateTag) {
        this.dateTag = dateTag;
    }

    /**
     * Sets the Date for the add/edit Term, Course, and Assessment activities.
     * @param datePicker = instance of the DatePicker object.
     * @param year = Year for the date.
     * @param month = Month for the date.
     * @param day = Day for the date.
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        switch (requireActivity().getLocalClassName()) {
            case "EditAssessmentActivity":
                ((EditAssessmentActivity) requireActivity()).processDatePickerResult(year, month, day, this.dateTag);
                break;
            case "EditCourseActivity":
                ((EditCourseActivity) requireActivity()).processDatePickerResult(year, month, day, this.dateTag);
                break;
            case "EditTermActivity":
                ((EditTermActivity) requireActivity()).processDatePickerResult(year, month, day, this.dateTag);
                break;
        }
    }

    /**
     * Creates the dialog for the date pickers. Used in each of the add/edit activities.
     * @param savedInstanceState = instance of Bundle.
     * @return = The Date picker dialog with its attributes.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

}
