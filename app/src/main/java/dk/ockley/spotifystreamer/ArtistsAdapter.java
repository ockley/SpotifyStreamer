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

public class ArtistsAdapter extends ArrayAdapter<ArtistParcelable>{
    private Context ctx;

    public ArtistsAdapter(Context context, ArrayList<ArtistParcelable> artists) {
        super(context, 0, artists);
        ctx = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get previous view or make a new
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_view_item, parent, false);
        }
        ImageView artistImage = (ImageView) convertView.findViewById(R.id.artist_image);
        TextView artistName = (TextView) convertView.findViewById(R.id.artist_name);

        //Populate view elements with fields from parcelable artist
        String name = getItem(position).getArtistName();
        String imgUrl = getItem(position).getArtistImage();

        artistName.setText(name);
        if (imgUrl != null) {
            Picasso.with(ctx).load(imgUrl).into(artistImage);
        } else {
            Picasso.with(ctx).load(R.drawable.profile_no_photo).into(artistImage);
        }

        return convertView;
    }
}
