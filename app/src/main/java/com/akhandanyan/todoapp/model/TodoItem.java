package com.akhandanyan.todoapp.model;

public class TodoItem {
    public static final int PRIORITY_MAX = 5;
    public static final int PRIORITY_MIN = 0;

    private String id;
    private String mTitle;
    private String mDescription;
    private String mDate;
    private boolean mShouldRemind;
    private int mPriority;
    private Repeat mRepeatType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public boolean isShouldRemind() {
        return mShouldRemind;
    }

    public void setShouldRemind(boolean shouldRemind) {
        mShouldRemind = shouldRemind;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        if (priority < PRIORITY_MIN || priority > PRIORITY_MAX) {
            throw new IllegalArgumentException("Priority should be in range of " + PRIORITY_MIN
                    + " - " + PRIORITY_MAX);
        }
        mPriority = priority;
    }

    public Repeat getRepeatType() {
        return mRepeatType;
    }

    public void setRepeatType(Repeat repeatType) {
        mRepeatType = repeatType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        // --- Attribute id
        result = prime * result + ((id == null) ? 0 : id.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        TodoItem other = (TodoItem) obj;
        // --- Attribute id
        if (id == null) {
            return other.id == null;
        } else {
            return id.equals(other.id);
        }
    }

    @Override
    public String toString() {
        return  "[" + id + "]:"
                + mTitle + "|"
                + mDescription + "|"
                + mDate + "|"
                + mShouldRemind + "|"
                + mPriority + "|"
                + mRepeatType;
    }

    public enum Repeat {
        NONE, DAILY, WEEKLY, MONTHLY
    }
}
