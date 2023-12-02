package com.example.universityscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.universityscheduler.Adapter.TermsAdapter;
import com.example.universityscheduler.Model.Terms;
import com.example.universityscheduler.ViewModel.SchedulerVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.Objects;

/**
 * Activity class for the Terms RecyclerView list. From here, the user can add a new term, or view/edit one, or delete one altogether.
 */
public class TermsActivity extends AppCompatActivity {

    private SchedulerVM schedulerVM;

    /**
     * Loads the Terms list XML and initializes the Terms RecyclerView.
     * @param savedInstanceState = instance of bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //When the floating action button is clicked, the user is redirected to the edit/add term activity.
        FloatingActionButton floatingActionButton = findViewById(R.id.termsFab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditTermActivity.class);
                startActivity(intent);
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Loads the layout for the Term RecyclerView.
        RecyclerView recyclerView = findViewById(R.id.termRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        final TermsAdapter termsAdapter = new TermsAdapter();
        recyclerView.setAdapter(termsAdapter);

        //Loads and initializes the ViewModel and has the Observer get all the terms and then proceeds to set them.
        schedulerVM = new ViewModelProvider(this).get(SchedulerVM.class);
        schedulerVM.getAllTerms().observe(this, new Observer<List<Terms>>() {
            @Override
            public void onChanged(@Nullable List<Terms> terms) {
                termsAdapter.setTermsList(terms);
            }
        });

        //OnClick listener for when the user clicks on an item in the RecyclerView, redirecting them to the Term detail activity.
        termsAdapter.setOnItemClickListener(new TermsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Terms terms) {
                Intent intent = new Intent(TermsActivity.this, DetailTermActivity.class);
                intent.putExtra(DetailTermActivity.EXTRA_ID, terms.getId());
                //intent.putExtra(EditTermActivity.EXTRA_TITLE, terms.getTermTitle());
                //intent.putExtra(EditTermActivity.EXTRA_START_DATE, terms.getTermStartDate());
                //intent.putExtra(EditTermActivity.EXTRA_END_DATE, terms.getTermEndDate());
                startActivity(intent);
            }
        });
    }

    /**
     * Click handler for the options navigation menu, allowing the user to navigate more easily throughout the Scheduler App.
     * @param menuItem = instance of MenuItem
     * @return = menuItem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_navigation) {
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(menuItem);
    }

    /**
     *Adds items to the Terms Options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
