package musicapp.karthick.com.backgroundmusicplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class Player extends Activity {

    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnPlaylist;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private SeekBar songProgressBar;
    private TextView songTitle;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;
    private Handler mHandler = new Handler();
    private TimerHelper timerHelper;


    // Media Player
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        songTitle = (TextView)findViewById(R.id.tv_songTitle);

        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);

        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);

        currentSongIndex=getIntent().getIntExtra("songIndex",0);
        playSong(getIntent().getIntExtra("songIndex",0));

        // Mediaplayer

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {


                mp.reset();

                /*
                try {
                    if (currentSongIndex < (Songs.songsListArray.length - 1)) {
                        playSong(currentSongIndex + 1);
                        currentSongIndex = currentSongIndex + 1;
                    } else
                        playSong(0);
                    currentSongIndex = 0;
                } catch (Exception e) {
                    Log.e("MUSIC SERVICE", "Error setting data source", e);
                }
                mp.prepareAsync();
                /*
                if (mp.getCurrentPosition()== mp.getDuration()){

                    if(currentSongIndex < (Songs.songsListArray.length - 1)){
                        mp.reset();
                        playSong(currentSongIndex + 1);
                        currentSongIndex = currentSongIndex + 1;
                    }else{
                        // play first song
                        mp.reset();
                        playSong(0);
                        currentSongIndex = 0;
                    }

                }
            */
            }
        });

        songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int mProgress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mp.getDuration();
                int currentPosition = timerHelper.progressToTimer(mProgress, totalDuration);

                // forward or backward to certain seconds
                mp.seekTo(currentPosition);

                // update timer progress again
                updateProgressBar();

            }
        });

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        timerHelper = new TimerHelper();
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if(mp.isPlaying()){
                    if(mp!=null){
                        mp.pause();
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.btn_play);
                    }
                }else{
                    // Resume song
                    if(mp!=null){
                        mp.start();
                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    }
                }

            }
        });


        // song forward
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if(currentPosition + seekForwardTime <= mp.getDuration()){
                    // forward song
                    mp.seekTo(currentPosition + seekForwardTime);
                }else{
                    // forward to end position
                    mp.seekTo(mp.getDuration());
                }
            }
        });

        /**
         * Backward button click event
         * Backward song to specified seconds
         * */
        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - seekBackwardTime >= 0){
                    // forward song
                    mp.seekTo(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    mp.seekTo(0);
                }

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not

                if(currentSongIndex < (Songs.songsListArray.length - 1)){
                    mp.reset();
                    playSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                }else{
                    // play first song
                    mp.reset();
                    playSong(0);
                    currentSongIndex = 0;
                }

            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(currentSongIndex > 0){
                    mp.reset();
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                }else{
                    // play last song
                    mp.reset();
                    playSong(Songs.songsListArray.length - 1);
                    currentSongIndex = Songs.songsListArray.length - 1;
                }

            }
        });




    }


    public void  playSong(int songIndex){
        // Play song


            mp = MediaPlayer.create(getApplicationContext(), Songs.songsListArray[songIndex]);
            mp.start();
            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.btn_pause);
            songTitle.setText(Songs.songNameArray[songIndex]);
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);
        updateProgressBar();



    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText(""+ timerHelper.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText(""+ timerHelper.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(timerHelper.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);


            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };




}
