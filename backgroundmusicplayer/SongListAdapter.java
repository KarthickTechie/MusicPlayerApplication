package musicapp.karthick.com.backgroundmusicplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by NEW on 12/1/2015.
 */
public class SongListAdapter extends ArrayAdapter<String> {

    private Activity context;
    private Integer[] songsListArray;
    private String[] songNameArray;
    private double duration = 0;
    MediaPlayer mediaPlayer;


    public SongListAdapter(Activity context, Integer[] songsListArray, String[] songNameArray) {
        super(context, R.layout.songlistdetails, songNameArray);
        this.context = context;
        this.songsListArray = songsListArray;
        this.songNameArray = songNameArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.songlistdetails, null);
        TextView tv_songName = (TextView) view.findViewById(R.id.tvSongTitle);
        TextView tv_songDuration = (TextView) view.findViewById(R.id.tvSongDuration);
        tv_songName.setText(songNameArray[position]);
        mediaPlayer = MediaPlayer.create(context, songsListArray[position]);
        duration = mediaPlayer.getDuration();
        long mins = TimeUnit.MILLISECONDS.toMinutes((long) duration);
        duration-=TimeUnit.MINUTES.toMillis(mins);
        long secs= TimeUnit.MILLISECONDS.toSeconds((long)duration);
        StringBuilder sb = new StringBuilder(6);
        sb.append(mins<10?"0"+mins:mins);
        sb.append(":");
        sb.append(secs<10?"0"+secs:secs);
        tv_songDuration.setText(sb.toString());
        return view;
    }

}
