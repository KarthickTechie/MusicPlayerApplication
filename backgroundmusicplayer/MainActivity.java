package musicapp.karthick.com.backgroundmusicplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {

    ListView listView;

    Intent playerIntent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.songsList);

        SongListAdapter adapter = new SongListAdapter(MainActivity.this,Songs.songsListArray,Songs.songNameArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

// getting listitem index
                int songIndex = i;

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        Player.class);
                // Sending songIndex to PlayerActivity
                in.putExtra("songIndex", songIndex);
               // setResult(100, in);
                startActivityForResult(in,100);
                // Closing PlayListView
                finish();

            }
        });

    }
}
