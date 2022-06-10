package swdm2016.gachon.mr_demo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import swdm2016.gachon.mr_demo.R;

public class WakeChalCertifiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake_chal_certifi);

        // 인증 창 넘어가기
        Button goDoWakeCertifiBtn = findViewById(R.id.goDoWakeCertifiBtn);
        goDoWakeCertifiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DoWakeCertifiActivity.class);
                startActivity(intent);
            }
        });
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intentCertified = getIntent();
        Bundle bundle = intentCertified.getExtras();
        String certifiedResult = bundle.getString("certification");

        if (certifiedResult.equals("true")) {

        }
    }*/
}
