package com.deepsyntax.tictactoegame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Pattern;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailog_about);
        ImageView reportBug = findViewById(R.id.about_dialog_report_bug);
        ImageView sendFeedback = findViewById(R.id.about_dialog_feedback);
        ImageView shareApp = findViewById(R.id.about_dialog_share);
        TextView developerWebsiteLink = findViewById(R.id.developer_website_link);
        Pattern pattern = Pattern.compile("\\bAndroid+\\b");
        Linkify.addLinks(developerWebsiteLink, pattern, "http://www.elnuru247.github.io");
        reportBug.setOnClickListener(this);
        sendFeedback.setOnClickListener(this);
        shareApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String msg = "Download this awesome app, MASTER TICTACTOE from the playstore\nI really love the game!";
        switch (view.getId()) {
            case R.id.about_dialog_report_bug:
                sendEmail("BUG REPORT");
                break;
            case R.id.about_dialog_feedback:
                sendEmail("FEEDBACK ON MASTER TICTACTOE");
                break;
            case R.id.about_dialog_share:
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                startActivity(Intent.createChooser(intent, "Share app with..."));
                break;
        }
    }

    public void sendEmail(String subject) {
        Intent openEmail = new Intent(Intent.ACTION_SENDTO);
        openEmail.setData(Uri.parse("mailto:"));
        String[] emailRecievers = {"ibrightstar247@gmail.com"};
        openEmail.putExtra(Intent.EXTRA_EMAIL, emailRecievers);
        openEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (openEmail.resolveActivity(getPackageManager()) != null) {
            startActivity(openEmail);
        }
    }
}
