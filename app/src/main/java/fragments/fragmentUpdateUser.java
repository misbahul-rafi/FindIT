package fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.findit.R;
import com.example.findit.UsersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class fragmentUpdateUser extends Fragment {
    EditText etName, etNim, etKelas, etPhone;
    DatabaseReference database;
    Button sendUpdateBtn;
    FirebaseUser user;



    public fragmentUpdateUser() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_user, container, false);

        database = FirebaseDatabase.getInstance().getReference("users");

        etName = view.findViewById(R.id.editName);
        etNim = view.findViewById(R.id.editNim);
        etKelas = view.findViewById(R.id.editKelas);
        etPhone = view.findViewById(R.id.editPhone);

        sendUpdateBtn = view.findViewById(R.id.sendUpdate);

        user = FirebaseAuth.getInstance().getCurrentUser();

        sendUpdateBtn.setOnClickListener(v -> {
            try {
                database.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String updateName = etName.getText().toString();
                        String updateNim = etNim.getText().toString();
                        String updateKelas = etKelas.getText().toString();
                        String updatePhone = etPhone.getText().toString();
                        if(updateName.isEmpty()){
                            updateName = snapshot.child("name").getValue(String.class);
                        }
                        if(updateNim.isEmpty()){
                            updateNim = snapshot.child("nim").getValue(String.class);
                        }
                        if(updateKelas.isEmpty()){
                            updateKelas = snapshot.child("absen").getValue(String.class);
                        }
                        if(updatePhone.isEmpty()){
                            updatePhone = snapshot.child("phone").getValue(String.class);
                        }

                        UsersModel userModel = new UsersModel(updateName, user.getEmail(), updatePhone, updateNim, updateKelas);
                        database.child(user.getUid()).setValue(userModel)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        return;
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Gagal Memperbarui", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }catch (Exception e){
                
            }finally {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentHome home = new fragmentHome();
                fragmentManager.beginTransaction().replace(R.id.flipFragment, home).commit();
            }
//
//            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                    .setDisplayName(updateName)
//                    // .setPhotoUri(Uri.parse("URL_foto_pengguna")) // Opsional: menambahkan URL foto
//                    .build();
//
//            user.updateProfile(profileUpdates)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            // Profil pengguna berhasil diperbarui
//                            // Lakukan tindakan sesuai kebutuhan
//                        }
//                    });
        });


        return view;
    }
}