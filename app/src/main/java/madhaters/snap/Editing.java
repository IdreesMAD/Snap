package madhaters.snap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.darwindeveloper.horizontalscrollmenulibrary.custom_views.HorizontalScrollMenuView;
import com.darwindeveloper.horizontalscrollmenulibrary.extras.MenuItem;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

public class Editing extends AppCompatActivity {

    HorizontalScrollMenuView menuS, menuS2, menuFaceDetection, menuSnapEffect;
    ImageView imgView;
    Bitmap bitmap;
    SeekBar sb_brightness;
    SeekBar sb_contrast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);

        imgView = (ImageView) findViewById(R.id.imgView);
        menuS = (HorizontalScrollMenuView) findViewById(R.id.menuS);
        menuS2 = (HorizontalScrollMenuView) findViewById(R.id.menuS2);
        menuFaceDetection = (HorizontalScrollMenuView) findViewById(R.id.menuFaceDetection);
        menuSnapEffect = (HorizontalScrollMenuView) findViewById(R.id.menuSnapEffect);
        sb_brightness = (SeekBar) findViewById(R.id.sb_brightness);
        sb_contrast = (SeekBar) findViewById(R.id.sb_contrast);

        sb_brightness.setMax(510);
        sb_brightness.setProgress((255));

        sb_contrast.setMax(510);
        sb_contrast.setProgress(255);


        menuS.setVisibility(View.VISIBLE);
        menuS2.setVisibility(View.GONE);
        menuFaceDetection.setVisibility(View.GONE);
        menuSnapEffect.setVisibility(View.GONE);
        initMenu();

        Intent i = this.getIntent();
        Uri uri = i.getParcelableExtra("GalleryImage");
        if (uri != null) {
                try {
                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imgView.setImageBitmap(bitmap1);
                    bitmap = bitmap1;
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }else{
            Bitmap mybitmap = i.getParcelableExtra("BitmapImage");
            imgView.setImageBitmap(mybitmap);
            bitmap = mybitmap;
        }
    }
    private void initMenu() {
        menuS.addItem("FaceDetect",R.mipmap.ic_face);
        menuS.addItem("Enhance",R.mipmap.ic_enhance);
        menuS.addItem("Adjust",R.mipmap.ic_adjust);
        menuS.addItem("Crop",R.mipmap.ic_crop);
        menuS.addItem("Text",R.mipmap.ic_text);
        menuS.addItem("Frames",R.mipmap.ic_frames);
        menuS.addItem("Effects",R.mipmap.ic_effects);
        menuS.addItem("Orientation",R.mipmap.ic_orientation);
        menuS.addItem("Blur",R.mipmap.ic_blur);

        menuS.setOnHSMenuClickListener(new HorizontalScrollMenuView.OnHSMenuClickListener() {
            @Override
            public void onHSMClick(MenuItem menuItem, int position) {
                Toast.makeText(Editing.this, ""+menuItem.getText(), Toast.LENGTH_SHORT).show();

                if (menuItem.getText().equals("Enhance"))
                {
                    menuS.setVisibility(View.GONE);
                    menuS2.setVisibility(View.VISIBLE);
                    menuSnapEffect.setVisibility(View.GONE);
                    menuFaceDetection.setVisibility(View.GONE);
                    insideMenu();
                }

                else if(menuItem.getText().equals("FaceDetect")){

                    menuS.setVisibility(View.GONE);
                    menuFaceDetection.setVisibility(View.VISIBLE);
                    menuS2.setVisibility(View.GONE);
                    menuSnapEffect.setVisibility(View.GONE);
                    faceMenu();
                    faceDetection();
                }
            }
        });
    }

    private void insideMenu() {
        menuS2.addItem("Back",R.mipmap.ic_back);
        menuS2.addItem("Brightness",R.mipmap.ic_brightness);
        menuS2.addItem("Contrast",R.mipmap.ic_contrast);
        menuS2.addItem("Sharpness",R.mipmap.ic_sharpness);
        menuS2.addItem("High-Def",R.mipmap.ic_highdef);

        menuS2.setOnHSMenuClickListener(new HorizontalScrollMenuView.OnHSMenuClickListener() {
            @Override
            public void onHSMClick(MenuItem menuItem, int position) {

                if (menuItem.getText().equals("Back"))
                {
                    menuS2.setVisibility(View.GONE);
                    menuS.setVisibility(View.VISIBLE);
                    menuFaceDetection.setVisibility(View.GONE);
                    menuSnapEffect.setVisibility(View.GONE);

                }
                else if(menuItem.getText().equals("Brightness"))
                {
                    sb_contrast.setVisibility(View.GONE);
                    menuS2.setVisibility(View.VISIBLE);
                    menuS.setVisibility(View.GONE);
                    menuFaceDetection.setVisibility(View.GONE);
                    menuSnapEffect.setVisibility(View.GONE);
                    sb_brightness.setVisibility(View.VISIBLE);

                    sb_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            imgView.setImageBitmap(changeBitmapBrightness(bitmap, (float)(sb_contrast.getProgress()-1),
                                    (float)(progress-255)));
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                }

                else if(menuItem.getText().equals("Contrast"))
                {
                    sb_brightness.setVisibility(View.GONE);
                    menuS2.setVisibility(View.VISIBLE);
                    menuS.setVisibility(View.GONE);
                    menuFaceDetection.setVisibility(View.GONE);
                    menuSnapEffect.setVisibility(View.GONE);
                    sb_contrast.setVisibility(View.VISIBLE);

                    sb_contrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                            imgView.setImageBitmap(changeBitmapContrast(bitmap, (float)(sb_brightness.getProgress()-255),
                                    (float)(progress-1)));

                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                }
            }
        });
    }

    private void faceMenu()
    {
        menuFaceDetection.addItem("Back",R.mipmap.ic_back);
        menuFaceDetection.addItem("Effects",R.mipmap.ic_snapeffect);
        menuFaceDetection.addItem("Red Eye",R.mipmap.ic_redeye);
        menuFaceDetection.addItem("Face Brighter",R.mipmap.ic_brightface);

        menuFaceDetection.setOnHSMenuClickListener(new HorizontalScrollMenuView.OnHSMenuClickListener() {
            @Override
            public void onHSMClick(MenuItem menuItem, int position) {

                if (menuItem.getText().equals("Back"))
                {
                    menuFaceDetection.setVisibility(View.GONE);
                    menuS.setVisibility(View.VISIBLE);
                    menuS2.setVisibility(View.GONE);
                    menuSnapEffect.setVisibility(View.GONE);
                }

                if (menuItem.getText().equals("Effects"))
                {
                    menuSnapEffect.setVisibility(View.VISIBLE);
                    menuFaceDetection.setVisibility(View.GONE);
                    menuS2.setVisibility(View.GONE);
                    menuS.setVisibility(View.GONE);
                    snapEffectMenu();
                }
            }
        });

    }

    private void snapEffectMenu()
    {
        menuSnapEffect.addItem("Back",R.mipmap.ic_back);
        //add effects by add Item but call them from methods


        menuSnapEffect.setOnHSMenuClickListener(new HorizontalScrollMenuView.OnHSMenuClickListener() {
            @Override
            public void onHSMClick(MenuItem menuItem, int position) {

                if (menuItem.getText().equals("Back"))
                {
                    menuSnapEffect.setVisibility(View.GONE);
                    menuFaceDetection.setVisibility(View.VISIBLE);
                    menuS2.setVisibility(View.GONE);
                    menuS.setVisibility(View.GONE);
                }
            }
        });

    }

    private void faceDetection()
    {
        final Paint rectPaint = new Paint();
        rectPaint.setStrokeWidth(5);
        rectPaint.setColor(Color.WHITE);
        rectPaint.setStyle(Paint.Style.STROKE);

        final Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(bitmap,0,0,null);


        FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();

        if (!faceDetector.isOperational()) {

            Toast.makeText(Editing.this, "Face Detector could not be set up on this phone", Toast.LENGTH_SHORT).show();
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> sparseArray = faceDetector.detect(frame);

        for (int i = 0; i < sparseArray.size(); i++) {
            Face face = sparseArray.valueAt(i);
            float x1 = face.getPosition().x;
            float y1 = face.getPosition().y;
            float x2 = x1+face.getWidth();
            float y2 = y1+face.getHeight();
            RectF rectF = new RectF(x1, y1, x2, y2);
            canvas.drawRoundRect(rectF, 2, 2, rectPaint);

        }
        imgView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));
        bitmap = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
        imgView.setImageBitmap(bitmap);

        faceDetector.release();
    }

    public static Bitmap changeBitmapBrightness(Bitmap bmp, float contrast, float brightness)
    {
        contrast = 1;

        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0,brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }

    public static Bitmap changeBitmapContrast(Bitmap bmp, float contrast, float brightness)
    {
        Bitmap ret = null;
        for ( brightness = 0; brightness <=255;brightness++) {
            ColorMatrix cm = new ColorMatrix(new float[]
                    {
                            contrast, 0, 0, 0, brightness,
                            0, contrast, 0, 0, brightness,
                            0, 0, contrast, 0, brightness,
                            0, 0, 0, 1, 0
                    });

            ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

            Canvas canvas = new Canvas(ret);

            Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(cm));
            canvas.drawBitmap(bmp, 0, 0, paint);

        }
        return ret;
    }

    /*public void performCrop(Uri uri)
    {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(uri, "image*//*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }

    }*/
}
