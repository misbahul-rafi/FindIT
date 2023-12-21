package fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.findit.MainActivity;
import com.example.findit.R;
import com.example.findit.UsersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class fragmentSignin extends Fragment {
    FirebaseAuth mAuth;
    DatabaseReference database;
    EditText nameInput, emailInput, passwordInput;
    ArrayList<UsersModel> users;
    Button registerBtn;

    public fragmentSignin() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");
        users = new ArrayList<>();

        nameInput = view.findViewById(R.id.inputName);
        emailInput = view.findViewById(R.id.inputEmail);
        passwordInput = view.findViewById(R.id.inputPassword);

        registerBtn = view.findViewById(R.id.register);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getName = nameInput.getText().toString();
                String getEmail = emailInput.getText().toString();
                String getPassword = passwordInput.getText().toString();

                if(getName.isEmpty()){
                    nameInput.setError("Masukkan Nama");
                    return;
                }
                if(getEmail.isEmpty()){
                    emailInput.setError("Masukkan Email");
                    return;
                }
                if(getPassword.isEmpty()){
                    passwordInput.setError("Masukkan Password");
                    return;
                }

                String password = passwordInput.getText().toString();
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Authenticating...");
                progressDialog.setMessage("Please wait while we authenticate you");
                progressDialog.setCanceledOnTouchOutside(false); // Membuat dialog tidak hilang ketika area di luar dialog disentuh
                progressDialog.show();
                try {
                    createUser(getName, getEmail, password);

                }catch (Exception e){
                    Log.e("Register", e.toString());
                }finally {
                    progressDialog.dismiss();
                }

            }
        });
        return view;
    }
    public void createUser(String name, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Berhasil Membuat Akun", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();
                            String email = user.getEmail();

                            String kosong = "Belum Diatur";
                            UsersModel modelUser = new UsersModel(name, email, kosong, kosong, kosong);
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                            database.child(uid).setValue(modelUser);

                            Log.d("Uid User", uid);
                            database.child(uid).setValue(modelUser);
                            Intent main = new Intent(getActivity(), MainActivity.class);
                            startActivity(main);
                            requireActivity().finish();
                        }else {
                            Toast.makeText(getContext(), "Login Gagal " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}