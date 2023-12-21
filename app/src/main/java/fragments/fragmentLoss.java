package fragments;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findit.Modules;
import com.example.findit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class fragmentLoss extends Fragment {
    EditText etItemName, etItemPlace, etItemMessege;
    TextView imageNameView;
    FragmentManager fragmentManager;
    Button lossPostBtn, pickimage;
    DatabaseReference database;
    StorageReference storage;
    Modules modules;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    ContentResolver cR;
    public fragmentLoss() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loss, container, false);

        Intent intent = new Intent();
        lossPostBtn = view.findViewById(R.id.lossPostBtn);
        database = FirebaseDatabase.getInstance().getReference("items");
        storage = FirebaseStorage.getInstance().getReference("uploads");
        modules = new Modules(requireActivity());

        etItemName = view.findViewById(R.id.inputItemName);
        etItemPlace = view.findViewById(R.id.inputItemPlace);
        etItemMessege = view.findViewById(R.id.inputItemMessege);
        pickimage = view.findViewById(R.id.pickImageLoss);
        imageNameView = view.findViewById(R.id.imageNameView);

        lossPostBtn.setOnClickListener(v -> {
            String imageUri = "No Image";
            if (mImageUri != null) {
                imageUri = mImageUri.toString();
                Log.d("Pengecekan nilai image", imageUri);
                // Panggil method uploadImage dari Modules
                modules.uploadImage(mImageUri, storage, requireActivity(), new Modules.OnImageUploadListener() {
                    @Override
                    public void onUploadSuccess(String imageUrl) {
                        // Jika upload gambar berhasil, tambahkan item ke database
                        modules.addItem(etItemName, etItemPlace, etItemMessege, false, imageUrl, database, getContext());
                        etItemPlace.setText("");
                        etItemMessege.setText("");
                        mImageUri = null;
                    }

                    @Override
                    public void onUploadFailure(Exception e) {
                        // Jika upload gagal, tampilkan pesan kesalahan
                        Toast.makeText(getContext(), "Gagal mengunggah gambar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.d("FragmentInput", "mImageUri is null");
                // Jika tidak ada gambar terpilih, tambahkan item ke database dengan URL gambar default
                modules.addItem(etItemName, etItemPlace, etItemMessege, false, imageUri, database, getContext());
            }
        });
        pickimage.setOnClickListener(v -> {
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mImageUri = modules.handleImageSelectionResult(requestCode, resultCode, data);
        Log.d("Activity Result", mImageUri != null ? mImageUri.toString() : "No image selected");
        if (mImageUri != null) {
            // Lakukan hal lain yang diperlukan setelah memilih gambar
            // Seperti menampilkan gambar, melakukan upload, dll.
            imageNameView.setText("Some hint here"); // Contoh penyesuaian hint
//            Picasso.with(getContext()).load(mImageUri).fit().centerCrop().into(imageView);
        }
    }
}