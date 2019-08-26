package com.example.kholoud.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MAdapter extends RecyclerView.Adapter<MAdapter.MovieViewHolder> {
    private static final String TAG = MAdapter.class.getSimpleName();

    private List<MoviesClass> movieItemList;
    private final Context context;
    final private ListItemClickListener onClickListener;

    public interface ListItemClickListener {
        void OnListItemClick(MoviesClass movieItem);
    }

    public MAdapter(List<MoviesClass> movieItemList, ListItemClickListener onClickListener, Context context) {

        this.movieItemList = movieItemList;

        this.onClickListener = onClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movieItemList == null ? 0 : movieItemList.size();
    }

    public void setMovieData(List<MoviesClass> movieItemList) {
        this.movieItemList = movieItemList;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView listMovieItemView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            listMovieItemView = itemView.findViewById(R.id.iv_item_poster);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
          MoviesClass movieItem = movieItemList.get(listIndex);
            listMovieItemView = itemView.findViewById(R.id.iv_item_poster);
            String posterPathURL = Network.buildPosterUrl(movieItem.getImage());
            try {
                Picasso.with(context)
                        .load(posterPathURL)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(listMovieItemView);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            onClickListener.OnListItemClick(movieItemList.get(clickedPosition));
        }
    }

}
