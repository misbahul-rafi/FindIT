package fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findit.AdapterItems;
import com.example.findit.ItemsModel;
import com.example.findit.Modules;
import com.example.findit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class fragmentHome extends Fragment{
    DatabaseReference database;
    RecyclerView cardsLayout;
    ArrayList<ItemsModel> items;
    AdapterItems adapter;
    Modules modules;
    StorageReference storage;
    public fragmentHome() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        modules = new Modules(requireActivity());
        items = new ArrayList<>();
        adapter = new AdapterItems(getContext(), items);
        cardsLayout = view.findViewById(R.id.cardsLayout);
        cardsLayout.setLayoutManager(new LinearLayoutManager(requireContext()));
        cardsLayout.setItemAnimator(new DefaultItemAnimator());

        cardsLayout.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference("items");
        storage = FirebaseStorage.getInstance().getReference("uploads");

        modules.showItems(database, items, adapter);
        return view;
    }
}