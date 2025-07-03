package com.example.save_map;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.parseColor("#f1f8e9"));
        layout.setGravity(Gravity.CENTER);

        TextView title = new TextView(this);
        title.setText("BC Transit Navigator");
        title.setTextSize(24);
        title.setTextColor(Color.WHITE);
        title.setBackgroundColor(Color.parseColor("#8bc34a"));
        title.setPadding(32, 64, 32, 64);
        title.setGravity(Gravity.CENTER);

        layout.addView(title);

        setContentView(layout);
    }
}
