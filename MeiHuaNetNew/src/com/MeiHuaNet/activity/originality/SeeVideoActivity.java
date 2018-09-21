package com.MeiHuaNet.activity.originality;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.VideoView;

import com.MeiHuaNet.R;
import com.MeiHuaNet.activity.BaseActivity;

public class SeeVideoActivity extends BaseActivity {

	VideoView videoView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_seevideo);
		String url = getIntent().getStringExtra("url");

		initView();
		seeVideo(url);
	}

	private void initView() {
		videoView = (VideoView) findViewById(R.id.videoview);
	}

	private void seeVideo(String url) {
		// Uri uri = Uri.parse("/sdcard/download/test.mp4");
		// videoView.setVideoURI(uri);
		videoView.setVideoPath(Environment.getExternalStorageDirectory()
				.getPath() + "download/test.mp4");
		videoView.setMediaController(new MediaController(this));
		videoView.requestFocus();
		videoView.start();
	}
}
