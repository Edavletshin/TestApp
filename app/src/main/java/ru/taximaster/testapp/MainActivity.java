package ru.taximaster.testapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.taximaster.testapp.Adapters.MyPagerAdapter;
import ru.taximaster.testapp.Fragments.MapFragment;
import ru.taximaster.testapp.ImagesModel.ImagesModel;
import ru.taximaster.testapp.ImagesModel.Photo;
import ru.taximaster.testapp.LocationModel.Location;
import ru.taximaster.testapp.LocationModel.LocationModel;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "03d0a7ae3a6ca2efbb122e01f74c35bc";
    public static final int TOTAL_COUNT_IMAGES = 20;
    public static final int PAGE_COUNT = 4;
    private int currentPage = 0;

    private EditText searchText;
    private Button searchButton;
    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;
    private String searchKey = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pd = new ProgressDialog(this);
        pd.setTitle("Понимаем...");
        pd.setMessage("Подождите еще немножко...");
        pd.setCancelable(false);

        searchText = (EditText) findViewById(R.id.editText);
        searchButton = (Button) findViewById(R.id.button);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if (!searchKey.equals(""))
                    pagerAdapter.loadImages(currentPage, searchKey);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchKey = searchText.getText().toString();
                if (!searchKey.equals(""))
                    pagerAdapter.loadImages(currentPage, searchKey);
            }
        });

        findViewById(R.id.map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                //получает данные с текущего фрагмента о фотографиях, чтобы получить их локацию
                final ImagesModel imagesModel = pagerAdapter.getImagesModel(currentPage);

                if (imagesModel!=null){
                    final int[] currentItem = {0};
                    final List<Person> personList = new ArrayList<>();
                    for (final Photo photo : imagesModel.getPhotos().getPhoto()){
                        //зарпосы на получение локации фотографий
                        App.getApi().getLocation(API_KEY,photo.getId()).enqueue(new Callback<LocationModel>() {
                            @Override
                            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                                currentItem[0]++;
                                if (response.body().getStat().equals("ok")) {
                                    //если у фотографии есть локация, то создается объект с ее локацией и ссылкой на фотографию
                                    Location location = response.body().getPhoto().getLocation();

                                    String url = "http://farm" + photo.getFarm() + ".static.flickr.com/"
                                            + photo.getServer() + "/" + photo.getId() + "_" + photo.getSecret() + ".jpg";

                                    personList.add(new Person(new LatLng(location.getLatitude(),location.getLongitude()),url));
                                }

                                if (currentItem[0]==imagesModel.getPhotos().getPhoto().size()){
                                    //если загружена последняя локация фотографий, то запускается фрагмент карты.
                                    Toast.makeText(MainActivity.this, personList.size() + "/" + currentItem[0], Toast.LENGTH_SHORT).show();
                                    pd.cancel();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, MapFragment.newInstance(personList)).addToBackStack("").commit();
                                }

                            }

                            @Override
                            public void onFailure(Call<LocationModel> call, Throwable t) {
                                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

    }

}
