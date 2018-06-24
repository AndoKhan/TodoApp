package com.akhandanyan.todoapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.akhandanyan.todoapp.R;
import com.akhandanyan.todoapp.model.TodoItem;
import com.akhandanyan.todoapp.util.DateUtil;

import java.util.Calendar;
import java.util.Date;import java.util.UUID;

public class TodoItemActivity extends AppCompatActivity {
    public static final String ARG_TODO_ITEM = "arg.todo.item";

    DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mSelectedDate.set(Calendar.YEAR, year);
            mSelectedDate.set(Calendar.MONTH, monthOfYear);
            mSelectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            openTimePicker();
        }
    };

    TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mSelectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mSelectedDate.set(Calendar.MINUTE, minute);

            updateDateLabel();
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.action_activity_todo_item_edit:
                    submit();
                    break;
                case R.id.action_activity_todo_item_priority_increase:
                    increasePriority();
                    break;
                case R.id.action_activity_todo_item_priority_decrease:
                    decreasePriority();
                    break;
                case R.id.label_activity_todo_item_date:
                    openDatePicker();
                    break;
            }
        }
    };

    private TextInputEditText mTitleInput;
    private TextInputEditText mDescriptionInput;
    private TextView mDateLabel;
    private CheckBox mReminderCheckBox;
    private RadioGroup mRepeatRadioGroup;
    private TextView mPriorityLabel;

    private Calendar mSelectedDate = Calendar.getInstance();
    private int mPriority = TodoItem.PRIORITY_MIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item);

        init();

        updateDateLabel();
    }

    private void init() {
        mTitleInput = findViewById(R.id.input_activity_todo_item_title);
        mDescriptionInput = findViewById(R.id.input_activity_todo_item_description);
        mReminderCheckBox = findViewById(R.id.checkbox_activity_todo_item_reminder);
        mRepeatRadioGroup = findViewById(R.id.radio_activity_todo_item_repeat);
        mPriorityLabel = findViewById(R.id.label_activity_todo_item_priority_value);
        mDateLabel = findViewById(R.id.label_activity_todo_item_date);

        findViewById(R.id.action_activity_todo_item_edit).setOnClickListener(mOnClickListener);
        mDateLabel.setOnClickListener(mOnClickListener);
        findViewById(R.id.action_activity_todo_item_priority_increase).setOnClickListener(mOnClickListener);
        findViewById(R.id.action_activity_todo_item_priority_decrease).setOnClickListener(mOnClickListener);

        ((CheckBox)findViewById(R.id.checkbox_activity_todo_item_repeat)).
                setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    }
        });
    }

    private void submit() {
        if (checkInput()) {
            Intent intent = new Intent();
            intent.putExtra(ARG_TODO_ITEM, createTodoItemFromInput());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private TodoItem createTodoItemFromInput() {
        TodoItem todoItem = new TodoItem();
        todoItem.setId(UUID.randomUUID().toString());
        todoItem.setTitle(mTitleInput.getText().toString());
        todoItem.setDescription(mDescriptionInput.getText().toString());
        todoItem.setDate(mSelectedDate.getTime());
        todoItem.setShouldRemind(mReminderCheckBox.isChecked());
        todoItem.setPriority(mPriority);
        switch (mRepeatRadioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_activity_todo_item_daily:
                todoItem.setRepeatType(TodoItem.Repeat.DAILY);
                break;
            case R.id.radio_activity_todo_item_weekly:
                todoItem.setRepeatType(TodoItem.Repeat.WEEKLY);
                break;
            case R.id.radio_activity_todo_item_monthly:
                todoItem.setRepeatType(TodoItem.Repeat.MONTHLY);
                break;
        }

        return todoItem;
    }

    private void increasePriority() {
        mPriority = Math.min(++mPriority, TodoItem.PRIORITY_MAX);
        mPriorityLabel.setText(String.valueOf(mPriority));
    }

    private void decreasePriority() {
        mPriority = Math.max(--mPriority, TodoItem.PRIORITY_MIN);
        mPriorityLabel.setText(String.valueOf(mPriority));
    }

    private void updateDateLabel() {
        mDateLabel.setText(DateUtil.formatDateToLongStyle(mSelectedDate.getTime()));
    }

    private boolean checkInput() {
        boolean isValid;
        isValid = checkTitle();

        return isValid;
    }

    private boolean checkTitle() {
        boolean isValid;
        if (isEmpty(mTitleInput)) {
            isValid = false;
            mTitleInput.setError("Title field is mandatory");
        } else {
            isValid = true;
            mTitleInput.setError(null);
        }

        return isValid;
    }

    private boolean isEmpty(TextInputEditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }

    private void openDatePicker() {
        new DatePickerDialog(this, mOnDateSetListener, mSelectedDate.get(Calendar.YEAR),
                mSelectedDate.get(Calendar.MONTH),
                mSelectedDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void openTimePicker() {
        new TimePickerDialog(this, mOnTimeSetListener, mSelectedDate.get(Calendar.HOUR_OF_DAY),
                mSelectedDate.get(Calendar.MINUTE), true).show();
    }
}
