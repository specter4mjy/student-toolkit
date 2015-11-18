package ml.mk.jm.ay.ak.studenttoolkit.todo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Marc on 08/11/2015.
 */
public class Todo implements Parcelable {

    Integer id;
    String title;
    String description;
    Date due;

    public Todo(Integer id, String title, String description, Date due) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.due = due;
    }

    public Todo(Parcel parcel) {
        this.title = parcel.readString();
        this.description = parcel.readString();
        this.due = new Date();
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

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

    public void setDue(Date due) { this.due = due; }

    @Override
    public String toString() {
        return title + ',' + description + ',' + due;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeString(this.due.toString());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

}
