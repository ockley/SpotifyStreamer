package dk.ockley.spotifystreamer;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    public static final String EXTRA_ARTIST_NAME = "EXTRA_ARTIST_NAME";
    public static final String EXTRA_ARTIST_ID = "EXTRA_ARTIST_ID";
    public static final String EXTRA_ARTIST_IMAGE = "EXTRA_ARTIST_IMAGE";
    private static final String SAVE_SEARCH = "save_search";
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
        searchStringEditText = (EditText) v.findViewById(R.id.search_edit_text);
        if(savedInstanceState != null)
            searchStringEditText.setText(savedInstanceState.get(SAVE_SEARCH).toString());
            searchStringEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!searchStringEditText.getText().toString().equals(""));
                    new FetchArtistsTask().execute(searchStringEditText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        artistsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist tmpArtist = (Artist) artistsList.getItemAtPosition(position);

                Intent i = new Intent(getActivity(), TopTracksActivity.class);
                i.putExtra(EXTRA_ARTIST_NAME, tmpArtist.name);
                i.putExtra(EXTRA_ARTIST_ID, tmpArtist.id);

                //Not all artists have images
                if (tmpArtist.images.size() > 0) {
                    i.putExtra(EXTRA_ARTIST_IMAGE, tmpArtist.images.get(0).url);
                } else {
                    i.putExtra(EXTRA_ARTIST_IMAGE, "http://www.solarimpulse.com/img/profile-no-photo.png");
                }
                startActivity(i);
            }
        });

        // Return the fragment view to the activity
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_SEARCH, searchStringEditText.getText().toString());
        Log.d(SPOTIFY_TAG, outState.getString(SAVE_SEARCH));
    }

    class FetchArtistsTask extends AsyncTask<String, Void, ArrayList<Artist>> {

        @Override
        protected ArrayList<Artist> doInBackground(String... params) {
            ArtistsPager result = spotify.searchArtists("*"+params[0]+"*");
            return (ArrayList<Artist>) result.artists.items;
        }

        @Override
        protected void onPostExecute(ArrayList<Artist> artists) {
            if (artists.size()>0) {
                adapter = new ArtistsAdapter(getActivity(), artists);
                artistsList.setAdapter(adapter);
            } else {
                if (!searchStringEditText.getText().toString().equals(""))
                    Toast.makeText(getActivity(), "Artist not found. Please refine your search.", Toast.LENGTH_SHORT).show();
                artistsList.setAdapter(null);
            }
        }
    }
}
