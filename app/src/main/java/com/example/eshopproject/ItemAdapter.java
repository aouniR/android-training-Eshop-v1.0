package com.example.eshopproject;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView Description;
        private ImageView itemImageView;
        private TextView price;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Description = itemView.findViewById(R.id.description);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            price = itemView.findViewById(R.id.price);
        }

        public void bind(Item item) {
            Description.setText(item.getDescription());
            price.setText(String.valueOf(item.getPrice()));

            // Load image using Picasso or any other image loading library
            Picasso.get().load(item.getImageUrl()).into(itemImageView);
        }
    }
}
