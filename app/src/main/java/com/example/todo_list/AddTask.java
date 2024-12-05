package com.example.todo_list;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todo_list.Model.TodoMoadel;
import com.example.todo_list.Utilis.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddTask extends BottomSheetDialogFragment {

    private EditText mEditText;
    private Button mSaveButton;
    private DataBaseHelper myDB;

    public static AddTask newInstance() {
        return new AddTask();
    }

    private OnTaskAddedListener taskAddedListener;

    public interface OnTaskAddedListener {
        void onTaskAdded();
    }

    public void setOnTaskAddedListener(OnTaskAddedListener listener) {
        this.taskAddedListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = view.findViewById(R.id.editText);
        mSaveButton = view.findViewById(R.id.addButton);
        myDB = new DataBaseHelper(getActivity());

        boolean isUpdate = false;
        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            if (task != null) {
                mEditText.setText(task);
                if (!task.isEmpty()) {
                    mSaveButton.setEnabled(true);
                }
            }
        }

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(Color.GRAY);
                } else {
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(Color.BLUE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mEditText.getText().toString();
                if (finalIsUpdate) {
                    int taskId = bundle.getInt("ID");
                    myDB.updateTask(taskId, text);
                } else {
                    TodoMoadel item = new TodoMoadel();
                    item.setTask(text);
                    item.setStatus(0);
                    myDB.insertTask(item);
                }

                if (taskAddedListener != null) {
                    taskAddedListener.onTaskAdded(); // Notify the listener
                }

                dismiss();
            }
        });
    }
}
