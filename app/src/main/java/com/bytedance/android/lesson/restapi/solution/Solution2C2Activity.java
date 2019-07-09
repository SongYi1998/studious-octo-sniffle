package com.bytedance.android.lesson.restapi.solution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bytedance.android.lesson.restapi.solution.bean.Cat;
import com.bytedance.android.lesson.restapi.solution.bean.Feed;
import com.bytedance.android.lesson.restapi.solution.bean.FeedResponse;
import com.bytedance.android.lesson.restapi.solution.bean.PostVideoResponse;
import com.bytedance.android.lesson.restapi.solution.newtork.ICatService;
import com.bytedance.android.lesson.restapi.solution.newtork.IMiniDouyinService;
import com.bytedance.android.lesson.restapi.solution.utils.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class Solution2C2Activity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;
    private static final String TAG = "Solution2C2Activity";
    private RecyclerView mRv;
    private List<Feed> mFeeds = new ArrayList<>();
    public Uri mSelectedImage;
    private Uri mSelectedVideo;
    public Button mBtn;
    private Button mBtnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution2_c2);
        initRecyclerView();
        initBtns();
        Intent intent=getIntent();
        boolean b=intent.getBooleanExtra("post",false);
        mBtn.setEnabled(b);
        mBtnRefresh.setEnabled(!b);
    }

    private void initBtns() {
        mBtn = findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mBtn.getText().toString();
                if (getString(R.string.select_an_image).equals(s)) {
                    chooseImage();
                } else if (getString(R.string.select_a_video).equals(s)) {
                    chooseVideo();
                } else if (getString(R.string.post_it).equals(s)) {
                    if (mSelectedVideo != null && mSelectedImage != null) {
                        postVideo();
                    } else {
                        throw new IllegalArgumentException("error data uri, mSelectedVideo = " + mSelectedVideo + ", mSelectedImage = " + mSelectedImage);
                    }
                } else if ((getString(R.string.success_try_refresh).equals(s))) {
                    mBtn.setText(R.string.select_an_image);
                }
            }
        });

        mBtnRefresh = findViewById(R.id.btn_refresh);
    }

    private void initRecyclerView() {
        mRv = findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                ImageView imageView = new ImageView(viewGroup.getContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setAdjustViewBounds(true);
                return new Solution2C1Activity.MyViewHolder(imageView);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                ImageView iv = (ImageView) viewHolder.itemView;
                // TODO-C2 (10) Uncomment these 2 lines, assign image url of Feed to this url variable
                final String url = mFeeds.get(i).getVideo_url();
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Solution2C2Activity.this, VideoPlayer.class);
                        intent.putExtra("openurl",url);
                        startActivity(intent);
                    }
                });
                Log.d(TAG, "onBindViewHolder: "+url);
                Glide.with(iv.getContext()).load(url).into(iv);
            }

            @Override
            public int getItemCount() {
                return mFeeds.size();
            }
        });
    }

    public void chooseImage() {
        // TODO-C2 (4) Start Activity to select an image
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE);
    }


    public void chooseVideo() {
        // TODO-C2 (5) Start Activity to select a video
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"),
                PICK_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");

        if (resultCode == RESULT_OK && null != data) {

            if (requestCode == PICK_IMAGE) {
                mSelectedImage = data.getData();
                Log.d(TAG, "selectedImage = " + mSelectedImage);
                mBtn.setText(R.string.select_a_video);
            } else if (requestCode == PICK_VIDEO) {
                mSelectedVideo = data.getData();
                Log.d(TAG, "mSelectedVideo = " + mSelectedVideo);
                mBtn.setText(R.string.post_it);
            }
        }
    }

    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        // if NullPointerException thrown, try to allow storage permission in system settings
        File f = new File(ResourceUtils.getRealPath(Solution2C2Activity.this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    private void postVideo() {
        mBtn.setText("POSTING...");
        mBtn.setEnabled(false);

        // TODO-C2 (6) Send Request to post a video with its cover image
        // if success, make a text Toast and show
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, 1);
        } else {
            new UploadTask().execute("http://test.androidcamp.bytedance.com");
//            new UploadTask().execute("http://test.androidcamp.bytedance.com/mini_douyin/invoke/video");
        }
    }

    /*
    request permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new UploadTask().execute("http://test.androidcamp.bytedance.com");
//                    new UploadTask().execute("http://test.androidcamp.bytedance.com/mini_douyin/invoke/video");
                } else {
                    Toast.makeText(this, "你拒绝了读取相册权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class UploadTask extends AsyncTask<String, Void, PostVideoResponse> {
        /**
         * The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute()
         */
        protected PostVideoResponse doInBackground(String... urls) {
            MultipartBody.Part file1 = getMultipartFromUri("cover_image", mSelectedImage);
            MultipartBody.Part file2 = getMultipartFromUri("video", mSelectedVideo);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(urls[0])
//                    .baseUrl("http://test.androidcamp.bytedance.com/mini_douyin/invoke/video/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Response<PostVideoResponse> response = null;
            try {
                response = retrofit.create(IMiniDouyinService.class).createVideo("1618268x",
                        "abcde", file1, file2).
                        execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Log.d(TAG, "doInBackground: upload status is successful? " + response.body().isSuccess());
            return response.body();
        }

        /**
         * The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground()
         */
        protected void onPostExecute(PostVideoResponse response) {
            if (response.isSuccess()) {
                Toast.makeText(Solution2C2Activity.this, "upload suceesfully!", Toast.LENGTH_SHORT).show();
                mBtn.setText(R.string.success_try_refresh);
                mBtn.setEnabled(true);
            }
        }
    }


    public void fetchFeed(View view) {
        mBtnRefresh.setText("requesting...");
        mBtnRefresh.setEnabled(false);
        // TODO-C2 (9) Send Request to fetch feed
        // if success, assign data to mFeeds and call mRv.getAdapter().notifyDataSetChanged()
        // don't forget to call resetRefreshBtn() after response received
        new FetchTask().execute("http://test.androidcamp.bytedance.com");
//        new FetchTask().execute("http://test.androidcamp.bytedance.com/mini_douyin/invoke/video");


    }


    private class FetchTask extends AsyncTask<String, Void, FeedResponse> {
        /**
         * The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute()
         */
        protected FeedResponse doInBackground(String... urls) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(urls[0])
//                    .baseUrl("http://test.androidcamp.bytedance.com/mini_douyin/invoke/video/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Response<FeedResponse> response = null;
            try {
                response = retrofit.create(IMiniDouyinService.class).getVideo().
                        execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Log.d(TAG, "doInBackground: fetch status is successful? " + response.body().isSuccess());
            return response.body();
        }

        /**
         * The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground()
         */
        protected void onPostExecute(FeedResponse response) {
            if (response.isSuccess()) {
                Toast.makeText(Solution2C2Activity.this, "fetch suceesfully!", Toast.LENGTH_SHORT).show();
                mFeeds = response.getFeeds();
                mRv.getAdapter().notifyDataSetChanged();
            }else {
                Toast.makeText(Solution2C2Activity.this, "fetch fail!", Toast.LENGTH_SHORT).show();
            }
            resetRefreshBtn();
        }
    }


    private void resetRefreshBtn() {
        mBtnRefresh.setText(R.string.refresh_feed);
        mBtnRefresh.setEnabled(true);
    }
}
