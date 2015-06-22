package dk.ockley.spotifystreamer;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class ArtistParcelable implements Parcelable {

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";
    private String artistId;
    private String artistName;
    private String artistImage;

    public String getArtistImage() {
        return artistImage;
    }

    public void setArtistImage(String artistImage) {
        this.artistImage = artistImage;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    protected ArtistParcelable(String id, String name, String image) {
        this.artistId = id;
        this.artistName = name;
        this.artistImage = image;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, this.artistId);
        bundle.putString(KEY_NAME, this.artistName);
        bundle.putString(KEY_IMAGE, this.artistImage);

        dest.writeBundle(bundle);
    }

    public static final Creator<ArtistParcelable> CREATOR = new Creator<ArtistParcelable>() {
        @Override
        public ArtistParcelable createFromParcel(Parcel in) {
            Bundle bundle = in.readBundle();
            return new ArtistParcelable(bundle.getString(KEY_ID), bundle.getString(KEY_NAME), bundle.getString(KEY_IMAGE));
        }

        @Override
        public ArtistParcelable[] newArray(int size) {
            return new ArtistParcelable[size];
        }
    };

}
