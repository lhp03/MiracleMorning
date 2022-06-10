package swdm2016.gachon.mr_demo.Adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import swdm2016.gachon.mr_demo.R;
import swdm2016.gachon.mr_demo.WritePostInfo;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    int index;
    private ArrayList<WritePostInfo> mDataSet;
    private Activity activity;
    public static class PostViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public PostViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public PostAdapter(Activity activity, ArrayList<WritePostInfo> myDataSet){
        mDataSet = myDataSet;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final PostViewHolder postViewHolder = new PostViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView textView = cardView.findViewById(R.id.TitleText);
        textView.setText(mDataSet.get(position).getTitle());

        LinearLayout imageLayout = cardView.findViewById(R.id.imageLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView imageView = new ImageView(activity);

        FirebaseStorage db = FirebaseStorage.getInstance();
        StorageReference storageRef = db.getReference();
        storageRef.child("posts/"+mDataSet.get(position).getPublisher()+mDataSet.get(position).getIndex()+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시

                Glide.with(activity.getApplicationContext())
                        .load(uri)
                        .into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                Toast.makeText(activity.getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });
        imageView.setLayoutParams(layoutParams);
        imageLayout.addView(imageView);

        TextView textView1 = new TextView(activity);
        textView1.setLayoutParams(layoutParams);
        textView1.setText(mDataSet.get(position).getContent());
        imageLayout.addView(textView1);

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
