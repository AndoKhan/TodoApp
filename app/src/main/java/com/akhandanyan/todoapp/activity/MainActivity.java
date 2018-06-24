package com.akhandanyan.todoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.akhandanyan.todoapp.R;
import com.akhandanyan.todoapp.adapter.TodoItemAdapter;
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

    private TodoItemAdapter.OnItemSelectedListener mOnItemSelectedListener = new TodoItemAdapter.OnItemSelectedListener() {
        @Override
        public void onItemSelected(TodoItem todoItem) {
            openEditTodoItem(todoItem);
        }
    };

    private TodoItemAdapter mTodoItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        FloatingActionButton fab = findViewById(R.id.fab_activity_main);
        fab.setOnClickListener(mOnClickListener);

        mTodoItemAdapter = new TodoItemAdapter();
        mTodoItemAdapter.setOnItemSelectedListener(mOnItemSelectedListener);
        RecyclerView recyclerView = findViewById(R.id.recycler_activity_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mTodoItemAdapter);
    }

    private void openAddTodoItem() {
        Intent intent = new Intent(this, TodoItemActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    private void openEditTodoItem(TodoItem todoItem) {
        Intent intent = new Intent(this, TodoItemActivity.class);
        intent.putExtra(TodoItemActivity.ARG_TODO_ITEM, todoItem);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ADD: {
                    TodoItem todoItem = data.getParcelableExtra(TodoItemActivity.ARG_TODO_ITEM);
                    mTodoItemAdapter.addItem(todoItem);
                }
                break;
            case REQUEST_CODE_EDIT: {
                    TodoItem todoItem = data.getParcelableExtra(TodoItemActivity.ARG_TODO_ITEM);
                    mTodoItemAdapter.updateItem(todoItem);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
