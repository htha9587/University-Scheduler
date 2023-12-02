package com.example.universityscheduler.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.universityscheduler.Model.Assessments;
import com.example.universityscheduler.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for the Assessments RecyclerView object.
 */
public class AssessmentsAdapter extends RecyclerView.Adapter {

    private OnItemClickListener onItemClickListener;
    private List<Assessments> assessmentsList = new ArrayList<>();

    /**
     * Initializes the Assessments RecyclerVIew.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessments_item, parent, false);
        return new AssessmentHolder(itemView);
    }

    /**
     * Binds the RecyclerView and loads it up with the entries from the assessments ArrayList.
     * @param holder = instance of the ViewHolder object.
     * @param position = the selected item in the RecycleView.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AssessmentHolder assessmentHolder = (AssessmentHolder) holder;
        Assessments currentAssessments = assessmentsList.get(position);
        assessmentHolder.textViewAssessmentTitle.setText(currentAssessments.getAssessmentTitle());
        assessmentHolder.textViewAssessmentStartDate.setText(currentAssessments.getAssessmentStartDate());
        assessmentHolder.textViewAssessmentDueDate.setText(currentAssessments.getAssessmentEndDate());
    }

    /**
     * Getter for the size of the Assessments RecyclerView.
     * @return = the size of the assessment list.
     */
    @Override
    public int getItemCount() {
        return assessmentsList.size();
    }

    /**
     * Setter for the size of the Assessments RecyclerView.
     * @param assessmentsList = sets the courseList.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setAssessmentsList(List<Assessments> assessmentsList) {
        this.assessmentsList = assessmentsList;
        notifyDataSetChanged();
    }

    /**
     * Superclass for the Assessments RecyclerView.
     */
    class AssessmentHolder extends RecyclerView.ViewHolder {
        private TextView textViewAssessmentTitle;
        private TextView textViewAssessmentStartDate;
        private TextView textViewAssessmentDueDate;

        //Links the assessment title and end date text views to the Superclass data members.
        public AssessmentHolder(View view) {
            super(view);
            textViewAssessmentTitle = view.findViewById(R.id.assessmentName);
            textViewAssessmentStartDate = view.findViewById(R.id.assessmentStartDate);
            textViewAssessmentDueDate = view.findViewById(R.id.assessmentEndDate);

            //OnClick listener for when the user clicks an assessment in the assessment RecyclerView list.
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View viewer) {
                    int position = getAdapterPosition();
                    if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(assessmentsList.get(position));
                    }
                }
            });
        }
    }

    /**
     * Setter for the onClick listener when the user clicks an assessment in the RecyclerView.
     * @param onItemClickListener = instance of the OnItemClickListener.
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Interface that calls the item click listener when clicked.
     */
    public interface OnItemClickListener {
        void onItemClick(Assessments assessments);
    }

}
