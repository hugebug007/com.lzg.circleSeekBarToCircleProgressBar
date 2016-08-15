package com.lzg.circleseekbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.lzg.circleseekbar.widget.MCircleSeekBar;
import com.lzg.circleseekbar.widget.MCircleSeekBar.OnSeekChangeListener;

/**
 * 
 * @author lzg
 *
 */
public class MainActivity extends BaseActivity {
	private TextView tvPerencetValue;
	private MCircleSeekBar mCircleSeekBar;
	private int progress;
	private Handler handler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvPerencetValue = getViewById(R.id.tv_perencet_set_perencet);
		mCircleSeekBar = getViewById(R.id.m_circleSeekBar_set_perencet);
		/*
		 * 下面三行代码请大家自己尝试值不同时的效果
		 */
		mCircleSeekBar.setRingMode(true);
		mCircleSeekBar.setCanMove(false);
		mCircleSeekBar.setShowProgressBar(false);
		progress = 0;
		mCircleSeekBar.setSeekBarChangeListener(new OnSeekChangeListener() {

			public void onProgressChange(MCircleSeekBar view, int newProgress) {
				tvPerencetValue.setText(Integer.toString(view.getProgress()));
			}
		});

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:

					mCircleSeekBar.setProgress(progress);
					break;

				default:
					break;
				}
			}
		};

		new Thread() {
			public void run() {
				while (true) {
					if (progress == 100)
						progress = 0;
					Message msg = Message.obtain();
					msg.what = 1;
					handler.sendMessage(msg);
					progress = progress + 1;
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}.start();
	}
}
