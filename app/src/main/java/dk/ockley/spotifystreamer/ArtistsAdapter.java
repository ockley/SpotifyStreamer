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

import kaaes.spotify.webapi.android.models.Artist;

public class ArtistsAdapter extends ArrayAdapter<Artist> {
    private Context ctx;
    private ArrayList<Artist> mArtists;

    public ArtistsAdapter(Context context, ArrayList<Artist> artists) {
        super(context, 0, artists);
        ctx = context;
        mArtists = artists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).name;
        String imgUrl;

        if (getItem(position).images.size() > 0) {
            imgUrl = (getItem(position)).images.get(0).url;
        } else {
            imgUrl = "http://www.solarimpulse.com/img/profile-no-photo.png";
        }

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_view_item, parent, false);
        }
        ImageView artistImage = (ImageView) convertView.findViewById(R.id.artist_image);
        TextView artistName = (TextView) convertView.findViewById(R.id.artist_name);

        artistName.setText(name);
        Picasso.with(ctx).load(imgUrl).into(artistImage);

        return convertView;
    }
}
