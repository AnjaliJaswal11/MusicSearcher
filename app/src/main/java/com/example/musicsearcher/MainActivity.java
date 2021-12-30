package com.example.musicsearcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button Search_button;
    EditText Search_bar;
    ListView Search_results;
    List<String> musicUrl = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Search_button=findViewById(R.id.SearchButton);
        Search_bar=findViewById(R.id.SearchBar);
        Search_results=(ListView)findViewById(R.id.SearchResultsList);
        Search_results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in=new Intent(getApplicationContext(), DetailActivity.class);
                in.putExtra("SongUrl",musicUrl.get(i));
                startActivity(in);
            }
        });
        Search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> musicDataList=new ArrayList<>();
                //List<String> musicUrl = new ArrayList<>();
                List<String> musicArtist = new ArrayList<>();
                List<String> musicImage = new ArrayList<>();
musicUrl.clear();
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url ="https://genius.com/api/search/song?q="+Search_bar.getText().toString().replace(" ","%20");

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject APIResponse) {
                                System.out.println(url);


                                try {
                                    for(int i=0;i<APIResponse.getJSONObject("response").getJSONArray("sections").getJSONObject(0).getJSONArray("hits").length();i++){

                                        musicDataList.add(APIResponse.getJSONObject("response").getJSONArray("sections").getJSONObject(0).getJSONArray("hits").getJSONObject(i).getJSONObject("result").getString("full_title"));
musicUrl.add("https://genius.com/api" + APIResponse.getJSONObject("response").getJSONArray("sections").getJSONObject(0).getJSONArray("hits").getJSONObject(i).getJSONObject("result").getString("api_path"));
musicArtist.add(APIResponse.getJSONObject("response").getJSONArray("sections").getJSONObject(0).getJSONArray("hits").getJSONObject(i).getJSONObject("result").getString("artist_names"));
musicImage.add(APIResponse.getJSONObject("response").getJSONArray("sections").getJSONObject(0).getJSONArray("hits").getJSONObject(i).getJSONObject("result").getString("song_art_image_url"));
                                    }
                                    ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this, R.layout.text_view, musicDataList);
                                    Search_results.setAdapter(arrayAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //textView.setText("Response: " + response.toString());
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                            }
                        });


// Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);


            }
        });

    }
}