package com.example.todo_list.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_list.AddTask;
import com.example.todo_list.MainActivity;
import com.example.todo_list.Model.TodoMoadel;
import com.example.todo_list.R;
import com.example.todo_list.Utilis.DataBaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    private List<TodoMoadel> mlist;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public ToDoAdapter(MainActivity activity, List<TodoMoadel> mlist, DataBaseHelper myDB) {
        this.activity = activity;
        this.mlist = mlist;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final TodoMoadel item = mlist.get(position);
        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked(item.getStatus() == 1);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                new androidx.appcompat.app.AlertDialog.Builder(activity)
                        .setTitle("Delete Task")
                        .setMessage("Do you want to delete this task?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            myDB.deleteTask(item.getId());
                            mlist.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mlist.size());
                        })
                        .setNegativeButton("No", (dialog, which) -> holder.checkBox.setChecked(false))
                        .setOnDismissListener(dialog -> {
                            if (item.getStatus() == 0) {
                                holder.checkBox.setChecked(false);
                            }
                        })
                        .show();
            } else {
                myDB.updateTaskStatus(item.getId(), 0);
                item.setStatus(0);
            }
        });

        holder.checkBox.setOnLongClickListener(v -> {
            editItems(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public void setTask(List<TodoMoadel> mlist) {
        this.mlist = mlist;
    }

    public void deleteTask(int position) {
        TodoMoadel item = mlist.get(position);
        myDB.deleteTask(item.getId());
        mlist.remove(position);
    }

    public void editItems(int position) {
        TodoMoadel item = mlist.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("ID", item.getId());
        bundle.putString("task", item.getTask());

        AddTask task = new AddTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), task.getTag());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
