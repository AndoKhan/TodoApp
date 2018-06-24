package com.akhandanyan.todoapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class TodoItem implements Parcelable {
    public static final int PRIORITY_MAX = 5;
    public static final int PRIORITY_MIN = 0;

    private String mId;
    private String mTitle;
    private String mDescription;
    private Date mDate;
    private boolean mShouldRemind;
    private int mPriority;
    private Repeat mRepeatType;

    public TodoItem() {

    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
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

        // --- Attribute mId
        result = prime * result + ((mId == null) ? 0 : mId.hashCode());

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
        // --- Attribute mId
        if (mId == null) {
            return other.mId == null;
        } else {
            return mId.equals(other.mId);
        }
    }

    @Override
    public String toString() {
        return  "[" + mId + "]:"
                + mTitle + "|"
                + mDescription + "|"
                + mDate + "|"
                + mShouldRemind + "|"
                + mPriority + "|"
                + mRepeatType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeLong(mDate.getTime());
        dest.writeInt(mShouldRemind ? 1 : 0);
        dest.writeInt(mPriority);
        if (mRepeatType != null) {
            dest.writeInt(mRepeatType.ordinal());
        } else {
            dest.writeInt(-1);
        }
    }

    public static final Parcelable.Creator<TodoItem> CREATOR
            = new Parcelable.Creator<TodoItem>() {
        public TodoItem createFromParcel(Parcel in) {
            return new TodoItem(in);
        }

        public TodoItem[] newArray(int size) {
            return new TodoItem[size];
        }
    };

    private TodoItem(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
        mDate = new Date(in.readLong());
        mShouldRemind = in.readInt() == 1;
        mPriority = in.readInt();
        int repeatTypeOrdinal = in.readInt();
        if (repeatTypeOrdinal > 0) {
            mRepeatType = Repeat.values()[repeatTypeOrdinal];
        }
    }

    public enum Repeat {
        NONE, DAILY, WEEKLY, MONTHLY
    }
}
