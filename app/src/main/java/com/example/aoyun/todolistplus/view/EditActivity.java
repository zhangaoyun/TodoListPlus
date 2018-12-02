package com.example.aoyun.todolistplus.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.aoyun.todolistplus.R;

public class EditActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Button button =findViewById(R.id.new_Todo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskEditText = new EditText(EditActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(EditActivity.this)  //是否增加Todo的AlertDialog
                        .setTitle("增加一个新的Todo")
                        .setMessage("打算做什么？")
                        .setView(taskEditText)
                        .setPositiveButton("增加", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = taskEditText.getText().toString();
                                if (task.compareTo("") != 0) {
                                    Intent intent =new Intent();
                                    intent.putExtra("data",task);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                dialog.show();
            }
        });
    }
}
