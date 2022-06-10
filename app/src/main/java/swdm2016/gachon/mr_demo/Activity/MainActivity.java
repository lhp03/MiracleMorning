package swdm2016.gachon.mr_demo.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import swdm2016.gachon.mr_demo.Fragment.AlarmFragment;
import swdm2016.gachon.mr_demo.Fragment.CalendarFragment;
import swdm2016.gachon.mr_demo.Fragment.ChallengeFragment;
import swdm2016.gachon.mr_demo.Fragment.CommunityFragment;
import swdm2016.gachon.mr_demo.Fragment.ProfileFragment;
import swdm2016.gachon.mr_demo.MemberInfo;
import swdm2016.gachon.mr_demo.R;


public class MainActivity extends AppCompatActivity {

    MemberInfo memberInfo;
    AlarmFragment alarmFragment;
    CommunityFragment communityFragment;
    ChallengeFragment challengeFragment;
    ProfileFragment profileFragment;
    CalendarFragment calendarFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            myStartActivity(LogInActivity.class);
        }
        else{
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    memberInfo = documentSnapshot.toObject(MemberInfo.class);
                    try{
                        memberInfo.getName();
                    }catch(NullPointerException e){
                        myStartActivity(InformationActivity.class);
                        Log.e("로그",e.toString());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    myStartActivity(InformationActivity.class);
                    Log.e("로그","안에서 출력");
                }
            });

        }
//        findViewById(R.id.LogOutButton).setOnClickListener(onClickListener);
        BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
        alarmFragment=new AlarmFragment();
        calendarFragment = new CalendarFragment();
        //analysisFragment=new AnalysisFragment();
        communityFragment=new CommunityFragment();
        challengeFragment=new ChallengeFragment();
        profileFragment=new ProfileFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, alarmFragment).commit();
        bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.alarm){
                    onFragmentSelected(0,null);
                }
                else if(id == R.id.analisation){
                    onFragmentSelected(1,null);
                }
                else if(id == R.id.community){
                    onFragmentSelected(2,null);
                }
                else if(id == R.id.challenge){
                    onFragmentSelected(3,null);
                }
                else if(id == R.id.setting){
                    onFragmentSelected(4,null);
                }
                return true;
            }
        });
    }
    public void onFragmentSelected(int position,Bundle bundle){
        Fragment curFragment =null;
        if(position == 0){
            curFragment = alarmFragment;
        }
        else if(position == 1)
            curFragment = calendarFragment;
        else if(position == 2)
            curFragment = communityFragment;
        else if(position == 3)
            curFragment = challengeFragment;
        else if(position == 4)
            curFragment = profileFragment;

        getSupportFragmentManager().beginTransaction().replace(R.id.container,curFragment).commit();

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
//                case R.id.LogOutButton:
//                    FirebaseAuth.getInstance().signOut();
//                    myStartActivity(LogInActivity.class);
//                    break;
                case R.id.floatingActionButton:
                    myStartActivity(WritePostActivity.class);
                    break;
            }

        }
    };
    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        startActivity(intent);
    }

}
