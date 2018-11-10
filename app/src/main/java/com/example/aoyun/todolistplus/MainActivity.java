package com.example.aoyun.todolistplus;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.DividerItemDecoration;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private TaskDbHelper mHelper;   //声明数据库Helper
    private RecyclerView mTaskRecyclerView; //recycler_view
    private TasksAdapter mAdapter;  //Todo适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskDbHelper(this, "tasks", null, 1);    //四个参数
        mTaskRecyclerView = findViewById(R.id.recycler_view);    //找到RecyclerView
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
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)  //是否增加Todo的AlertDialog
                        .setTitle("增加一个新的Todo")
                        .setMessage("打算做什么？")
                        .setView(taskEditText)
                        .setPositiveButton("增加", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = taskEditText.getText().toString();
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                Log.d("SQLite", "Here");
                                values.put("title", task);
                                db.insert("tasks", null, values);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                dialog.show();
                return true;
            default:
                return true;
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

    private void updateUI() {
        ArrayList <String> tasksList = new ArrayList <>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("tasks", new String[]{"_id", "title"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex("title");//获取某一列在表中对应的位置索引
            tasksList.add(cursor.getString(idx));
        }

        mAdapter = new TasksAdapter(tasksList, this);    //new一个
        mTaskRecyclerView.setAdapter(mAdapter);


        cursor.close(); //需要close
    }

    //想要用这种方法添加分割线未成功
//    class MyDecoration extends RecyclerView.ItemDecoration{
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            super.getItemOffsets(outRect, view, parent, state);
//            outRect.set(0,0,0,getResources().getDimensionPixelOffset(R.dimen.dividerHeight));
//        }
//    }

}
