package com.example.home.optometryapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;



import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;


public class BookAdapter extends FirestoreRecyclerAdapter<Book, BookAdapter.BookHolder> {

    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BookAdapter(@NonNull FirestoreRecyclerOptions<Book> options) {
        super(options);
    }

    //tells adapter what we want to put in each card of the layout
    @Override
    protected void onBindViewHolder(@NonNull BookHolder holder, int position, @NonNull Book model) {
        holder.textViewChapterName.setText(model.getChapterName());
        holder.textViewChapterInfo.setText(model.getChapterInfo());
        holder.textViewChapterNumber.setText(String.valueOf(model.getChapterNumber()));
                Picasso.get()
                .load(model.getImageUrl())
                .fit()
                .into(holder.imageViewDisplay);



    }

    //tells adapter which layout to inflate
    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        return new BookHolder(v);
    }

    //returns doc snapshot and this exact position in recyclerView
    public void deleteItem(int position)    {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    //inner class for bookHolder
    class BookHolder extends RecyclerView.ViewHolder {
        //create variables for TextViews
        TextView textViewChapterName;
        TextView textViewChapterNumber;
        TextView textViewChapterInfo;
        ImageView imageViewDisplay;

        public BookHolder(final View itemView) {
            super(itemView);
            textViewChapterName = itemView.findViewById(R.id.textViewChapterName);
            textViewChapterNumber = itemView.findViewById(R.id.textViewChapterNumber);
            textViewChapterInfo = itemView.findViewById(R.id.textViewChapterInfo);
            imageViewDisplay = itemView.findViewById(R.id.imageViewDisplay);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null)//ensure the user does not click deleted items
                    {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }


    public interface OnItemClickListener    {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }
}
