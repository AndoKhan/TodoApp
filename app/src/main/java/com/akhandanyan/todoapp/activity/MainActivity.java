package com.akhandanyan.todoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.akhandanyan.todoapp.R;
import com.akhandanyan.todoapp.model.TodoItem;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_ADD = 100;
    public static final int REQUEST_CODE_EDIT = 101;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_activity_main:
                    openAddTodoItem();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        FloatingActionButton fab = findViewById(R.id.fab_activity_main);
        fab.setOnClickListener(mOnClickListener);
    }

    private void openAddTodoItem() {
        Intent intent = new Intent(this, TodoItemActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ADD:
                TodoItem todoItem = data.getParcelableExtra(TodoItemActivity.ARG_TODO_ITEM);
                Toast.makeText(MainActivity.this, todoItem.toString(), Toast.LENGTH_LONG).show();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
