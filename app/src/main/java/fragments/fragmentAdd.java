package fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.findit.LoginActivity;
import com.example.findit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class fragmentAdd extends Fragment {
    FragmentManager fragmentManager;
    FirebaseUser user;
    Button findBtn, lossBtn, button;
    public fragmentAdd() {
        // Required empty public constructor
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            showAlertDialog();
        }

        findBtn = view.findViewById(R.id.findSwitch);
        lossBtn = view.findViewById(R.id.lossSwitch);
        findFrag();

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findFrag();
            }
        });
        lossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lossFrag();
            }
        });

        return view;
    }
    public void FragmentTransaction (Fragment fragment){
        fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flipAddFragment, fragment, null)
                .setReorderingAllowed(true)
                .commit();
    }
    public void findFrag(){
        fragmentFind findFrag = new fragmentFind();
        FragmentTransaction(findFrag);
    }
    public void lossFrag(){
        fragmentLoss lossFrag = new fragmentLoss();
        FragmentTransaction(lossFrag);
    }
    private void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Not Logged");
        alertDialogBuilder.setMessage("Please login before post the item");

        // Positive button
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform action when OK is clicked
                dialog.dismiss(); // Close the dialog
                Intent login = new Intent(requireActivity(), LoginActivity.class);
                requireActivity().startActivity(login);
            }
        });

        // Create and show the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}