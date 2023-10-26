package com.enjamamulhoque.sqlitemytasksapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.enjamamulhoque.sqlitemytasksapp.adapter.TaskAdapter;
import com.enjamamulhoque.sqlitemytasksapp.model.TaskModel;
import com.enjamamulhoque.sqlitemytasksapp.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener, Toolbar.OnMenuItemClickListener {

    Toolbar toolbar;
    RecyclerView taskRecyclerView;
    DatabaseHelper databaseHelper;
    List<TaskModel> taskModelList;
    TaskAdapter taskAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setOnMenuItemClickListener(this);
        taskRecyclerView = findViewById(R.id.main_task_recycler_view);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        taskModelList = new ArrayList<>();
        taskAdapter = new TaskAdapter(MainActivity.this, databaseHelper);

        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        taskModelList = databaseHelper.getAllTasks();
        Collections.reverse(taskModelList);
        taskAdapter.setTask(taskModelList);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);


    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        if (databaseHelper != null) {
            taskModelList = databaseHelper.getAllTasks();
            Collections.reverse(taskModelList);
            taskAdapter.setTask(taskModelList);
            taskAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if(item.getItemId() == R.id.menu_add){

            new AddNewTask().show(getSupportFragmentManager(), AddNewTask.TAG);

        }

        return true;
    }
}