package com.example.save_map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfileFragment extends Fragment {
    private TextView fontSizeValue;
    private TextView themeValue;
    private MaterialSwitch notificationSwitch;
    private MaterialSwitch voiceSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        // UI 요소 찾기
        fontSizeValue = view.findViewById(R.id.font_size_value);
        themeValue = view.findViewById(R.id.theme_value);
        notificationSwitch = view.findViewById(R.id.switch_notifications);
        voiceSwitch = view.findViewById(R.id.switch_voice);
        
        // 글자 크기 설정
        MaterialCardView fontSizeCard = view.findViewById(R.id.card_font_size);
        fontSizeCard.setOnClickListener(v -> showFontSizeDialog());
        
        // 테마 설정
        MaterialCardView themeCard = view.findViewById(R.id.card_theme);
        themeCard.setOnClickListener(v -> showThemeDialog());
        
        // 스위치 리스너
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 알림 설정 변경 처리
            showToast("알림이 " + (isChecked ? "켜짐" : "꺼짐"));
        });
        
        voiceSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 음성 안내 설정 변경 처리
            showToast("음성 안내가 " + (isChecked ? "켜짐" : "꺼짐"));
        });
        
        return view;
    }
    
    private void showFontSizeDialog() {
        String[] options = {"작게", "보통", "크게", "매우 크게"};
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("글자 크기 선택")
            .setItems(options, (dialog, which) -> {
                fontSizeValue.setText(options[which]);
                showToast("글자 크기: " + options[which]);
            })
            .show();
    }
    
    private void showThemeDialog() {
        String[] options = {"밝은 테마", "어두운 테마", "시스템 설정"};
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("테마 선택")
            .setItems(options, (dialog, which) -> {
                themeValue.setText(options[which]);
                showToast("테마: " + options[which]);
            })
            .show();
    }
    
    private void showToast(String message) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show();
    }
}
