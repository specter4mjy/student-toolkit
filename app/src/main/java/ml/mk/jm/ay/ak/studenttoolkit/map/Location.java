package ml.mk.jm.ay.ak.studenttoolkit.map;
/**
 * Created by Ahmed.
 */

//this class used to create object has room number, room lat and room lng in it.
public class Location {

    //private variables
    int _id;
    String room_no;
    String room_lat;
    String room_lng;

    // Empty constructor
    public Location(){
    }
    // constructor
    public Location(int id, String name, String lat, String lng){
        this._id = id;
        this.room_no = name;
        this.room_lat = lat;
        this.room_lng = lng;
    }
    // constructor
    public Location(String name, String lat, String lng){
        this.room_no = name;
        this.room_lat = lat;
        this.room_lng = lng;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this.room_no;
    }

    // setting name
    public void setName(String name){
        this.room_no = name;
    }

    // getting phone number
    public String getRoomLat(){
        return this.room_lat;
    }

    // setting phone number
    public void setRoomLat(String lat){
        this.room_lat = lat;
    }

    // getting phone number
    public String getRoomLng(){
        return this.room_lng;
    }

    // setting phone number
    public void setRoomLng(String lng){
        this.room_lng = lng;
    }
}
