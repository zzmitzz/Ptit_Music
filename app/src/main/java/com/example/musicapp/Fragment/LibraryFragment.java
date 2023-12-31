package com.example.musicapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Adapter.CategoryAdapter;
import com.example.musicapp.Class.Book;
import com.example.musicapp.Class.Category;
import com.example.musicapp.Data.LibraryData;
import com.example.musicapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Chưa làm =))
public class LibraryFragment extends Fragment {
    private static CategoryAdapter categoryAdapter;
    private RecyclerView rcvCategory;

    public static void setAdapter(List<Category> categories) {
        if (categoryAdapter != null) {
            categoryAdapter.setData(categories);
        }
    }

    private static List<Category> getListCategory() {

        List<Book> musicianList = LibraryData.getMusicianData();
        List<Book> musicList = LibraryData.getFavlist();
        List<Book> historyList = LibraryData.getHisList();

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Favourite Artist", musicianList,"library"));
        categories.add(new Category("Favourite Song", musicList,"library"));
        categories.add(new Category("History", historyList,"library"));
        return categories;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        AnhXa(view);
        categoryAdapter = new CategoryAdapter(this.getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);
        categoryAdapter.setData(getListCategory());
        rcvCategory.setAdapter(categoryAdapter);


        return view;
    }

    private void AnhXa(View view) {
        rcvCategory = view.findViewById(R.id.rcv_category);
    }
}
