package dk.ockley.spotifystreamer;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kv on 22/06/15.
 */
public class ParcelableTopTracks implements Parcelable {

    private static final String KEY_SONG_NAME = "song_name";
    private static final String KEY_ALBUM_NAME = "album_name";
    private static final String KEY_ALBUM_IMAGE = "album_image";
    private String songName;
    private String albumName;
    private String albumImage;

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }

    protected ParcelableTopTracks(String songName, String albumName, String albumImage) {
        this.songName = songName;
        this.albumName = albumName;
        this.albumImage = albumImage;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SONG_NAME, this.songName);
        bundle.putString(KEY_ALBUM_NAME, this.albumName);
        bundle.putString(KEY_ALBUM_IMAGE, this.albumImage);

        dest.writeBundle(bundle);
    }

    public static final Creator<ArtistParcelable> CREATOR = new Creator<ArtistParcelable>() {
        @Override
        public ArtistParcelable createFromParcel(Parcel in) {
            Bundle bundle = in.readBundle();
            return new ArtistParcelable(bundle.getString(KEY_SONG_NAME), bundle.getString(KEY_ALBUM_NAME), bundle.getString(KEY_ALBUM_IMAGE));
        }

        @Override
        public ArtistParcelable[] newArray(int size) {
            return new ArtistParcelable[size];
        }
    };

}
