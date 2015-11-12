package ml.mk.jm.ay.ak.studenttoolkit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Marc on 08/11/2015.
 */
public class Todo implements Parcelable {

    String title;
    String description;
    Date due;

    public Todo(String title, String description, Date due) {
        this.title = title;
        this.description = description;
        this.due = due;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    @Override
    public String toString() {
        return title + ',' + description + ',' + due;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
