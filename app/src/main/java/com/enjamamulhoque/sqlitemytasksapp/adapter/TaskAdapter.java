package com.enjamamulhoque.sqlitemytasksapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enjamamulhoque.sqlitemytasksapp.AddNewTask;
import com.enjamamulhoque.sqlitemytasksapp.MainActivity;
import com.enjamamulhoque.sqlitemytasksapp.R;
import com.enjamamulhoque.sqlitemytasksapp.model.TaskModel;
import com.enjamamulhoque.sqlitemytasksapp.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    MainActivity activity;
    List<TaskModel> taskModelList = new ArrayList<>();
    DatabaseHelper databaseHelper;

    public TaskAdapter(MainActivity activity, DatabaseHelper databaseHelper) {
        this.activity = activity;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_task_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.taskCheckBox.setText(taskModelList.get(position).getTask());
        int status = taskModelList.get(position).getStatus();
        holder.taskCheckBox.setChecked(toBoolean(status));

        holder.taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int adapterPosition = holder.getAdapterPosition();
                int id = taskModelList.get(adapterPosition).getId();
                if(b){

                    databaseHelper.updateStatus(id, 1);

                }else {

                    databaseHelper.updateStatus(id, 0);

                }
            }
        });


    }


    public void setTask(List<TaskModel> taskModelList){
        this.taskModelList = taskModelList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        databaseHelper.deleteTask(taskModelList.get(position).getId());
        taskModelList.remove(position);
        notifyItemRemoved(position);

    }

    public void editTask(int position){
        TaskModel task = taskModelList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", task.getId());
        bundle.putString("task", task.getTask());

        // passing data from MainActivity to BottomSheetFragment
        AddNewTask newTask = new AddNewTask();
        newTask.setArguments(bundle);
        newTask.show(activity.getSupportFragmentManager(), newTask.getTag());

    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }


    private boolean toBoolean(int status){

        return status != 0;
    }


    public Context getContext(){
        return activity;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox taskCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            taskCheckBox = itemView.findViewById(R.id.item_task_checkBox);
        }
    }
}
