package com.manager.subtitles.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.subtitles.R;
import com.manager.subtitles.sqlite.Sql;
import com.manager.subtitles.adapters.SubAdapter;
import com.manager.subtitles.model.SubModel;

import java.util.ArrayList;

public class SubFragment extends Fragment {

    private static String key ="idfile";
    private static String lg ="lang";
    SubAdapter adapter;
    ArrayList<SubModel> models;
    RecyclerView recyclerView;
    Sql db;

    public static SubFragment newInstance(String index,String lang) {

        SubFragment fragment = new SubFragment();
        Bundle bundle = new Bundle();
        bundle.putString(key,index);
        bundle.putString(lg,lang);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Sql.Getnewinstans(getContext());
        String idfile =getArguments().getString(key);
        String langfile =getArguments().getString(lg);
        models =db.getSubModel(idfile,langfile);
        adapter =new SubAdapter(getContext(),models);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.home,container,false);
            recyclerView = v.findViewById(R.id.rev);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);

    return v;
    }

}
