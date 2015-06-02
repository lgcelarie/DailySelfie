package course.coursera.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Guillermo Celarie on 20/03/2015.
 */
public class MyPictureViewer extends Activity{

    private ImageView mImageView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        setContentView(R.layout.picture_viewer);

        // Get a reference to the ImageView field
        mImageView = (ImageView) findViewById(R.id.imageView);
        File imgFile = new  File(myIntent.getStringExtra("PIC_URI"));

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            mImageView.setImageBitmap(myBitmap);

        }
        else {
            Log.v("PIC-VIEWER","IMAGE NOT FOUND");
            this.finish();
        }
    }
}
