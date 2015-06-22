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

import java.io.IOError;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


public class MainActivityFragment extends Fragment {
    // Constants
    public static final String EXTRA_ARTIST_LIST = "EXTRA_ARTIST_LIST";
    private static final String EXTRA_SAVE_SEARCH = "EXTRA_SAVE_SEARCH";
    public static final String EXTRA_ARTIST = "EXTRA_ARTIST";

    //View items
    private EditText searchStringEditText;
    private static ListView artistsListView;

    //Other
    private ArrayList<ArtistParcelable> artistArrayList;
    private static Toast toast;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        //Fetch view items
        artistsListView = (ListView) v.findViewById(R.id.artist_list_view);
        searchStringEditText = (EditText) v.findViewById(R.id.search_edit_text);

        //If returning from a saved instance, then populate artist list and search field
        if (savedInstanceState != null) {
            try {
                artistArrayList = savedInstanceState.getParcelableArrayList(EXTRA_ARTIST_LIST);
                searchStringEditText.setText(savedInstanceState.get(EXTRA_SAVE_SEARCH).toString());
            } catch (Error e) {

            }
        } else {
            artistArrayList = new ArrayList<>();
        }

        searchStringEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Prevent searching an empty string
                if (!searchStringEditText.getText().toString().equals(""))
                    new FetchArtistsTask().execute(searchStringEditText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Place parcelable artist as an extra for use in detail TopTracksActivity
                Intent i = new Intent(getActivity(), TopTracksActivity.class);
                i.putExtra(EXTRA_ARTIST, (ArtistParcelable) artistsListView.getItemAtPosition(position));
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Make sure that both search string and list of artists is saved on configuration change.
        outState.putString(EXTRA_SAVE_SEARCH, searchStringEditText.getText().toString());
        outState.putParcelableArrayList(EXTRA_ARTIST_LIST, artistArrayList);
    }

    class FetchArtistsTask extends AsyncTask<String, Void, ArrayList<Artist>> {

        @Override
        protected ArrayList<Artist> doInBackground(String... params) {
            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                ArtistsPager result = spotify.searchArtists("*" + params[0] + "*");
                return (ArrayList<Artist>) result.artists.items;
            } catch (IOError e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Artist> artists) {
            //Clear the artist list
            artistArrayList = new ArrayList<>();
            if (artists.size() > 0) {
                for (Artist artist : artists) {
                    //Populate the list with parcelable artists with relevant data
                    if (artist.images.size() > 0) {
                        artistArrayList.add(new ArtistParcelable(artist.id, artist.name, artist.images.get(0).url));
                    } else {
                        artistArrayList.add(new ArtistParcelable(artist.id, artist.name, null));
                    }
                }
                //Attach adapter to list view.
                ArtistsAdapter adapter = new ArtistsAdapter(getActivity(), artistArrayList);
                artistsListView.setAdapter(adapter);
            } else {
                //No artists. Make a toast!
                if (!searchStringEditText.getText().toString().equals("")) {
                    if (toast != null)
                        toast.cancel();
                    toast.makeText(getActivity(), "Artist not found. Please refine your search.", Toast.LENGTH_SHORT).show();
                }
                artistsListView.setAdapter(null);
            }
        }
    }
}
