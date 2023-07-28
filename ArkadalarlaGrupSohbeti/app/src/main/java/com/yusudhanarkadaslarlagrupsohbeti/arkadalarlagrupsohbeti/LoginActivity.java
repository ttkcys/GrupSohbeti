package com.yusudhanarkadaslarlagrupsohbeti.arkadalarlagrupsohbeti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText meetingIdInput,nameInput;
    MaterialButton joinBtn,createBtn;
    SharedPreferences sharedPreferences;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        setAds();

        sharedPreferences = getSharedPreferences("name_pref", MODE_PRIVATE);

        meetingIdInput = findViewById(R.id.meeting_id_input);
        nameInput = findViewById(R.id.name_input);
        joinBtn = findViewById(R.id.join_btn);
        createBtn = findViewById(R.id.create_btn);

        nameInput.setText(sharedPreferences.getString("name",""));

        joinBtn.setOnClickListener((v)->{

            if(mInterstitialAd != null){
                mInterstitialAd.show(LoginActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        startActivity(new Intent(LoginActivity.this, ConferenceActivity.class));
                        mInterstitialAd = null;
                        setAds();
                    }
                });
            }else{
                startActivity(new Intent(LoginActivity.this, ConferenceActivity.class));
            }
            String meetingID = meetingIdInput.getText().toString();
            if(meetingID.length() != 10){
                meetingIdInput.setError("Geçersiz Toplantı Numarası");
                meetingIdInput.requestFocus();
                return;
            }

            String name = nameInput.getText().toString();
            if(name.isEmpty()){
                nameInput.setError("Kullanıcı Adı Boş Bırakılamaz...");
                nameInput.requestFocus();
                return;
            }

            startMeeting(meetingID,name);
        });

        createBtn.setOnClickListener(v->{
            if(mInterstitialAd != null){
                mInterstitialAd.show(LoginActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        startActivity(new Intent(LoginActivity.this, ConferenceActivity.class));
                        mInterstitialAd = null;
                        setAds();
                    }
                });
            }else{
                startActivity(new Intent(LoginActivity.this, ConferenceActivity.class));
            }
            String name = nameInput.getText().toString();
            if(name.isEmpty()){
                nameInput.setError("Kullanıcı Adı Boş Bırakılamaz");
                nameInput.requestFocus();
                return;
            }
            startMeeting(getRandomMeetingID(),name);
        });
    }

    void startMeeting(String meetingID, String name){
        sharedPreferences.edit().putString("name",name).apply();

        String userID = UUID.randomUUID().toString();

        Intent intent = new Intent(LoginActivity.this,ConferenceActivity.class);
        intent.putExtra("meeting_ID",meetingID);
        intent.putExtra("name",name);
        intent.putExtra("user_id",userID);
        startActivity(intent);
    }

    String getRandomMeetingID(){
        StringBuilder id = new StringBuilder();
        while (id.length() != 10){
            int random = new Random().nextInt(10);
            id.append(random);
        }
        return id.toString();
    }

    @Override
    public void onBackPressed() {
        // Uygulamayı tamamen kapatmak için
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
//
    //ca-app-pub-5060997619686611~1500309261
    public void setAds(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-5060997619686611~1500309261", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
    }
}