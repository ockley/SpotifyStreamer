package dk.ockley.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTracksFragment extends Fragment {

    private static final String SPOTIFY_TAG = "SPOTIFY_TAG";
    private String mArtistName;
    private String mArtistId;
    private String mArtistImage;
    private SpotifyApi api;
    private SpotifyService spotify;
    private ListView topTracksList;
    private TopTracksAdapter adapter;

    public TopTracksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_top_tracks, container, false);

        Intent i  = getActivity().getIntent();
        mArtistName = i.getStringExtra(MainActivityFragment.EXTRA_ARTIST_NAME);
        mArtistId = i.getStringExtra(MainActivityFragment.EXTRA_ARTIST_ID);
        mArtistImage = i.getStringExtra(MainActivityFragment.EXTRA_ARTIST_IMAGE);
        api = new SpotifyApi();
        spotify = api.getService();
        topTracksList = (ListView) v.findViewById(R.id.artist_top_tracks_listview);
        getTopTracks(mArtistId);
        return v;
    }

    // Get artists based on search string
    private void getTopTracks(String searchStr) {
        Map<String, Object> options = new HashMap<>();
        options.put("country", Locale.getDefault().getCountry());
        spotify.getArtistTopTrack(mArtistId, options, new Callback<Tracks>() {
            @Override
            public void success(final Tracks tracks, Response response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tracks.tracks.size() > 0) {
                            showTopTracks(tracks);
                        } else {
                            Toast.makeText(getActivity(), "No Top Tracks Found!", Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void showTopTracks(Tracks tracks) {

        adapter = new TopTracksAdapter(getActivity(), (ArrayList<Track>) tracks.tracks);
        topTracksList.setAdapter(adapter);
    }
}
