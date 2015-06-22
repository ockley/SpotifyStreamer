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

public class TopTracksAdapter extends ArrayAdapter<ParcelableTopTracks> {
    private Context ctx;

    public TopTracksAdapter(Context context, ArrayList<ParcelableTopTracks> tracks) {
        super(context, 0, tracks);
        ctx = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get previous view or make a new one
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.top_tracks_view_item, parent, false);
        }
        ImageView trackImageField = (ImageView) convertView.findViewById(R.id.top_tracks_image);
        TextView songNameField = (TextView) convertView.findViewById(R.id.top_tracks_song_name);
        TextView albumNameField = (TextView) convertView.findViewById(R.id.top_tracks_album_name);

        // Get values from parcelable top track and populate view elements
        String songName = getItem(position).getSongName();
        String albumName = getItem(position).getAlbumName();
        String imgUrl = getItem(position).getAlbumImage();
        songNameField.setText(songName);
        albumNameField.setText(albumName);
        if (imgUrl != null)
            Picasso.with(ctx).load(imgUrl).into(trackImageField);
        else
            Picasso.with(ctx).load(R.drawable.profile_no_photo).into(trackImageField);

        return convertView;
    }
}
