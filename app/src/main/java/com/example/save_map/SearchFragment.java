package com.example.save_map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.search.SearchBar;

public class SearchFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        SearchBar searchBar = view.findViewById(R.id.search_bar);
        ChipGroup filterChipGroup = view.findViewById(R.id.filter_chip_group);
        RecyclerView routeResultList = view.findViewById(R.id.route_result_list);
        // TODO: 어댑터, 이벤트, 검색/경로 데이터 연동 구현
        return view;
    }
}
