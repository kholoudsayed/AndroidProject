package com.example.kholoud.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

public class TAdapter extends RecyclerView.Adapter<TAdapter.TrailerViewHolder> {
    private static final String TAG = TAdapter.class.getSimpleName();
//Def***********************************************************
    private final Context context;
    private List<TrailerClass> trailerList;
    final private ListItemClickListener onClickListener;

    public interface ListItemClickListener {
        void OnListItemClick(TrailerClass trailerItem);
    }
// Constractor*************************************************

   public TAdapter(Context context, ArrayList<TrailerClass> trailerList, ListItemClickListener onClickListener) {
        this.context = context;
        this.trailerList = trailerList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public TAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdListItem = R.layout.trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdListItem, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TAdapter.TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(trailerList == null)
            return  0;
        else
            return  trailerList.size();

    }

    public void PutTrailer(List<TrailerClass> trailerItemList) {
        trailerList = trailerItemList;
        notifyDataSetChanged();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView listTrailerItemView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            listTrailerItemView = itemView.findViewById(R.id.tv_trailer_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
           onClickListener.OnListItemClick(trailerList.get(clickedPosition));
        }

        public void bind(int position) {
            listTrailerItemView.setText(trailerList.get(position).getName());
        }
    }
}
