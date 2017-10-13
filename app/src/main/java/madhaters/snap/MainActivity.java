package madhaters.snap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Button cameraBtn, photoLibBtn, mainPageBtn;
    ImageView imgView;
    public boolean enabled = false;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBtn = (Button) findViewById(R.id.cameraBtn);
        photoLibBtn = (Button) findViewById(R.id.photoLibBtn);
        imgView = (ImageView) findViewById(R.id.imgView);
        mainPageBtn = (Button) findViewById(R.id.mainPageBtn);
        mainPageBtn.setEnabled(false);

        if (!hasCamera())
        {
            cameraBtn.setEnabled(false);
        }

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

            }
        });


        photoLibBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                File photoDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
                String photoDirPath = photoDir.getPath();
                Uri data = Uri.parse(photoDirPath);
                photoPickerIntent.setDataAndType(data, "image/*");
                startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
            }
        });

        mainPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enabled == true)
                {


                }
            }
        });
    }

    Uri ImageUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ((requestCode == IMAGE_GALLERY_REQUEST) && (resultCode == RESULT_OK))
        {
            ImageUri = data.getData();
                if (ImageUri == null)
                {
                //    Toast.makeText(this,"Image is null", Toast.LENGTH_SHORT).show();
                    Log.d("Error","null");
                    return;
                }
                else
                {
                 //   Toast.makeText(this,"Image is not null",Toast.LENGTH_SHORT).show();
                    Log.d("NoError",ImageUri.getPath());
                }

                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(ImageUri);
                    Bitmap photo= BitmapFactory.decodeStream(inputStream);
                    imgView.setImageBitmap(photo);

                    getImage(photo);
                    mainPageBtn.setEnabled(true);
                    enabled = true;


                } catch (FileNotFoundException e) {
                    System.out.println("ErrorImage"+e.getMessage());
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_SHORT).show();
                }

        }

        else if ((requestCode == REQUEST_IMAGE_CAPTURE) && (resultCode == RESULT_OK))
        {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            imgView.setImageBitmap(photo);
            mainPageBtn.setEnabled(true);
            enabled = true;
            sendCameraImage(photo);
        }
    }

    public void sendCameraImage(Bitmap photo)
    {
        try {
            Intent i = new Intent(MainActivity.this, EditOrCollage.class);
            i.putExtra("BitmapImage",photo);
            startActivity(i);
        }catch (Exception e){
            System.out.println("BitmapError:"+e.getMessage());
            Log.d("BitmapError",e.getMessage());
        }
    }


    private boolean hasCamera()
    {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }
    public void getImage(Bitmap photo)
    {
        try {
            Intent i = new Intent(MainActivity.this, EditOrCollage.class);
            i.putExtra("GalleryImage",ImageUri);
            startActivity(i);
        }catch (Exception e){
            System.out.println("BitmapError:"+e.getMessage());
            Log.d("BitmapError",e.getMessage());
        }
    }

}
