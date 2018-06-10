package ru.taximaster.testapp.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.taximaster.testapp.Adapters.GridAdapter;
import ru.taximaster.testapp.App;
import ru.taximaster.testapp.ImagesModel.ImagesModel;
import ru.taximaster.testapp.ImagesModel.Photo;
import ru.taximaster.testapp.MainActivity;
import ru.taximaster.testapp.R;

import static ru.taximaster.testapp.MainActivity.API_KEY;
import static ru.taximaster.testapp.MainActivity.TOTAL_COUNT_IMAGES;

public class GridFragment extends Fragment {

    final static String PAGE = "page";

    private ImagesModel imagesModel;
    private int backgroundColor;
    private int currentPage;
    private GridAdapter adapter;
    private View view;

    public static GridFragment getNewInstance(int page) {
        GridFragment gf = new GridFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        gf.setArguments(args);
        return gf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new GridAdapter(getActivity(), R.layout.grid_item,getActivity().getSupportFragmentManager());
        Random rnd = new Random();
        backgroundColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        currentPage = getArguments().getInt(PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.grid_fragment, null);
        view.setBackgroundColor(backgroundColor);
        ((GridView) view.findViewById(R.id.grid)).setAdapter(adapter);
        return view;
    }

    public void loadImages(String searchKey){
        App.getApi().getPhoto(API_KEY, (currentPage+1), TOTAL_COUNT_IMAGES, searchKey).enqueue(new Callback<ImagesModel>() {
            @Override
            public void onResponse(Call<ImagesModel> call, Response<ImagesModel> response) {
                imagesModel = response.body();
                adapter.clear();
                for (Photo imageModel : response.body().getPhotos().getPhoto()){
                    String url = "http://farm" + imageModel.getFarm() + ".static.flickr.com/"
                            + imageModel.getServer() + "/" + imageModel.getId() + "_" + imageModel.getSecret() + ".jpg";
                    adapter.addImage(url);
                }
            }

            @Override
            public void onFailure(Call<ImagesModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                Log.e("GridFragment", t.toString());
            }
        });
    }

    public ImagesModel getImages(){
        return  imagesModel;
    }

}
