package com.akhandanyan.todoapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akhandanyan.todoapp.R;
import com.akhandanyan.todoapp.adapter.TodoItemAdapter;
import com.akhandanyan.todoapp.model.TodoItem;

public class TodoItemsListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public TodoItemsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TodoItemsListFragment.
     */
    public static TodoItemsListFragment newInstance() {
        TodoItemsListFragment fragment = new TodoItemsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private TodoItemAdapter.OnItemSelectedListener mOnItemSelectedListener = new TodoItemAdapter.OnItemSelectedListener() {
        @Override
        public void onItemSelected(TodoItem todoItem) {
            if (mListener != null) {
                mListener.onEditItem(todoItem);
            }
        }
    };

    private TodoItemAdapter mTodoItemAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_items_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
    }

    private void init(View root) {
        mTodoItemAdapter = new TodoItemAdapter();
        mTodoItemAdapter.setOnItemSelectedListener(mOnItemSelectedListener);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_fragment_todo_items_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mTodoItemAdapter);
    }

    public void addTodoItem(TodoItem todoItem) {
        mTodoItemAdapter.addItem(todoItem);
    }

    public void editTodoItem(TodoItem todoItem) {
        mTodoItemAdapter.updateItem(todoItem);
    }

    public void setOnInteractionListener(OnFragmentInteractionListener onInteractionListener) {
        mListener = onInteractionListener;
    }

    /**
     * This interface can be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onEditItem(TodoItem todoItem);
    }
}
