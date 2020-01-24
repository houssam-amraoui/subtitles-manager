package com.manager.subtitles.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.manager.subtitles.R;
import com.manager.subtitles.model.SubModel;

import java.util.ArrayList;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.bodiholder> implements Filterable {

        private Context context;
        private ArrayList<SubModel> postspro;
        private ArrayList<SubModel> posts ;

        public  SubAdapter(Context contex, ArrayList<SubModel> posts){
                this.context=contex;
                this.posts=posts;
                this.postspro=new ArrayList<SubModel>(posts);
                }
        public SubAdapter setlist(ArrayList<SubModel> postse){
                this.posts=postse;
                this.postspro=new ArrayList<SubModel>(posts);
                return this;
                }

        @NonNull
        @Override
        public SubAdapter.bodiholder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new bodiholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sub,parent,false));
                }

        @Override
        public void onBindViewHolder(@NonNull final bodiholder holder, int position){
            holder.num.setText(posts.get(position).num+"");
            holder.lang.setText(posts.get(position).lang+"");
            holder.timestart.setText(posts.get(position).timeStart.getDefaultString());
            holder.timeend.setText(posts.get(position).timeEnd.getDefaultString());
            holder.body.setText(posts.get(position).getText());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                int position=holder.getAdapterPosition();
                @Override
             public void onClick(View view) {
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
                if (postspro.get(i).getText().toLowerCase().contains(f.toLowerCase())) {
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
            TextView num,lang,timestart,timeend,body;
            bodiholder(View itemView) {
                super(itemView);
                num = itemView.findViewById(R.id.numsub);
                lang = itemView.findViewById(R.id.lang);
                timestart =itemView.findViewById(R.id.timeS);
                timeend =itemView.findViewById(R.id.timeE);
                body =itemView.findViewById(R.id.subtext);
            }
        }
}











