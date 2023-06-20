package com.example.eshopproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AddItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private Button chooseImageButton;
    private Button addButton;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private CollectionReference itemsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Initialize Firebase Firestore
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        itemsCollection = firebaseFirestore.collection("items");

        // Initialize views
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priceEditText = findViewById(R.id.priceEditText);
        chooseImageButton = findViewById(R.id.chooseImageButton);
        addButton = findViewById(R.id.addItem);
        selectedImageView = findViewById(R.id.selectedImageView);

        // Set click listener for choose image button
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Set click listener for add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
    }

    // Open image picker
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData();
                selectedImageView.setImageURI(selectedImageUri);
            }
        }
    }

    // Add item to Firebase collection
    private void addItem() {
        String description = descriptionEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();

        // Perform validation checks
        if (description.isEmpty() || price.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill in all fields and choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new item object with the provided details
        Item item = new Item(description, Double.parseDouble(price));

        // Add the item to Firebase
        uploadItemImage(item);
    }

// Upload item image to Firebase Storage
    private void uploadItemImage(Item item) {
        // Create a unique filename for the image in Firebase Storage
        String fileName = UUID.randomUUID().toString();

        // Create a reference to the Firebase Storage location where the image will be stored
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("item_images")
                .child(fileName);

        // Upload the image to Firebase Storage
        storageReference.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Retrieve the download URL of the uploaded image
                    storageReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                item.setImageUrl(imageUrl);
                                saveItemToFirestore(item);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to get image download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Save the item to Firestore
    private void saveItemToFirestore(Item item) {
        itemsCollection.add(item)
                .addOnSuccessListener(documentReference -> {
                    String itemId = documentReference.getId();
                    item.setId(itemId);
                    Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish the activity after successful item addition
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
