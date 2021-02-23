package com.example.news;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.news.models.Article;

import java.util.ArrayList;
import java.util.List;



public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {
    private List<Article> articles;
    private List<Article> articlesOld;
    private Context context;
    private static OnItemClickListener onItemClickListener;

    public Adapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
        this.articlesOld = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Article model = articles.get(position);
        RequestOptions requestOptions = new RequestOptions();
        RequestOptions placeholder = requestOptions.placeholder(Utils.getRandomDrawbleColor());
        placeholder.error(Utils.getRandomDrawbleColor());
        placeholder.diskCacheStrategy(DiskCacheStrategy.ALL);
        placeholder.centerCrop();

        Glide.with(context).load(model.getUrlToImage()).apply(requestOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);
        holder.tvTitle.setText(model.getTitle());
        holder.tvDesc.setText(model.getDescription());
        holder.tvSource.setText(model.getSource().getName());
        holder.tvTime.setText(" \u2022 "+Utils.DateToTimeFormat(model.getPublishedAt()));
        holder.tvPublishedAt.setText(Utils.DateFormat(model.getPublishedAt()));
        holder.tvAuthor.setText(model.getAuthor());

    }

    @Override
    public int getItemCount() {
        if(articles != null) return articles.size();
        return 0;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvAuthor, tvPublishedAt, tvTitle, tvDesc, tvSource, tvTime;
        ImageView imageView;
        ProgressBar progressBar;
//        OnItemClickListener onItemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.author);
            tvPublishedAt = itemView.findViewById(R.id.publishedAt);
            tvTitle = itemView.findViewById(R.id.title);
            tvDesc = itemView.findViewById(R.id.desc);
            tvSource = itemView.findViewById(R.id.source);
            tvTime = itemView.findViewById(R.id.time);

            imageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.progress_load_photo);

//            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(v, getLayoutPosition());
                    }
                }
            });

        }

    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(!strSearch.isEmpty()){
                    List<Article> list = new ArrayList<>();
                    for(Article article : articlesOld){
                        if(article.getTitle().toLowerCase().contains(strSearch)){
                            list.add(article);
                        }
                    }
                    articles = list;
                }
                else {
                    articles = articlesOld;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = articles;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                articles = (List<Article>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
