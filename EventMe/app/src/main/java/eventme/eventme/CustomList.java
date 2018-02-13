package eventme.eventme;
/**
 * Created by andreas agapitos on 07-Jan-17.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomList extends BaseAdapter {

    private final Activity context;
    ArrayList<Event> events;
    ArrayList<StorageReference> images;
    public CustomList(Activity context, ArrayList<Event> events, ArrayList<StorageReference> images) {
        this.context = context;
        this.events=events;
        this.images=images;


    }
    @Override
    public int getCount() {
        return events.size();
    }
    @Override
    public Object getItem(int position) {
        return events.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_row, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.title);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_image);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        txtTitle.setText(events.get(position).getName());
        Glide
                .with(context)
                .using(new FirebaseImageLoader())
                .load(images.get(position))
                .into(imageView);

        return rowView;
    }
}