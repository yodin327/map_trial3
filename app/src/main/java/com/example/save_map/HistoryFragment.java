package com.example.save_map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryFragment extends Fragment {
    private List<HistoryItem> allItems = new ArrayList<>();
    private HistoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        MaterialToolbar toolbar = view.findViewById(R.id.history_toolbar);
        RecyclerView historyList = view.findViewById(R.id.history_list);
        historyList.setLayoutManager(new LinearLayoutManager(getContext()));
        // 샘플 데이터
        allItems.clear();
        allItems.add(new HistoryItem("2025-07-08", "14:30", "Main St", "Broadway", true, 5));
        allItems.add(new HistoryItem("2025-07-07", "09:10", "Oakridge", "Downtown", false, 5));
        allItems.add(new HistoryItem("2025-07-06", "18:45", "UBC", "Metrotown", false, 5));
        adapter = new HistoryAdapter(new ArrayList<>(allItems));
        historyList.setAdapter(adapter);
        // 아이템 클릭 시 상세 다이얼로그
        adapter.setOnItemClickListener(item -> {
            String msg = "날짜: " + item.getDate() + "\n"
                    + "시간: " + item.getTime() + "\n"
                    + "출발: " + item.getFrom() + "\n"
                    + "도착: " + item.getTo() + "\n"
                    + "즐겨찾기: " + (item.isFavorite() ? "O" : "X") + "\n"
                    + "노선: " + item.getRoute();
            new MaterialAlertDialogBuilder(requireContext())
                .setTitle("이용 상세 정보")
                .setMessage(msg)
                .setPositiveButton("확인", null)
                .show();
        });

        // 검색/필터 기능
        TextInputEditText searchEdit = view.findViewById(R.id.history_search);
        ChipGroup filterGroup = view.findViewById(R.id.history_filter_group);
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                filterAndShow(searchEdit, filterGroup);
            }
        });
        filterGroup.setOnCheckedChangeListener((group, checkedId) -> filterAndShow(searchEdit, filterGroup));

        // BarChart: 주간 이용 통계 샘플
        BarChart barChart = view.findViewById(R.id.history_bar_chart);
        List<BarEntry> entries = Arrays.asList(
            new BarEntry(1, 2), // 월
            new BarEntry(2, 1), // 화
            new BarEntry(3, 0), // 수
            new BarEntry(4, 3), // 목
            new BarEntry(5, 1), // 금
            new BarEntry(6, 0), // 토
            new BarEntry(7, 2)  // 일
        );
        BarDataSet dataSet = new BarDataSet(entries, "주간 이용 횟수");
        dataSet.setColor(getResources().getColor(R.color.md_theme_light_primary));
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        // X축 한글 요일 라벨 적용
        com.github.mikephil.charting.components.XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
        final String[] days = {"", "월", "화", "수", "목", "금", "토", "일"};
        xAxis.setValueFormatter((value, axis) -> {
            int idx = (int) value;
            if (idx >= 1 && idx <= 7) return days[idx];
            return "";
        });
        xAxis.setTextSize(14f);
        xAxis.setTextColor(getResources().getColor(R.color.md_theme_light_primary));
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.setScaleEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.invalidate();

        // PieChart: 노선별 이용 비율 샘플
        com.github.mikephil.charting.charts.PieChart pieChart = view.findViewById(R.id.history_pie_chart);
        List<com.github.mikephil.charting.data.PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new com.github.mikephil.charting.data.PieEntry(2, "Main St"));
        pieEntries.add(new com.github.mikephil.charting.data.PieEntry(1, "Oakridge"));
        pieEntries.add(new com.github.mikephil.charting.data.PieEntry(1, "UBC"));
        com.github.mikephil.charting.data.PieDataSet pieDataSet = new com.github.mikephil.charting.data.PieDataSet(pieEntries, "노선별 비율");
        pieDataSet.setColors(new int[]{
            getResources().getColor(R.color.md_theme_light_primary),
            getResources().getColor(R.color.md_theme_light_secondary),
            getResources().getColor(R.color.md_theme_light_outline)
        });
        pieDataSet.setValueTextSize(14f);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.md_theme_light_onPrimary));
        com.github.mikephil.charting.data.PieData pieData = new com.github.mikephil.charting.data.PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelColor(getResources().getColor(R.color.md_theme_light_primary));
        pieChart.setEntryLabelTextSize(14f);
        pieChart.getLegend().setEnabled(false);
        pieChart.setTouchEnabled(false);
        pieChart.invalidate();

        return view;
    }

    private void filterAndShow(TextInputEditText searchEdit, ChipGroup filterGroup) {
        String query = searchEdit.getText() != null ? searchEdit.getText().toString().trim() : "";
        int checkedId = filterGroup.getCheckedChipId();
        List<HistoryItem> filtered = new ArrayList<>();
        for (HistoryItem item : allItems) {
            boolean match = true;
            if (!query.isEmpty() && !(item.getFrom().contains(query) || item.getTo().contains(query))) {
                match = false;
            }
            if (checkedId == R.id.chip_favorite && !item.isFavorite()) match = false;
            if (checkedId == R.id.chip_weekday && isWeekend(item.getDate())) match = false;
            if (checkedId == R.id.chip_weekend && !isWeekend(item.getDate())) match = false;
            if (match) filtered.add(item);
        }
        adapter.setItems(filtered);
    }

    private boolean isWeekend(String date) {
        // date: yyyy-MM-dd, returns true if Sat/Sun
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date d = sdf.parse(date);
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(d);
            int day = cal.get(java.util.Calendar.DAY_OF_WEEK);
            return (day == java.util.Calendar.SATURDAY || day == java.util.Calendar.SUNDAY);
        } catch (Exception e) { return false; }
    }
}
