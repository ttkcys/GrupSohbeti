package com.yusudhanarkadaslarlagrupsohbeti.arkadalarlagrupsohbeti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceConfig;
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceFragment;

public class ConferenceActivity extends AppCompatActivity {

    TextView meetingIDText;
    ImageView shareBtn;

    String meetingID,userID,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference);

        meetingIDText = findViewById(R.id.meeting_id_textview);
        shareBtn = findViewById(R.id.share_btn);

        meetingID = getIntent().getStringExtra("meeting_ID");
        userID = getIntent().getStringExtra("user_id");
        name = getIntent().getStringExtra("name");

        meetingIDText.setText("Toplandı Oda Numarası: " + meetingID);

        addFragment();

        shareBtn.setOnClickListener(v->{

            String appLink = "https://play.google.com/store/apps/details?id=com.yusudhanarkadaslarlagrupsohbeti.arkadalarlagrupsohbeti&hl=tr&gl=US";
            String meetingText = "Toplantıya Katıl\nToplantı Oda Numarası: " + meetingID;
            String shareText = meetingText + "\n\nUygulamayı İndirmek İçin Tıkla: " + appLink;

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,shareText);
            startActivity(Intent.createChooser(intent,"şununla paylaş"));
        });
    }

    public void addFragment() {
        long appID = AppConstans.appId;
        String appSign = AppConstans.appSign;

        ZegoUIKitPrebuiltVideoConferenceConfig config = new ZegoUIKitPrebuiltVideoConferenceConfig();
        config.turnOnCameraWhenJoining = false;
        config.turnOnMicrophoneWhenJoining = false;
        ZegoUIKitPrebuiltVideoConferenceFragment fragment = ZegoUIKitPrebuiltVideoConferenceFragment.newInstance(appID, appSign, userID, name,meetingID,config);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }
}