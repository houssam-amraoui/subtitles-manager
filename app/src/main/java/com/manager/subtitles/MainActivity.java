package com.manager.subtitles;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import com.manager.subtitles.adapters.FileAdapter;
import com.manager.subtitles.model.SubModel;
import com.manager.subtitles.sqlite.Sql;
import com.manager.subtitles.vtt.VttRead;
import com.manager.subtitles.vtt.VttWrite;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FileAdapter adapter;
    Sql sql;
    RecyclerView recyclerView ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        sql = Sql.Getnewinstans(this);
      //  if (sql.getTableCount(Sql.File) < 1)
     //   sql.addAll();
        adapter = new FileAdapter(this,sql.getFilleEnly());
        recyclerView = findViewById(R.id.rev);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.in :
                startActivity(new Intent(MainActivity.this,ImportActivity.class));
                break;
            case R.id.ex :
                startActivity(new Intent(MainActivity.this,FusionActivity.class));
                break;
            case R.id.se :

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        adapter.setlist(sql.getFilleEnly());
        adapter.notifyDataSetChanged();
        super.onResume();
    }



}
