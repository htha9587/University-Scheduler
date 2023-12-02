package com.example.universityscheduler.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.universityscheduler.Model.Terms;
import com.example.universityscheduler.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for the Terms RecyclerView object.
 */
public class TermsAdapter extends RecyclerView.Adapter {

    private OnItemClickListener onItemClickListener;
    private List<Terms> termsList = new ArrayList<>();

    /**
     * Initializes the Terms RecyclerVIew.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.terms_item, parent, false);
        return new TermHolder(itemView);
    }

    /**
     * Binds the RecyclerView and loads it up with the entries from the terms ArrayList.
     * @param holder = instance of the ViewHolder object.
     * @param position = the selected item in the RecycleView.
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TermHolder termHolder = (TermHolder) holder;
        Terms currentTerms = termsList.get(position);
        termHolder.textViewTermTitle.setText(currentTerms.getTermTitle());
        termHolder.textViewTermDateRange.setText(currentTerms.getTermStartDate() + " through " + currentTerms.getTermEndDate());
    }

    /**
     * Getter for the size of the Terms RecyclerView.
     * @return = the size of the terms list.
     */
    @Override
    public int getItemCount() {
        return termsList.size();
    }

    /**
     * Setter for the size of the Terms RecyclerView.
     * @param termsList = sets the termList.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setTermsList(List<Terms> termsList) {
        this.termsList = termsList;
        notifyDataSetChanged();
    }

    /**
     * Superclass for the Terms RecyclerView.
     */
    class TermHolder extends RecyclerView.ViewHolder {
        private TextView textViewTermTitle;
        private TextView textViewTermDateRange;

        //Links the Term title and range of date text views to the Superclass data members.
        public TermHolder(View view) {
            super(view);
            textViewTermTitle = view.findViewById(R.id.termsTitle);
            textViewTermDateRange = view.findViewById(R.id.termsDateRange);

            //OnClick listener for when the user clicks on a term in the term RecyclerView list.
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View viewer) {
                    int position = getAdapterPosition();
                    if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(termsList.get(position));
                    }
                }
            });
        }
    }

    /**
     * Setter for the onClick listener when the user clicks on a term in the RecyclerView.
     * @param onItemClickListener = instance of the OnItemClickListener.
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Interface that calls the item click listener when clicked.
     */
    public interface OnItemClickListener {
        void onItemClick(Terms terms);
    }

}
