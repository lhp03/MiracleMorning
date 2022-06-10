package swdm2016.gachon.mr_demo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import swdm2016.gachon.mr_demo.R;

public class WakeChallengeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake_challenge);

        // 인증 창 넘어가기
        Button wakeCertifiBtn = findViewById(R.id.wakeCertifiBtn);
        wakeCertifiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WakeChalCertifiActivity.class);
                startActivity(intent);
            }
        });
    }
}