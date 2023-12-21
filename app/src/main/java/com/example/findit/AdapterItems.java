package com.example.findit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterItems extends RecyclerView.Adapter<AdapterItems.ViewItemsAdapter> {
    private ArrayList<ItemsModel> mList;
    private Context context;
    public AdapterItems(Context context, ArrayList<ItemsModel> mList){
        
        this.context = context;
        this.mList = mList;
    }


    @NonNull
    @Override
    public AdapterItems.ViewItemsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.layout_item, parent,false);
        return new ViewItemsAdapter(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItems.ViewItemsAdapter holder, int position) {
        final ItemsModel itemsProps = mList.get(position);
        holder.itemName.setText(itemsProps.getItemName());
        holder.itemPlace.setText("Find In " + itemsProps.getItemPlace());
        holder.itemMessege.setText(itemsProps.getItemMessege());
        Boolean report = itemsProps.getItemReport();
        Uri imageUri = Uri.parse(itemsProps.getItemImageUri());
        Uri itemId = Uri.parse(itemsProps.getItemId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl(itemId.toString());
                Toast.makeText(context, "Jalan" + database.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("uri", database.toString());
                context.startActivity(intent);
            }
        });
        if(report == true){
            holder.itemReport.setText("Hubungi Penemu");
        }else{
            holder.itemReport.setText("Hubungi Pemilik");
        }
        if (imageUri != null) {
            Picasso.with(holder.itemView.getContext())
                    .load(imageUri)
                    .fit()
                    .centerCrop()
                    .into(holder.itemImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Log.e("Load Image" ,"Gagal Memuat Image " + imageUri);
                        }
                    });
        } else {
            // Handling jika Uri gambar null
            holder.itemImage.setImageDrawable(null); // Mengosongkan ImageView jika Uri gambar null
        }
        Log.d("Adapter Image", String.valueOf(imageUri));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewItemsAdapter extends RecyclerView.ViewHolder {
        TextView itemName, itemPlace, itemMessege, itemReport;
        CardView cardItem;
        ImageView itemImage;
        public ViewItemsAdapter (@NonNull View itemView) {
            super(itemView);
            cardItem = itemView.findViewById(R.id.cardItem);
            itemName = itemView.findViewById(R.id.nameItemView);
            itemPlace = itemView.findViewById(R.id.placeItemView);
            itemMessege = itemView.findViewById(R.id.messegeItemView);
            itemReport = itemView.findViewById(R.id.reportItemView);
            itemImage = itemView.findViewById(R.id.imageView);
        }
    }
}
