package com.example.testapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {
    private String name;
    private String position;
    private String nationality;
    private int jerseyNumber;
    private float rating;
    private int imageResource;

    // Constructor
    public Player(String name, String position, String nationality, int jerseyNumber, float rating, int imageResource) {
        this.name = name;
        this.position = position;
        this.nationality = nationality;
        this.jerseyNumber = jerseyNumber;
        this.rating = rating;
        this.imageResource = imageResource;
    }

    // Getters
    public String getName() { return name; }
    public String getPosition() { return position; }
    public String getNationality() { return nationality; }
    public int getJerseyNumber() { return jerseyNumber; }
    public float getRating() { return rating; }
    public int getImageResource() { return imageResource; }

    // Parcelable implementation
    protected Player(Parcel in) {
        name = in.readString();
        position = in.readString();
        nationality = in.readString();
        jerseyNumber = in.readInt();
        rating = in.readFloat();
        imageResource = in.readInt();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(position);
        dest.writeString(nationality);
        dest.writeInt(jerseyNumber);
        dest.writeFloat(rating);
        dest.writeInt(imageResource);
    }
}