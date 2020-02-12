package com.manager.subtitles;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FusionActivity  extends AppCompatActivity {
    TextView tpath1,tpathe2;
    RadioGroup radioGroup;
    LinearLayout l1,l2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fusionsub);

        radioGroup = findViewById(R.id.rgroup);
        l1= findViewById(R.id.L1);
        l2= findViewById(R.id.L2);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.im:
                        l1.setVisibility(View.VISIBLE);
                        l2.setVisibility(View.GONE);
                        break;
                    case R.id.ex:
                        l1.setVisibility(View.GONE);
                        l2.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });

    }

}
