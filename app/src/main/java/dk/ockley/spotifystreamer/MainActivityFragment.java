package dk.ockley.spotifystreamer;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;



public class MainActivityFragment extends Fragment {

    public static final String SPOTIFY_TAG = "SPOTIFY_TAG";
    private EditText searchStringEditText;
    private static ListView artistsList;
    private SpotifyApi api;
    private SpotifyService spotify;
    private ArtistsAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        api = new SpotifyApi();
        spotify = api.getService();
        artistsList = (ListView) v.findViewById(R.id.artist_list_view);
        //artistsList.setAdapter(adapter);
        searchStringEditText = (EditText) v.findViewById(R.id.search_edit_text);
        searchStringEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getArtists(searchStringEditText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        artistsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist tmpArtist = (Artist) artistsList.getItemAtPosition(position);
                Toast.makeText(getActivity(), tmpArtist.name, Toast.LENGTH_SHORT).show();
            }
        });

        // Return the fragment view to the activity
        return v;
    }





    // Get artists based on search string
    private void getArtists(String searchStr) {
        spotify.searchArtists(searchStr, new Callback<ArtistsPager>() {
            @Override
            public void success(final ArtistsPager artistsPager, Response response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (artistsPager.artists.items.size() > 0) {
                            showArtists((ArrayList<Artist>) artistsPager.artists.items);
                        } else {
                            artistsList.setAdapter(null);
                        }
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void showArtists(ArrayList<Artist> artists) {
        adapter = new ArtistsAdapter(getActivity(), artists);
        artistsList.setAdapter(adapter);
    }
}
