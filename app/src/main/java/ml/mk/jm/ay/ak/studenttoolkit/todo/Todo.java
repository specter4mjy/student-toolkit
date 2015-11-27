package ml.mk.jm.ay.ak.studenttoolkit.todo;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Marc.
 * A simply POJO class to hold To-Do data. nothing special, aside from implementing Parcelable
 * to allow these objects to be placed in bundle and sent from one activity to another.
 */
public class Todo implements Parcelable {

    Integer id;//Required for database interaction more than anything else.
    String title;//Simple text data.
    String description;//More descriptive text data.
    Date due;//The date that this To-Do is due.

    //The constructor that should be used whenever a To-Do has to be created.
    public Todo(Integer id, String title, String description, Date due) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.due = due;
    }

    //A parcelable constructor, used when bundling To-Do's
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

    //A simple getter.
    public Integer getId() { return id; }

    //A simple setter.
    public void setId(Integer id) { this.id = id; }

    //A simple getter.
    public String getTitle() {
        return title;
    }

    //A simple setter.
    public void setTitle(String title) {
        this.title = title;
    }

    //A simple getter.
    public String getDescription() {
        return description;
    }

    //A simple setter.
    public void setDescription(String description) {
        this.description = description;
    }

    //A simple getter.
    public Date getDue() {
        return due;
    }

    //A simple setter.
    public void setDue(Date due) { this.due = due; }

    //toString method, overridden from Object to explicitly NOT display the ID of a To-Do.
    //That is data that only the database needs to know.
    @Override
    public String toString() {
        return title + ',' + description + ',' + due;
    }

    //Required over-write from parcelable.
    @Override
    public int describeContents() {
        return 0;
    }

    //Required over-write from parcelable.
    //Thsi is used to write to a parcel.
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeString(this.due.toString());
    }

    //Required for parcelable to work.
    public static final Creator CREATOR = new Creator() {
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

}
