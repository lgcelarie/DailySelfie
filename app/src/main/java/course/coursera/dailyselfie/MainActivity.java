package course.coursera.dailyselfie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String mCurrentPhotoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/dailySelfies/";
    private static final long TWO_MINUTES = 120 * 1000L;
    ListView list;
    ListLoaderTask mListLoaderTask;
    private SelfiesDataSource datasource;
    private String cache_uri;
    private AlarmManager mAlarmManager;
    private Intent mSelfieNotificationIntent;
    private PendingIntent mSelfiePendingIntent;


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(),"Couldn't create image file",Toast.LENGTH_LONG).show();
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES+"/dailySelfies/");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir       /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        //cache_uri = imageFileName+".jpg";
        cache_uri = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView)findViewById(R.id.imageListView);
        File folder = new File(mCurrentPhotoPath);
        if(!folder.exists()) {
            folder.mkdirs();
            Log.e("MainActivity :: ", "Problem creating Image folder");
        }

        /*mListLoaderTask = new ListLoaderTask();
        mListLoaderTask.execute();
*/
        datasource = new SelfiesDataSource(this);
        datasource.open();

        final ArrayList<Selfie> values = datasource.getAllSelfies();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        //Selfie[] array = values.toArray(new Selfie[values.size()]);
        MyCustomArrayAdapter adapter = new MyCustomArrayAdapter(getApplicationContext(),values);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent fullSelfieIntent = new Intent(Intent.ACTION_VIEW);
                fullSelfieIntent.setDataAndType(Uri.parse("file://" + values.get(i).getUri()), "image/*");
                startActivity(fullSelfieIntent);
            }
        });
        createPendingIntents();
        createSelfieReminders();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_camera) {
            dispatchTakePictureIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            MyCustomArrayAdapter adapter = (MyCustomArrayAdapter) list.getAdapter();
            Selfie selfie = null;
            selfie = datasource.createSelfie(cache_uri,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            adapter.add(selfie);





           /* Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");*/
            //mImageView.setImageBitmap(imageBitmap);
        }
    }

    private void createPendingIntents() {
        // Create the notification pending intent
        mSelfieNotificationIntent = new Intent(MainActivity.this, SelfieNotificationReciever.class);
        mSelfiePendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, mSelfieNotificationIntent, 0);
    }
    private void createSelfieReminders() {
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Broadcast the notification intent at specified intervals
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP
                , System.currentTimeMillis() + TWO_MINUTES
                , TWO_MINUTES
                , mSelfiePendingIntent);

    }
    class ListLoaderTask extends AsyncTask<Void,Void,ArrayAdapter<String>> {
        Context context;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
            pDialog.setMessage(context.getString(R.string.loading_items));
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        public  ArrayAdapter<String> doInBackground(Void... arg0)
        {

            return null;
        }

        @Override
        protected void onPostExecute(ArrayAdapter<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            list.setAdapter(result);
            pDialog.dismiss();
        }
    }

}
