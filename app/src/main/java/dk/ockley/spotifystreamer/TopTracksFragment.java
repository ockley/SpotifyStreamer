package dk.ockley.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

public class TopTracksFragment extends Fragment {

    private static final String EXTRA_TOP_TRACKS = "EXTRA_TOP_TRACKS";
    private SpotifyService spotify;
    private ListView topTracksList;
    private ArrayList<ParcelableTopTracks> topTracksParcel;
    private static Toast toast;

    public TopTracksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        View v =  inflater.inflate(R.layout.fragment_top_tracks, container, false);

        //Hook up the list view item
        topTracksList = (ListView) v.findViewById(R.id.artist_top_tracks_listview);

        //If saved instance then populate list with top tracks
        if (savedInstanceState != null) {
            topTracksParcel = savedInstanceState.getParcelableArrayList(EXTRA_TOP_TRACKS);
        } else {
            topTracksParcel = new ArrayList<ParcelableTopTracks>();
        }

        // Test that the intent has a parcelable artist
        Intent i  = getActivity().getIntent();
        if (i != null && i.hasExtra(MainActivityFragment.EXTRA_ARTIST)) {
            ArtistParcelable artist = i.getParcelableExtra(MainActivityFragment.EXTRA_ARTIST);
            new FetchTopTracks().execute(artist.getArtistId());
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_TOP_TRACKS, topTracksParcel);
    }

    class FetchTopTracks extends AsyncTask<String, Void, ArrayList<Track>> {

        @Override
        protected ArrayList<Track> doInBackground(String... params) {
            //Connect to the Spotify service
            SpotifyApi api = new SpotifyApi();
            spotify = api.getService();
            try {
                // Make option from locale country or default to US
                Map<String, Object> options = new HashMap<>();
                String country = Locale.getDefault().getCountry();
                if (country.equals("")) country = "US";
                options.put("country", country);

                Tracks result = spotify.getArtistTopTrack(params[0], options);
                return (ArrayList<Track>) result.tracks;

            } catch (RetrofitError e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Track> tracks) {
            super.onPostExecute(tracks);
            //Clear the top tracks list
            topTracksParcel = new ArrayList<ParcelableTopTracks>();
            if (tracks != null && tracks.size() > 0) {
                for (Track track : tracks) {
                    //Populate the list with parcelable top tracks
                    if(track.album.images.size() > 0)
                        topTracksParcel.add(new ParcelableTopTracks(track.name, track.album.name, track.album.images.get(0).url));
                     else
                        topTracksParcel.add(new ParcelableTopTracks(track.name, track.album.name, null));
                }
            } else {
                if (toast != null)
                    toast.cancel();
                toast.makeText(getActivity(),"No Top Tracks Found!", Toast.LENGTH_SHORT).show();
            }

            //Populate list view with adapter
            TopTracksAdapter adapter = new TopTracksAdapter(getActivity(), topTracksParcel);
            topTracksList.setAdapter(adapter);
        }
    }
}
