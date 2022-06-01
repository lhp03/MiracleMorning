package swdm2016.gachon.mr_demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class WritePostActivity extends AppCompatActivity {
    static int count = 0;
    MemberInfo memberInfo;
    FirebaseUser user;
    Uri selectedURI;
    private final int IMAGE_CODE = 10;
    private final int VIDEO_CODE = 20;

    private static final String TAG = "WritePostActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        findViewById(R.id.check).setOnClickListener(onClickListener);
        findViewById(R.id.image).setOnClickListener(onClickListener);
        findViewById(R.id.video).setOnClickListener(onClickListener);


    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            case IMAGE_CODE: {
                if(resultCode == Activity.RESULT_OK){
                    ImageView imageView1 = findViewById(R.id.imageView4);
                    selectedURI= data.getData();
                    Glide.with(getApplicationContext()).load(selectedURI).into(imageView1);
                    Log.d("로오그", String.valueOf(selectedURI));

                }
                break;
            }
            case VIDEO_CODE: {
                if(resultCode == Activity.RESULT_OK){
                    ImageView imageView1 = findViewById(R.id.imageView4);
                    selectedURI = data.getData();
                    Glide.with(getApplicationContext()).load(selectedURI).into(imageView1);
                    Log.d("로오그", String.valueOf(selectedURI));
                }
                break;
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.check:
                    postUpload();
                    break;
                case R.id.image:
                    Intent intent1 = new Intent(Intent.ACTION_PICK);
                    intent1.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent1,IMAGE_CODE);
                    break;
                case R.id.video:
                    Intent intent2 = new Intent(Intent.ACTION_PICK);
                    intent2.setType(MediaStore.Video.Media.CONTENT_TYPE);
                    startActivityForResult(intent2,VIDEO_CODE);
                    break;
            }

        }
    };
    public void postUpload() {

        String title = ((EditText)findViewById(R.id.titleEditText)).getText().toString();
        String content = ((EditText)findViewById(R.id.contentEditText)).getText().toString();

        if (title.length()>0 && content.length()>0) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    memberInfo = documentSnapshot.toObject(MemberInfo.class);
                    try{
                        count = memberInfo.getCount();
                        Log.e("로오그", String.valueOf(count));
                        StorageReference riversRef = storageRef.child("posts/"+user.getUid()+count+".png");
                        UploadTask uploadTask = riversRef.putFile(selectedURI);
                        try{
                            InputStream stream = getContentResolver().openInputStream(selectedURI);
                            Bitmap img = BitmapFactory.decodeStream(stream);
                            stream.close();

                        } catch(FileNotFoundException e){
                            Log.e("로그",e.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(WritePostActivity.this,"사진저장X",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(WritePostActivity.this,"사진저장o",Toast.LENGTH_SHORT).show();
                            }
                        });


                        WritePostInfo writePostInfo = new WritePostInfo(title,selectedURI.toString(),content,user.getUid(),count);
                        uploader(writePostInfo);
                    }catch(NullPointerException e){
                        Log.e("로그",e.toString());
                    }
                }
            });


        }else {
            startToast("입력해주세요");
        }
    }
    private void uploader(WritePostInfo writePostInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(writePostInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        count=count+1;
                        db.collection("users").document(user.getUid()).update("count",count);
                        startToast("성공");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("실패");
                        Log.w("Error",e);
                    }
                });
    }

    private void myStartActivity(Class c,String media){
        Intent intent = new Intent(this,c);
        intent.putExtra("media",media);
        startActivityForResult(intent,0);
    }


    private void startToast(String msg){ Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();}
}
