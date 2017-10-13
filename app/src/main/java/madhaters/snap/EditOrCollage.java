package madhaters.snap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class EditOrCollage extends AppCompatActivity {

    Button editBtn, collageBtn;
    ImageView imgView;
    Uri bitmapImage;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BitmapSecond","here");
        setContentView(R.layout.activity_edit_or_collage);


        editBtn = (Button) findViewById(R.id.editBtn);
        collageBtn = (Button) findViewById(R.id.collageBtn);
        imgView = (ImageView) findViewById(R.id.imgView);

        Intent i = this.getIntent();
        if (i.getParcelableExtra("GalleryImage")!=null) {
            bitmapImage = i.getParcelableExtra("GalleryImage");
            imgView.setImageURI(bitmapImage);
        }
        else if (i.getParcelableExtra("BitmapImage") != null){
            bitmap = i.getParcelableExtra("BitmapImage");
            imgView.setImageBitmap(bitmap);
        }


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Intent i = new Intent(EditOrCollage.this, Editing.class);
                    if (bitmapImage != null) {
                        i.putExtra("GalleryImage", bitmapImage);
                        startActivity(i);
                    }
                    else{
                        i.putExtra("BitmapImage", bitmap);
                        startActivity(i);
                    }

                }catch (Exception e){
                    System.out.println("BitmapError:"+e.getMessage());
                    Log.d("BitmapError",e.getMessage());
                }

            }
        });

        collageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditOrCollage.this, Collage.class);
                EditOrCollage.this.startActivity(i);
            }
        });
    }
}
