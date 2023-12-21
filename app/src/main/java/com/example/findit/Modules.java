package com.example.findit;

import static android.app.Activity.RESULT_OK;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import fragments.fragmentHome;

public class Modules {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private Activity activity;
    FragmentManager fragmentManager;
    public Modules(Activity activity){
        this.activity = activity;
    }
    public void addItem(EditText etItemName, EditText etItemPlace, EditText etItemMessege, Boolean typeReport, String imageUri, DatabaseReference database, Context context){
        Log.d("addItem","Mendapatkan Data");

        String getItemName = etItemName.getText().toString();
        String getItemPlace = etItemPlace.getText().toString();
        String getItemMessege = etItemMessege.getText().toString();
        Boolean getItemReport = typeReport;
        String getItemImageUri = imageUri;
        Log.d("Add Item", "Mengecek isi data");
        if(getItemName.isEmpty()){
            etItemName.setError("Masukkan Nama");
        } else if (getItemPlace.isEmpty()) {
            etItemPlace.setError("Masukkan Tempat Ditemukan");
        } else if (getItemMessege.isEmpty()) {
            etItemMessege.setError("Masukkan Pesan Untuk pemilik");
        } else {
            DatabaseReference getItemId = database.push();
            getItemId.setValue(new ItemsModel(getItemName, getItemPlace, getItemMessege, getItemReport, getItemImageUri, getItemId.toString()))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context, "Berhasil", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "Gagal" + e, Toast.LENGTH_SHORT).show();
                Log.d("Add item", "Gagal pada tahap akhir");
            });

        }
    }
    public static Uri handleImageSelectionResult(int requestCode, int resultCode, Intent data) {
        Uri mImageUri = null;
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            String imageName;
//            Cursor cursor = getContentResolver().query(mImageUri, null, null, null, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//                int nameIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME);
//                imageName = cursor.getString(nameIndex);
//                cursor.close();
//                // Handle the image name or perform other operations as needed
//            }
        }
        return mImageUri;
    }
    public String getFileExtension(Uri uri, Context context) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    public interface OnImageUploadListener {
        void onUploadSuccess(String imageUrl);
        void onUploadFailure(Exception e);
    }
    public void uploadImage(Uri mImageUri, StorageReference storage, Context context, OnImageUploadListener listener) {
        Log.d("Upload Image", "Mulai Menjalankan" + mImageUri);
        ProgressDialog progressDialog = new ProgressDialog(context);
        if (mImageUri != null) {
            StorageReference fileReference = storage.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri, context));
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(context, "Berhasil", Toast.LENGTH_SHORT).show();
                        Log.d("Upload Image", "Tahap Pertama");
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageURL = uri.toString();
                            // Simpan URL gambar ke Firebase Database
                            Log.d("Upload Gambar", imageURL);
                            listener.onUploadSuccess(imageURL); // Panggil listener saat berhasil
                            progressDialog.dismiss();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Upload Image", "Gagal Mengupload");
                        listener.onUploadFailure(e); // Panggil listener saat gagal
                    })
                    .addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Mengunggah...");
                        progressDialog.setCancelable(false); // Mencegah pengguna menutup dialog
                        progressDialog.show();
                        // Buatkan layout progress disini
                        // Toast.makeText(requireContext(), progress, Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
        }
    }
    public void showItems(DatabaseReference database, ArrayList<ItemsModel> items, AdapterItems adapter) {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for(DataSnapshot item : snapshot.getChildren()){
                    ItemsModel getItem = item.getValue((ItemsModel.class));
                    getItem.setItemName(getItem.getItemName());
                    items.add(getItem);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public  void logOut(Context context, Intent main){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Authenticating...");
        progressDialog.setMessage("Please wait while we authenticate you");
        progressDialog.setCanceledOnTouchOutside(false); // Membuat dialog tidak hilang ketika area di luar dialog disentuh
        progressDialog.show();
        try {
            FirebaseAuth.getInstance().signOut();
            main = new Intent(context, MainActivity.class);
            context.startActivity(main);
        } catch (Exception e) {
            Log.e("Autentikasi", e.toString());
            throw new RuntimeException(e);
        }finally {
            progressDialog.dismiss();
        }

    }
}
