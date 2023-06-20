package com.example.eshopproject;


import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private FirebaseFirestore db;
    private static final int ADD_ITEM_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        RecyclerView itemRecyclerView = findViewById(R.id.itemRecyclerView);
        Button addItemButton = findViewById(R.id.addItemButton);
        Button refreshButton = findViewById(R.id.refreshButton);

        // Set the RecyclerView layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        itemRecyclerView.setLayoutManager(layoutManager);

        // Initialize item list and adapter
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);
        itemRecyclerView.setAdapter(itemAdapter);

        // Fetch items from Firebase Firestore collection
        fetchItems();
        refreshButton.setOnClickListener(v->{
            fetchItems();
        });
        addItemButton.setOnClickListener(v -> {
            // Create an intent to navigate to the AddItemActivity
            Intent intent = new Intent(this, AddItemActivity.class);

            // Start the activity and wait for a result
            startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
        });
    }


    private void fetchItems() {
        db.collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Item item = documentSnapshot.toObject(Item.class);
                        itemList.add(item);
                    }
                    itemAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle error while fetching items
                    Toast.makeText(ShopActivity.this, "Failed to fetch items", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                boolean itemAdded = data.getBooleanExtra("itemAdded", false);

                if (itemAdded) {
                    Toast.makeText(ShopActivity.this, "item added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShopActivity.this, "Failed to add items", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
