package com.manager.subtitles.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.subtitles.MainActivity;
import com.manager.subtitles.R;
import com.manager.subtitles.sqlite.Sql;
import com.manager.subtitles.SubPiager;
import com.manager.subtitles.model.SubFileOnly;

import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.bodiholder> implements Filterable {

        private Context context;
        private ArrayList<SubFileOnly> postspro;
        private ArrayList<SubFileOnly> posts ;
        private SharedPreferences defpreferences;
        private Sql db;

        public  FileAdapter(MainActivity context, ArrayList<SubFileOnly> posts){
            this.context=context;
            this.posts=posts;
            this.postspro=new ArrayList<SubFileOnly>(posts);
            db = Sql.Getnewinstans(context);
            defpreferences= PreferenceManager.getDefaultSharedPreferences(context);
           // vn= Integer.parseInt(defpreferences.getString(Kaysend.viewpost,"0"));
           // dateFormat =new  SimpleDateFormat("EEEE d MMM yyyy", new Locale("ar","ma"));
        }
        public FileAdapter setlist(ArrayList<SubFileOnly> postse){
            this.posts=postse;
            this.postspro=new ArrayList<SubFileOnly>(posts);
            return this;
        }

        @NonNull
        @Override
        public FileAdapter.bodiholder  onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
            return new bodiholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fille,parent,false));
        }

    @Override
        public void onBindViewHolder(@NonNull final bodiholder holder, int position){
            holder.name.setText(posts.get(position).name);
            holder.path.setText(posts.get(position).path);
            //block chekbox holder.checkBox.setEnabled(false);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                int position=holder.getAdapterPosition();
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,SubPiager.class);
                    intent.putExtra(Sql.File,posts.get(position).iddb);
                    context.startActivity(intent);

                }
            });
        }
   /* public void removeAt(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataSet.size());
    }*/

        public void filter(String f){
            posts.clear();
            if(f == "")
                posts=postspro;
            else {
                for (int i = 0; i < postspro.size(); i++) {
                    if (postspro.get(i).name.toLowerCase().contains(f.toLowerCase())) {
                        posts.add(postspro.get(i));
                    }
                }
            }
            notifyDataSetChanged();
        }
        @Override
        public int getItemCount() {
            return posts.size();
        }

    @Override
    public Filter getFilter() {
        return null;
    }


    class  bodiholder extends RecyclerView.ViewHolder{
            TextView name,path;
            bodiholder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.filename);
                path = itemView.findViewById(R.id.filepath);
            }
        }

        }

