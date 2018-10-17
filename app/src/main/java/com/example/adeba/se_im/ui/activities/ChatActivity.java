package com.example.adeba.se_im.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.adeba.se_im.R;
import com.example.adeba.se_im.ui.fragments.ChatFragment;
import com.example.adeba.se_im.utils.Constants;


public class ChatActivity extends AppCompatActivity {
    private Toolbar toolbar;

    public static void startActivity(Context context,
                                     String receiver,
                                     String receiverUid,
                                     String firebaseToken) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.ARG_RECEIVER, receiver);
        intent.putExtra(Constants.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        bindView();
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(getIntent().getStringExtra(Constants.ARG_RECEIVER));
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_chat,
                ChatFragment.newInstance(getIntent().getStringExtra(Constants.ARG_RECEIVER),
                        getIntent().getStringExtra(Constants.ARG_RECEIVER_UID),
                        getIntent().getStringExtra(Constants.ARG_FIREBASE_TOKEN)),
                ChatFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    private void bindView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }
}
