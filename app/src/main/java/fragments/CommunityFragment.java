package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import adapter.PostAdapter;
import swdm2016.gachon.mr_demo.GalleryAdapter;
import swdm2016.gachon.mr_demo.R;
import swdm2016.gachon.mr_demo.WritePostInfo;

public class CommunityFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_community, container, false);



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<WritePostInfo> postList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("로오그", document.getId() + " => " + document.getData());
                                postList.add(new WritePostInfo(
                                        document.getData().get("title").toString(),
                                        document.getData().get("imageURI").toString(),
                                        document.getData().get("content").toString(),
                                        document.getData().get("publisher").toString(),
                                        document.getData().get("index").hashCode()
                                ));
                            }
                            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                            RecyclerView.Adapter myadapator = new PostAdapter(getActivity(),postList);
                            recyclerView.setAdapter(myadapator);
                        } else {
                            Log.d("로오그", "Error getting documents: ", task.getException());
                        }
                    }
                });



        return view;
    }
}