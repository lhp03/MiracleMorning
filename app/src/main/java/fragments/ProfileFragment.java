package fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import swdm2016.gachon.mr_demo.LogInActivity;
import swdm2016.gachon.mr_demo.MemberInfo;
import swdm2016.gachon.mr_demo.R;

public class ProfileFragment extends Fragment {
    public View view;
    public MemberInfo memberInfo;
    public TextView greeting;
    public TextView name;
    public TextView height;
    public TextView weight;
    public TextView age;
    public Button image;
    public Button logout_btn;
    Uri selectedURI;
    private final int IMAGE_CODE = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        greeting=view.findViewById(R.id.greetingText);
        name = view.findViewById(R.id.nameText);
        height = view.findViewById(R.id.heightText);
        weight = view.findViewById(R.id.weightText);
        age = view.findViewById(R.id.ageText);
        logout_btn = view.findViewById(R.id.Logout);
        image = view.findViewById(R.id.profileimage);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                memberInfo = documentSnapshot.toObject(MemberInfo.class);
                try {
                    greeting.setText("Hi, "+memberInfo.getName().toString()+".\n Have a great morning! ");
                    name.setText(memberInfo.getName().toString());
                    height.setText(memberInfo.getHeight().toString());
                    weight.setText(memberInfo.getWeight().toString());
                    age.setText(memberInfo.getAge().toString());

                } catch (NullPointerException e) {
                    Log.e("로그", e.toString());
                }
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LogInActivity.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent1,IMAGE_CODE);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            case IMAGE_CODE: {
                if(resultCode == Activity.RESULT_OK){
                    ImageView profileimageView = view.findViewById(R.id.profileImageView);
                    selectedURI= data.getData();
                    Glide.with(getActivity().getApplicationContext()).load(selectedURI).into(profileimageView);
                    Log.d("로오그", String.valueOf(selectedURI));
                }
                break;
            }
        }
    }
}