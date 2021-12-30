package com.example.musicsearcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    TextView  Artist_name;
    TextView  Song_name;
    Button youtube;
    Button spotify;
    String utubUrl;
    String spotUrl;
    ImageView song_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent i = getIntent();
        youtube=findViewById(R.id.Youtube);


        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Uri uri=Uri.parse(utubUrl);
startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });
        spotify=findViewById(R.id.Spotify);

        spotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri=Uri.parse("https://open.spotify.com/track/"+spotUrl);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });

         Song_name=findViewById(R.id.SongName);
          Artist_name=findViewById(R.id.SongArtist);
         song_image=findViewById(R.id.SongImage);
        RequestQueue queue = Volley.newRequestQueue(DetailActivity.this);
        String url =i.getExtras().getString("SongUrl");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject APIResponse) {

                        try {
                            utubUrl=APIResponse.getJSONObject("response").getJSONObject("song").getString("youtube_url");
                             spotUrl=APIResponse.getJSONObject("response").getJSONObject("song").getString("spotify_uuid");
                           String ArtistName=APIResponse.getJSONObject("response").getJSONObject("song").getString("artist_names");
                    String SongName=APIResponse.getJSONObject("response").getJSONObject("song").getString("title");
                   String SongImageurl=APIResponse.getJSONObject("response").getJSONObject("song").getString("song_art_image_thumbnail_url");
                            Song_name.setText(SongName);
                            Glide.with(DetailActivity.this).load(SongImageurl).into(song_image);
                            Artist_name.setText(ArtistName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //textView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
System.out.println("Error");
                        Toast.makeText(DetailActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();

                    }
                });



        queue.add(jsonObjectRequest);




    }

}