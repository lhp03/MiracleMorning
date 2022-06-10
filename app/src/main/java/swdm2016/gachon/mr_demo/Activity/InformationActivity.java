package swdm2016.gachon.mr_demo.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import swdm2016.gachon.mr_demo.MemberInfo;
import swdm2016.gachon.mr_demo.R;

public class InformationActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private Button buttonCheckButton;
    private static final String TAG = "InformationActivity";
    public int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        findViewById(R.id.profileButton).setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.profileButton:
                    profileUpdate();
                    break;
            }
        }
    };

    public void profileUpdate() {

        String name = ((EditText)findViewById(R.id.NameEditText)).getText().toString();
        String height = ((EditText)findViewById(R.id.HeightEditText)).getText().toString();
        String weight = ((EditText)findViewById(R.id.EditWeightText)).getText().toString();
        String age = ((EditText)findViewById(R.id.EditAgeText)).getText().toString();

        if (name!=null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();//초기화
            MemberInfo memberInfo = new MemberInfo(name,height,weight,age,count);
                db.collection("users").document(user.getUid()).set(memberInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(InformationActivity.this, "Profile Update Complete!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(InformationActivity.this, "Profile Update error!", Toast.LENGTH_SHORT).show();
                            }
                        });

        }else {
            Toast.makeText(InformationActivity.this, "회원정보를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }


}