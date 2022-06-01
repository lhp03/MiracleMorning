package swdm2016.gachon.mr_demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DoWakeCertifiActivity extends AppCompatActivity {

    Bitmap image;
    private TessBaseAPI mTess;
    String datapath ="";

    Button takeWakePhoto;
    Button certiWakePhoto;

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_wake_certifi);

        takeWakePhoto = findViewById(R.id.takeWakePhoto);
        certiWakePhoto = findViewById(R.id.certiWakePhoto);

        datapath = getFilesDir()+"/tesseract/";

        checkFile(new File(datapath + "tessdata/"), "kor");
        checkFile(new File(datapath + "tessdata/"), "eng");

        String lang = "kor+eng";

        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);

        takeWakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTakePhotoIntent();
            }
        });

        certiWakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable d = (BitmapDrawable)((ImageView) findViewById(R.id.photo)).getDrawable();
                image = d.getBitmap();

                String OCRresult = null;
                mTess.setImage(image);

                OCRresult = mTess.getUTF8Text();

                try {
                    long now = System.currentTimeMillis();
                    Date mDate = new Date(now);

                    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM dd HH mm");
                    SimpleDateFormat onlyDate = new SimpleDateFormat("yyyy MM dd");

                    //String nowDateStr = simpleDate.format(mDate);
                    String dueDateStr = onlyDate.format(mDate) + " 05 10";

                    //Date nowDate = simpleDate.parse(nowDateStr);
                    Date dueDate = simpleDate.parse(dueDateStr);
                    Date nowDate = onlyDate.parse(OCRresult);

                    long diff = dueDate.getTime() - mDate.getTime();
                    long diffMinute = diff / 60000;

                    if (nowDate.equals(onlyDate.parse(onlyDate.format(mDate)))
                            && ((diffMinute < 60) && (diffMinute > 0))) {
                        Snackbar.make(view, "인증이 완료되었습니다. 오늘 하루도 화이팅!!!", Snackbar.LENGTH_INDEFINITE).show();

                        Intent certifiedDateIntent = new Intent(getBaseContext(), WakeChalCertifiActivity.class);
                        certifiedDateIntent.putExtra("certification", "true");

                        startActivity(certifiedDateIntent);
                    }
                    else {
                        Snackbar.make(view, "인증에 실패하였습니다.", Snackbar.LENGTH_INDEFINITE).show();
                    }
                } catch(Exception e) {}
            }
        });
    }

    // 이미지 뷰에 찍은 사진 띄워주는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ((ImageView) findViewById(R.id.photo)).setImageURI(photoUri);
            ExifInterface exif = null;

            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            ((ImageView)findViewById(R.id.photo)).setImageBitmap(rotate(bitmap, exifDegree));
        }
    }

    // 이미지 돌아가 있으면 회전 시켜주기 메서드
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    // 찍은 사진 인텐트로 보내주기 메서드
    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                if(permissionCheck == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(DoWakeCertifiActivity.this,new String[]{Manifest.permission.CAMERA},0);
                }
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // 찍은 이미지가 저장될 파일 만드는 메서드
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    // 장치에 파일 복사하기 메서드
    private void copyFiles(String lang) {
        try{
            String filepath = datapath + "/tessdata/"+lang+".traineddata";

            AssetManager assetManager = getAssets();

            InputStream instream = assetManager.open("tessdata/"+lang+".traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            byte[] buffer = new byte[1024];
            int read;

            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFile(File dir, String lang) {
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles(lang);
        }

        if (dir.exists()) {
            String datafilepath = datapath+"/tessdata/"+lang+".traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles(lang);
            }
        }
    }
}
