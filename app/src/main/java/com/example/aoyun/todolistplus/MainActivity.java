package com.example.aoyun.todolistplus;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static ArrayList <String> tasksList = new ArrayList <>();

    public TaskDbHelper mHelper;
    private RecyclerView mTaskRecyclerView; //recycler_view
    private TasksAdapter mAdapter;  //Todo适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskDbHelper(this, "tasks", null, 1);    //四个参数
        mTaskRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTaskRecyclerView.setLayoutManager(layoutManager);  //设置LinearLayoutManager
        mTaskRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        updateUI(); //更新界面

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);  //菜单，即增加按钮
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task:
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent, 1);
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("data");
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    Log.d("SQLite", "Here");
                    values.put("title", returnedData);
                    db.insert("tasks", null, values);
                    db.close();
                    updateUI();
                }
                break;
            default:
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_title);
        String task = taskTextView.getText().toString();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete("tasks", "title = ?", new String[]{task});
        db.close();
        updateUI();
    }

    public void editTask(View view) {
        Intent intent = new Intent(MainActivity.this, TodoActivity.class);
        startActivity(intent);
    }

    private void updateUI() {
        tasksList.clear();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("tasks", new String[]{"_id", "title"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex("title");//获取某一列在表中对应的位置索引
            tasksList.add(cursor.getString(idx));
        }
            mAdapter = new TasksAdapter(tasksList, this);
            mTaskRecyclerView.setAdapter(mAdapter);

        cursor.close(); //需要close
    }
}
