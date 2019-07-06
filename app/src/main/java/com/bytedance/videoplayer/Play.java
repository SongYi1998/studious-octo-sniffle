package com.bytedance.videoplayer;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Play extends AppCompatActivity {
    private SurfaceView surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private TextView tv_start;
    private TextView tv_end;
    private SeekBar seekBar;
    private Timer timer;
    private boolean seekBarIsChaging;
    private int position;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("播放器");

        setContentView(R.layout.activity_play);
        tv_start = findViewById(R.id.tv_start);
        tv_end = findViewById(R.id.tv_end);
        seekBar = findViewById(R.id.seekbar);
        surfaceView = findViewById(R.id.surfaceView);
        player = new MediaPlayer();
        try {
            if (getIntent() != null && getIntent().getData() != null) {
                player.setDataSource(this, getIntent().getData());
            } else {
                player.setDataSource(getResources().openRawResourceFd(R.raw.yuminhong));
            }
            holder = surfaceView.getHolder();
            holder.addCallback(new PlayerCallBack());
            player.prepare();

            tv_start.setText(calculateTime(player.getCurrentPosition() / 1000) + " /");
            tv_end.setText(calculateTime(player.getDuration() / 1000));

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.seekTo(position, MediaPlayer.SEEK_CLOSEST);
                    player.setLooping(true);
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!seekBarIsChaging) {
                                seekBar.setProgress(player.getCurrentPosition());
                            }
                        }
                    }, 0, 1000);
                }
            });
            // 缓冲进度
            player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    System.out.println(percent);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        initSeekBar();

        findViewById(R.id.buttonPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!player.isPlaying()) {
                    player.start();
                }
            }
        });

        findViewById(R.id.buttonPause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.isPlaying()) {
                    player.pause();
                }
            }
        });
    }

    private void initSeekBar() {
        final int duration = player.getDuration();
        seekBar.setMax(duration);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player != null) {
                    tv_start.setText(calculateTime(player.getCurrentPosition() / 1000));
                }
                tv_end.setText(calculateTime(duration / 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarIsChaging = true;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarIsChaging = false;
                player.seekTo(seekBar.getProgress(), MediaPlayer.SEEK_CLOSEST);//在当前位置播放
                tv_start.setText(calculateTime(player.getCurrentPosition() / 1000));
            }
        });
    }

    public String calculateTime(int time) {
        int minute;
        int second;
        if (time > 60) {
            minute = time / 60;
            second = time % 60;
            if (minute >= 0 && minute < 10) {
                if (second >= 0 && second < 10) {
                    return "0" + minute + ":" + "0" + second;
                } else {
                    return "0" + minute + ":" + second;
                }
            } else {
                if (second >= 0 && second < 10) {
                    return minute + ":" + "0" + second;
                } else {
                    return minute + ":" + second;
                }
            }
        } else if (time < 60) {
            second = time;
            if (second >= 0 && second < 10) {
                return "00:" + "0" + second;
            } else {
                return "00:" + second;
            }
        }
        return "";
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            position = player.getCurrentPosition();
            player.stop();
            player.reset();
            player.release();
            player = null;
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    private class PlayerCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (player != null) {
            position = player.getCurrentPosition();
            player.stop();
            player.reset();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", position);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("position");
        } else {
            position = 0;
        }
    }
}
