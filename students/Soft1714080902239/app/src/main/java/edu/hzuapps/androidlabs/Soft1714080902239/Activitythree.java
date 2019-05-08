package edu.hzuapps.myapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Activitythree extends AppCompatActivity {
    private SensorManager mSensorManager;
    private float mLux;
    private TextView mTextView;
    private Sensor mSensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitythree);


        final MyImageView myImageView = (MyImageView) findViewById(R.id.image_view);
        //点击按钮下载网络图片
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myImageView.setImageURL("https://img03.sogoucdn.com/app/a/100520093/d71a6360ba8601ff-3cc95dbc4e7ef3af-f10b1998dd2c06eae23ac3155ca27cee.jpg");
            }
        });
        //跳转到备忘录
        Button button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activitythree.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //跳转到指定网页
        Button button3=(Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 =new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("https://www.zhihu.com"));
                startActivity(intent1);
            }
        });


        mTextView = (TextView) findViewById(R.id.textView2);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
// 传感器种类-光照传感器
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(listener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onDestroy() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(listener);
        }
        super.onDestroy();
    }

    private SensorEventListener listener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
// 获取光线强度
                mLux = event.values[0];
                mTextView.setText("当前光照强度为：" + mLux);
                changeWindowBrightness((int) mLux);
            }
        }
        public void changeWindowBrightness(int brightness) {
            Window window=getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if (brightness == -1) {
                layoutParams.screenBrightness =
                        WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            }
            else {
                layoutParams.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
            }
            window.setAttributes(layoutParams);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
