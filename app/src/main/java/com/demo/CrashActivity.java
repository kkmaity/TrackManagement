package com.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CrashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_report);

        final String stackTrace = getIntent().getStringExtra("stacktrace");
        final TextView reportTextView = (TextView) findViewById(R.id.tvError);
        reportTextView.setText(stackTrace);
    }

    public void clickedSend(View view) {
        final TextView t = (TextView) findViewById(R.id.tvError);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "yaju.rcc@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Crash Report");
        emailIntent.putExtra(Intent.EXTRA_TEXT, t.getText().toString());
        startActivity(Intent.createChooser(emailIntent, "Send Error to ..."));
    }


}
