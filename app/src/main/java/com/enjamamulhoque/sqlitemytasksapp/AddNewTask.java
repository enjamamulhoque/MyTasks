package com.enjamamulhoque.sqlitemytasksapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.enjamamulhoque.sqlitemytasksapp.model.TaskModel;
import com.enjamamulhoque.sqlitemytasksapp.utils.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.internal.TextWatcherAdapter;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    // widgets
    private EditText editText;
    private Button saveBtn;

    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_new_task_layout, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = view.findViewById(R.id.new_task_editText);
        saveBtn = view.findViewById(R.id.new_task_save_btn);

        databaseHelper = new DatabaseHelper(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();

        if(bundle != null){

            isUpdate = true;
            String task = bundle.getString("task");
            editText.setText(task);

            if(task.length() > 0){
                saveBtn.setEnabled(false);
            }

        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.equals("")){
                    saveBtn.setTextColor(Color.GRAY);
                    saveBtn.setEnabled(false);
                }else {
                    saveBtn.setTextColor(Color.BLACK);
                    saveBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boolean finalIsUpdate = isUpdate;

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String task = editText.getText().toString().trim();
                if(finalIsUpdate){
                    int id = bundle.getInt("id");
                    databaseHelper.updateTask(id, task);

                }else {

                    TaskModel taskModel = new TaskModel();
                    taskModel.setTask(task);
                    taskModel.setStatus(0);

                    databaseHelper.insertTask(taskModel);

                }

                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }













}
