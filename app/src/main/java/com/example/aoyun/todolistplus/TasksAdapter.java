package com.example.aoyun.todolistplus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter <TasksAdapter.MyRecyclerViewHolder> {

    private Context mContext;
    private List <String> mTasksList;

    public TasksAdapter(List <String> tasksList, Context context) {
        this.mTasksList = tasksList;
        this.mContext = context;

    }

    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_todo, parent, false);  //传入的view就是每一个item的样式
        MyRecyclerViewHolder holder = new MyRecyclerViewHolder(view);    //传入的view就是每一个item的样式
        return holder;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewHolder holder, int position) {
        String todoTitle = mTasksList.get(position);
        holder.textView.setText(todoTitle);
    }

    @Override
    public int getItemCount() {
        return mTasksList.size();
    }

    class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;


        public MyRecyclerViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.task_title);


        }
    }

}
