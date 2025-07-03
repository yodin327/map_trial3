package com.example.save_map;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.compose.setContent;
import androidx.compose.foundation.layout.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.unit.dp;

public class MainActivity extends ComponentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(() -> MainScreen());
    }

    @Composable
    void MainScreen() {
        MaterialTheme(
            colorScheme = lightColorScheme(
                Color.parseColor("#8bc34a"), // Primary
                Color.parseColor("#c5e1a5"), // PrimaryContainer
                Color.parseColor("#b2ff59"), // Secondary
                Color.parseColor("#f1f8e9"), // Background
                Color.parseColor("#dcedc8")  // Surface
            ),
            content = () -> {
                Scaffold(
                    topBar = () -> TopAppBar(),
                    content = innerPadding -> {
                        Column(
                            modifier = Modifier.padding(innerPadding).fillMaxSize()
                        ) {
                            // TODO: Add Card, List, FAB, etc. as per UI spec
                            Text("BC Transit Navigator", style = MaterialTheme.typography().headlineMedium());
                        }
                    }
                );
            }
        );
    }

    @Composable
    void TopAppBar() {
        LargeTopAppBar(
            title = () -> Text("BC Transit Navigator"),
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = Color.parseColor("#8bc34a"),
                titleContentColor = Color.White
            )
        );
    }
}
