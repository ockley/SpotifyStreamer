package dk.ockley.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by kv on 10/06/15.
 */
public class TopTracksAdapter extends ArrayAdapter<Track> {
    private Context ctx;
    private ArrayList<Track> mTracks;

    public TopTracksAdapter(Context context, ArrayList<Track> tracks) {
        super(context, 0, tracks);
        ctx = context;
        mTracks = tracks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String songName = getItem(position).name;
        String albumName = getItem(position).album.name;
        String imgUrl;

        if (getItem(position).album.images.size() > 0) {
            imgUrl = (getItem(position)).album.images.get(0).url;
        } else {
            imgUrl = "http://www.solarimpulse.com/img/profile-no-photo.png";
        }

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.top_tracks_view_item, parent, false);
        }
        ImageView trackImageField = (ImageView) convertView.findViewById(R.id.top_tracks_image);
        TextView songNameField = (TextView) convertView.findViewById(R.id.top_tracks_song_name);
        TextView albumNameField = (TextView) convertView.findViewById(R.id.top_tracks_album_name);


        songNameField.setText(songName);
        albumNameField.setText(albumName);
        Picasso.with(ctx).load(imgUrl).into(trackImageField);

        return convertView;
    }
}
