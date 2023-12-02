package com.example.universityscheduler.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.universityscheduler.Model.Courses;
import com.example.universityscheduler.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for the Courses RecyclerView object.
 */
public class CoursesAdapter extends RecyclerView.Adapter {

    private OnItemClickListener onItemClickListener;
    private List<Courses> coursesList = new ArrayList<>();

    /**
     * Initializes the Courses RecyclerVIew.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_item, parent, false);
        return new CourseHolder(itemView);
    }

    /**
     * Binds the RecyclerView and loads it up with the entries from the courses ArrayList.
     * @param holder = instance of the ViewHolder object.
     * @param position = the selected item in the RecycleView.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CourseHolder courseHolder = (CourseHolder) holder;
        Courses currentCourses = coursesList.get(position);
        courseHolder.textViewCourseTitle.setText(currentCourses.getCourseTitle());
    }

    /**
     * Getter for the size of the Courses RecyclerView.
     * @return = the size of the course list.
     */
    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    /**
     * Setter for the size of the Courses RecyclerView.
     * @param coursesList sets the courseList.
     */
    public void setCourses(List<Courses> coursesList) {
        this.coursesList = coursesList;
        notifyDataSetChanged();
    }

    /**
     * Superclass for the Courses RecyclerView.
     */
    class CourseHolder extends RecyclerView.ViewHolder {
        private TextView textViewCourseTitle;

        //Links the course title text view to the textViewCourseTitle.
        public CourseHolder(View view) {
            super(view);
            textViewCourseTitle = view.findViewById(R.id.courseTitle);

            //OnClick listener for when the user clicks a course in the course RecyclerView list.
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View viewer) {
                    int position = getAdapterPosition();
                    if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(coursesList.get(position));
                    }
                }
            });
        }
    }

    /**
     * Setter for the onClick listener when the user clicks a course in the RecyclerView.
     * @param onItemClickListener = instance of the OnItemClickListener.
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Interface that calls the item click listener when clicked.
     */
    public interface OnItemClickListener {
        void onItemClick(Courses courses);
    }

}
