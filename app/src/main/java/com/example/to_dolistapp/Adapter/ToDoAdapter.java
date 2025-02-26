package com.example.to_dolistapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolistapp.AddNewTask;
import com.example.to_dolistapp.MainActivity;
import com.example.to_dolistapp.Model.ToDoModel;
import com.example.to_dolistapp.R;


import java.util.List;

import Utils.DatabaseHandler;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

        private List<ToDoModel> todoList;
        private MainActivity activity;
        private DatabaseHandler db;

        public ToDoAdapter(DatabaseHandler db, MainActivity activity){
            this.db = db;
            this.activity = activity;
            this.db.openDatabase();
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.tasks_layout, parent, false);
            return new ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position){
            db.openDatabase();
            ToDoModel item = todoList.get(position);
            holder.task.setText(item.getTask());
            holder.task.setChecked(toBoolean(item.getStatus()));
            holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        db.updateStatus(item.getId(),1);
                    }
                     else {
                         db.updateStatus(item.getId(),0);
                    }
                }
            });
        }

        private boolean toBoolean(int n){
        return n!=0;
    }

        @Override
        public int getItemCount(){
            return todoList.size();
        }

        public Context getContext(){
        return activity;
    }
        public void setTasks(List<ToDoModel> todoList){
            this.todoList = todoList;
            notifyDataSetChanged();
        }


        public void deleteItem(int position){
            ToDoModel item = todoList.get(position);
            db.deleteTask(item.getId());
            todoList.remove(position);
            notifyItemRemoved(position);
        }

        public void editItem(int position){
            ToDoModel item = todoList.get(position);
            Bundle bundle= new Bundle();
            bundle.putInt("id",item.getId());
            bundle.putString("task",item.getTask());
            AddNewTask fragment = new AddNewTask();
            fragment.setArguments(bundle);
            fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
        }
        public static class ViewHolder extends RecyclerView.ViewHolder{
            CheckBox task;

            ViewHolder(View view){
                super(view);
                task = view.findViewById(R.id.checkbox1);
            }
        }
}
