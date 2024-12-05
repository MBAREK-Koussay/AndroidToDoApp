package com.example.todo_list;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_list.Adapter.OnDialogCloseListner;
import com.example.todo_list.Adapter.ToDoAdapter;
import com.example.todo_list.Model.TodoMoadel;
import com.example.todo_list.Utilis.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListner {

    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private DataBaseHelper myDB;
    private List<TodoMoadel> mlist;
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);

        // Initialize database and task list
        myDB = new DataBaseHelper(MainActivity.this);
        mlist = myDB.getAllTasks();
        if (mlist == null) {
            mlist = new ArrayList<>(); // Ensure mlist is never null
        }
        Collections.reverse(mlist); // Reverse list for proper order

        // Set up RecyclerView with Adapter and LayoutManager
        adapter = new ToDoAdapter(MainActivity.this, mlist, myDB);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set click listener for add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTask addTask = AddTask.newInstance();
                addTask.setOnTaskAddedListener(() -> {
                    refreshTaskList();
                });
                addTask.show(getSupportFragmentManager(), AddTask.class.getSimpleName());
            }
        });
    }

    @Override
    public void onDialogClose(@NonNull DialogInterface dialogInterface) {
        refreshTaskList();
    }

    private void refreshTaskList() {
        mlist = myDB.getAllTasks();
        if (mlist == null) {
            mlist = new ArrayList<>();
        }
        Collections.reverse(mlist); // Reverse list for proper order
        adapter.setTask(mlist);
        adapter.notifyDataSetChanged(); // Notify adapter to refresh RecyclerView
    }
}
