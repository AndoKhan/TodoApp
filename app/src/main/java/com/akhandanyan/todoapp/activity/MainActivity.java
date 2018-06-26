package com.akhandanyan.todoapp.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.akhandanyan.todoapp.R;
import com.akhandanyan.todoapp.fragment.TodoItemFragment;
import com.akhandanyan.todoapp.fragment.TodoItemsListFragment;
import com.akhandanyan.todoapp.model.TodoItem;

public class MainActivity extends AppCompatActivity {

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

    private TodoItemsListFragment.OnFragmentInteractionListener mOnTodoItemsInteractionListener =
            new TodoItemsListFragment.OnFragmentInteractionListener() {
                @Override
                public void onEditItem(TodoItem todoItem) {
                    openEditTodoItem(todoItem);
                }
            };

    private TodoItemFragment.OnFragmentInteractionListener mOnTodoItemInteractionListener =
            new TodoItemFragment.OnFragmentInteractionListener() {
                @Override
                public void onItemCreated(TodoItem todoItem) {
                    delegateItemCreationToFragment(todoItem);
                }

                @Override
                public void onItemChanged(TodoItem todoItem) {
                    delegateItemChangeToFragment(todoItem);
                }
            };

    private TodoItemsListFragment mTodoItemsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        FloatingActionButton fab = findViewById(R.id.fab_activity_main);
        fab.setOnClickListener(mOnClickListener);

        mTodoItemsListFragment = (TodoItemsListFragment) (getFragmentManager()
                .findFragmentById(R.id.fragment_activity_main));
        mTodoItemsListFragment.setOnInteractionListener(mOnTodoItemsInteractionListener);
    }

    private void openAddTodoItem() {
        TodoItemFragment todoItemFragment = TodoItemFragment.newInstance(null);
        todoItemFragment.setOnInteractionListener(mOnTodoItemInteractionListener);
        openFragmentInContainer(todoItemFragment, true);
    }

    private void openEditTodoItem(TodoItem todoItem) {
        TodoItemFragment todoItemFragment = TodoItemFragment.newInstance(todoItem);
        todoItemFragment.setOnInteractionListener(mOnTodoItemInteractionListener);
        openFragmentInContainer(todoItemFragment, true);
    }

    private void delegateItemCreationToFragment(TodoItem todoItem) {
        mTodoItemsListFragment.addTodoItem(todoItem);
        getFragmentManager().popBackStack();
    }

    private void delegateItemChangeToFragment(TodoItem todoItem) {
        mTodoItemsListFragment.editTodoItem(todoItem);
        getFragmentManager().popBackStack();
    }

    private void openFragmentInContainer(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.container_activity_main, fragment);
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }

        fragmentTransaction.commit();
    }
}
