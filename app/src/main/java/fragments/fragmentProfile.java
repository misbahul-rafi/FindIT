package fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findit.LoginActivity;
import com.example.findit.MainActivity;
import com.example.findit.Modules;
import com.example.findit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;

public class fragmentProfile extends Fragment {
    Intent login, main;
    Modules modules;
    Button loginBtn, logOutBtn, updateBtn;
    TextView nameView, phoneView, emailView, nimView, kelasView;
    View notLogged, logedIn;
    FirebaseUser user;
    DatabaseReference database;
    FragmentManager fragmentManager;
    public fragmentProfile() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        modules = new Modules(requireActivity());
        user = FirebaseAuth.getInstance().getCurrentUser();

        loginBtn = view.findViewById(R.id.loginBtn);
        logOutBtn = view.findViewById(R.id.logOutBtn);
        updateBtn = view.findViewById(R.id.updateBtn);

        notLogged = view.findViewById(R.id.notLogged);
        logedIn = view.findViewById(R.id.logedIn);

        nameView = view.findViewById(R.id.nameView);
        emailView = view.findViewById(R.id.emailView);
        phoneView = view.findViewById(R.id.phoneView);
        nimView = view.findViewById(R.id.nimView);
        kelasView = view.findViewById(R.id.kelasView);


        handleViewProfile(user, logedIn, notLogged, nameView, emailView, phoneView);

        loginBtn.setOnClickListener(v -> {
            login = new Intent(getActivity(), LoginActivity.class);
            startActivity(login);
        });
        logOutBtn.setOnClickListener(v -> modules.logOut(requireActivity(), main));
        updateBtn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentUpdateUser updateFrag = new fragmentUpdateUser();
            fragmentManager.beginTransaction().replace(R.id.flipFragment, updateFrag).commit();
        });
        return view;
    }
    public void handleViewProfile(FirebaseUser user, View logedIn, View notLogged, TextView nameView, TextView emailView, TextView phoneView){
        if (user != null) {
            database = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);
                        String nim = snapshot.child("nim").getValue(String.class);
                        String kelas = snapshot.child("kelas").getValue(String.class);
                        nameView.setText(name);
                        emailView.setText(email);
                        phoneView.setText(phone);
                        nimView.setText(nim);
                        kelasView.setText(kelas);

                        Log.d("Autentikasi", "Auth ID = " + name);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Error", error.toString());
                }
            });
            logedIn.setVisibility(View.VISIBLE);
            notLogged.setVisibility(View.GONE);
//            String uid = user.getUid();
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            String phone = user.getPhoneNumber();
//            Uri photoUrl = user.getPhotoUrl();
        }else {
            logedIn.setVisibility(View.GONE);
            notLogged.setVisibility(View.VISIBLE);
            Log.d("Autentikasi", "Belum login");
        }
    }
}