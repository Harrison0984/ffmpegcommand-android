package com.ai.mediaempire;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.mabi87.ffmpegexecutersample.FFmpegExecuter;

import java.io.File;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MediaEmpire";
    private FFmpegExecuter ffmpegCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String mp4File = "/storage/emulated/0/DCIM/Camera/VID_20151125_133604.mp4";
        String pngFile = "/storage/emulated/0/DCIM/Camera/test.png";

        ffmpegCommand = new FFmpegExecuter(getApplicationContext());
        ffmpegCommand.init();

        //String outFile = mergePng(mp4File, pngFile);
        //String outFile = mergeMp4(mp4File, mp4File);
    }

    public String mergePng(String mp4Path, String pngPath) {

        /*
        ffmpegCommand.setOnReadProcessLineListener(new FFmpegExecuter.OnReadProcessLineListener() {
            @Override
            public void onReadProcessLine(String line) {

                Log.d(TAG, line);
            }
        });
        ffmpegCommand.setOnEndProcessListener(new FFmpegExecuter.OnEndProcessListener() {
            @Override
            public void onEndProcess() {

                Log.d(TAG, "process is end");
            }
        });
        */

        ffmpegCommand.init();
        ffmpegCommand.putCommand("-y");
        ffmpegCommand.putCommand("-i");
        ffmpegCommand.putCommand(mp4Path);
        ffmpegCommand.putCommand("-i");
        ffmpegCommand.putCommand(pngPath);
        ffmpegCommand.putCommand("-filter_complex");
        ffmpegCommand.putCommand("overlay=W-w");
        ffmpegCommand.putCommand("-strict");
        ffmpegCommand.putCommand("-2");

        String outFile = new File(mp4Path).getParent() + "/" + randomString(6) + ".mp4";
        ffmpegCommand.putCommand(outFile);

        try {
            ffmpegCommand.executeCommand();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return outFile;
    }

    public String mergeMp4(String mp4First, String mp4Second) {

        String tsFirst = mp4tots(mp4First);
        String tsSecond = mp4tots(mp4Second);

        ffmpegCommand.init();
        ffmpegCommand.putCommand("-i");

        String command = "concat:" + tsFirst + "|" + tsSecond;
        ffmpegCommand.putCommand(command);
        ffmpegCommand.putCommand("-acodec");
        ffmpegCommand.putCommand("copy");
        ffmpegCommand.putCommand("-vcodec");
        ffmpegCommand.putCommand("copy");
        ffmpegCommand.putCommand("-absf");
        ffmpegCommand.putCommand("aac_adtstoasc");

        String outFile = new File(mp4First).getParent() + "/" + randomString(6) + ".mp4";
        ffmpegCommand.putCommand(outFile);

        try {
            ffmpegCommand.executeCommand();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return outFile;
    }

    private String mp4tots(String mp4Path) {

        ffmpegCommand.init();
        ffmpegCommand.putCommand("-i");
        ffmpegCommand.putCommand(mp4Path);
        ffmpegCommand.putCommand("-vcodec");
        ffmpegCommand.putCommand("copy");
        ffmpegCommand.putCommand("-acodec");
        ffmpegCommand.putCommand("copy");
        ffmpegCommand.putCommand("-vbsf");
        ffmpegCommand.putCommand("h264_mp4toannexb");

        String outFile = new File(mp4Path).getParent() + "/" + randomString(6) + ".ts";
        ffmpegCommand.putCommand(outFile);

        try {
            ffmpegCommand.executeCommand();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return outFile;
    }

    private static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }
}
