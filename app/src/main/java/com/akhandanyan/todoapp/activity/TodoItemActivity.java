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
import java.util.UUID;

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
    private CheckBox mRepeatCheckBox;
    private RadioGroup mRepeatRadioGroup;
    private TextView mPriorityLabel;

    private Calendar mSelectedDate = Calendar.getInstance();
    private int mPriority = TodoItem.PRIORITY_MIN;

    private TodoItem mTodoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item);

        init();

        updateDateLabel();

        if (getIntent().hasExtra(ARG_TODO_ITEM)) {
            mTodoItem = getIntent().getParcelableExtra(ARG_TODO_ITEM);
            fillData(mTodoItem);
        }
    }

    private void init() {
        mTitleInput = findViewById(R.id.input_activity_todo_item_title);
        mDescriptionInput = findViewById(R.id.input_activity_todo_item_description);
        mReminderCheckBox = findViewById(R.id.checkbox_activity_todo_item_reminder);
        mRepeatCheckBox = findViewById(R.id.checkbox_activity_todo_item_repeat);
        mRepeatRadioGroup = findViewById(R.id.radio_activity_todo_item_repeat);
        mRepeatRadioGroup.setVisibility(View.GONE);
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
                        toggleRepeatTypeGroupVisibility(isChecked);
                    }
        });
    }

    private void fillData(TodoItem todoItem) {
        mTitleInput.setText(todoItem.getTitle());
        mDescriptionInput.setText(todoItem.getDescription());
        mSelectedDate.setTime(todoItem.getDate());
        mDateLabel.setText(DateUtil.formatDateToLongStyle(mSelectedDate.getTime()));
        mReminderCheckBox.setChecked(todoItem.isShouldRemind());
        if (todoItem.getRepeatType() != null) {
            switch (todoItem.getRepeatType()) {
                case NONE:
                    mRepeatCheckBox.setChecked(false);
                    break;
                case DAILY:
                    mRepeatRadioGroup.check(R.id.radio_activity_todo_item_daily);
                    mRepeatCheckBox.setChecked(true);
                    break;
                case WEEKLY:
                    mRepeatRadioGroup.check(R.id.radio_activity_todo_item_weekly);
                    mRepeatCheckBox.setChecked(true);
                    break;
                case MONTHLY:
                    mRepeatCheckBox.setChecked(true);
                    mRepeatRadioGroup.check(R.id.radio_activity_todo_item_monthly);
                    break;
            }
        }
        mPriorityLabel.setText(String.valueOf(todoItem.getPriority()));
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
        if (mTodoItem == null) {
            // If item is newly created initialize with uuid
            mTodoItem = new TodoItem();
            mTodoItem.setId(UUID.randomUUID().toString());
        }
        mTodoItem.setTitle(mTitleInput.getText().toString());
        mTodoItem.setDescription(mDescriptionInput.getText().toString());
        mTodoItem.setDate(mSelectedDate.getTime());
        mTodoItem.setShouldRemind(mReminderCheckBox.isChecked());
        mTodoItem.setPriority(mPriority);
        if (mRepeatCheckBox.isChecked()) {
            switch (mRepeatRadioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_activity_todo_item_daily:
                    mTodoItem.setRepeatType(TodoItem.Repeat.DAILY);
                    break;
                case R.id.radio_activity_todo_item_weekly:
                    mTodoItem.setRepeatType(TodoItem.Repeat.WEEKLY);
                    break;
                case R.id.radio_activity_todo_item_monthly:
                    mTodoItem.setRepeatType(TodoItem.Repeat.MONTHLY);
                    break;
                default:
                    mTodoItem.setRepeatType(TodoItem.Repeat.NONE);
            }
        }

        return mTodoItem;
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

    private void toggleRepeatTypeGroupVisibility(boolean visible) {
        if (visible) {
            mRepeatRadioGroup.setVisibility(View.VISIBLE);
        } else {
            mRepeatRadioGroup.setVisibility(View.GONE);
        }
    }
}
