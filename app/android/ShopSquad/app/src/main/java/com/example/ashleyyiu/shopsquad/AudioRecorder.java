package com.example.ashleyyiu.shopsquad;

import android.app.Activity;
import android.graphics.Point;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import cz.msebera.android.httpclient.client.HttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

import static android.media.AudioRecord.getMinBufferSize;

public class AudioRecorder extends Activity {
    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;

    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // app logo
        ImageView logo = (ImageView) findViewById(R.id.shopSquadLogo);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int imageWidth = size.x / 2;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageWidth, imageWidth);

        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        logo.setLayoutParams(layoutParams);

        logo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        logo.setImageResource(R.drawable.shopsquadlogo);

        Button btn = (Button) findViewById(R.id.recordbutton);

        btn.setOnClickListener(btnClick);

        int bufferSize = getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

    }


    private void startRecording() {

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);

        recorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }


    private void stopRecording() {
        // stops the recording activity
        if (null != recorder) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
        }
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (isRecording) {
                stopRecording();
                ((Button) v).setText("Record");
            } else {
                startRecording();
                ((Button) v).setText("Stop");
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte
        short sData[] = new short[BufferElements2Rec];

        FileOutputStream os = null;
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("voice", ".wav", getCacheDir());
            os = new FileOutputStream(tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            recorder.read(sData, 0, BufferElements2Rec);
            System.out.println("Short wirting to file" + sData.toString());
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MultipartRequest request = new MultipartRequest("hidden-hamlet-20559.herokuapp.com/messages",
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                },
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    }
                }, tmpFile, Collections.<String, String>emptyMap());

    }
}
