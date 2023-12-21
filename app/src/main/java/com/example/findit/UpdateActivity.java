package com.example.findit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fragments.fragmentHome;

public class UpdateActivity extends AppCompatActivity {

    private ItemsModel updateItem; // Deklarasi updateItem di luar metode onCreate()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("uri")) {
            String idItem = intent.getStringExtra("uri");
            DatabaseReference database = FirebaseDatabase.getInstance().getReferenceFromUrl(idItem);

            TextView nameView = findViewById(R.id.itemName);
            EditText placeView = findViewById(R.id.itemPlace);
            EditText messegeView = findViewById(R.id.itemMessege);
            Button updateBtn = findViewById(R.id.updateBtn);
            Button deleteBtn = findViewById(R.id.deleteBtn);

            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("itemName").getValue(String.class);
                        String place = snapshot.child("itemPlace").getValue(String.class);
                        String messege = snapshot.child("itemMessege").getValue(String.class);
                        String image = snapshot.child("itemMessege").getValue(String.class);
                        Boolean report = Boolean.valueOf(snapshot.child("itemMessege").getValue(String.class));
                        String id = snapshot.child("itemId").getValue(String.class);

                        nameView.setText(name);
                        placeView.setText(place);
                        messegeView.setText(messege);

                        updateItem = new ItemsModel(name, place, messege, report, image, id); // Inisialisasi updateItem di dalam onDataChange
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase Error", "Gagal mengambil data: " + error.getMessage());
                }
            });

            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (updateItem != null) {
                        String placeUpdate = placeView.getText().toString();
                        String messegeUpdate = messegeView.getText().toString();

                        updateItem.setItemPlace(placeUpdate);
                        updateItem.setItemMessege(messegeUpdate);
                        // Tambahkan data ke Firebase setelah mengubah nilai pada updateItem
                        database.setValue(updateItem);
                        Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(UpdateActivity.this, "Berhasil Menghapus", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdateActivity.this, "Gagal " + e.toString(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                }
            });
        }
    }
}
