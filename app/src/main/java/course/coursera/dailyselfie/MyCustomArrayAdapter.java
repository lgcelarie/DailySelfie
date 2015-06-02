package course.coursera.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Guillermo Celarie on 31/05/2015.
 */
public class MyCustomArrayAdapter extends ArrayAdapter<Selfie> {
    private final Context context;
    private final Selfie[] values;
    private View rowView;

    public MyCustomArrayAdapter(Context context, Selfie[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView textViewTitle = (TextView) rowView.findViewById(R.id.firstLine);
        TextView textViewDate = (TextView) rowView.findViewById(R.id.secondLine);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        ImageView imageView = setPic(values[position].getUri());
        textViewTitle.setText(values[position].getUri());
        textViewDate.setText("Taken: "+ values[position].getDate());
        // change the icon for Windows and iPhone
        //String s = values[position];
        /*if (s.startsWith("iPhone")) {
            imageView.setImageResource(R.drawable.no);
        } else {
            imageView.setImageResource(R.drawable.ok);
        }*/

        return rowView;
    }

    private ImageView setPic(String uri) {
        // Get the dimensions of the View
        ImageView mImageView = (ImageView) rowView.findViewById(R.id.icon);
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        if(BitmapFactory.decodeFile(uri, bmOptions) == null)
            return mImageView;
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(uri, bmOptions);
        mImageView.setImageBitmap(bitmap);
        return mImageView;
    }
}


