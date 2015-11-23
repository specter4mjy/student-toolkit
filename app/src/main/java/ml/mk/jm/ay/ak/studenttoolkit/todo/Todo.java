package ml.mk.jm.ay.ak.studenttoolkit.todo;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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
        SimpleDateFormat simpleDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyy", Locale.US);
        Date parsedDate = new Date();
        this.id = parcel.readInt();
        this.title = parcel.readString();
        this.description = parcel.readString();
        try {
            parsedDate = simpleDate.parse(parcel.readString());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.due = parsedDate;
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

    public static final Creator CREATOR = new Creator() {
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

}
