package ru.taximaster.testapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.taximaster.testapp.Fragments.ImageFragment;
import ru.taximaster.testapp.R;

public class GridAdapter  extends ArrayAdapter {

    private final static String TAG = "GridAdapter";

    private FragmentManager manager;
    private List<String> photosUrls;

    public GridAdapter(@NonNull Context context, int resource, FragmentManager manager) {
        super(context, resource);
        photosUrls = new ArrayList<String>();
        this.manager = manager;
    }

    @Override
    public int getCount() {
        return photosUrls.size();
    }

    @Override
    public String getItem(int i) {
        return photosUrls.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, viewGroup, false);
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        final View progressbar = convertView.findViewById(R.id.progress_bar);

        try {
            Picasso.get()
                    .load(photosUrls.get(position))
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressbar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.iv_ico);
            Log.e(TAG, "Ошибка показа изображения : " + e.toString());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //увеличение картинки
                manager.beginTransaction().replace(R.id.frame, ImageFragment.getInstance(getItem(position))).addToBackStack("").commit();
            }
        });

        return convertView;
    }

    public void addImage(String url) {
        photosUrls.add(url);
        notifyDataSetChanged();
    }

    public void clear() {
        photosUrls.clear();
        notifyDataSetChanged();
    }

}