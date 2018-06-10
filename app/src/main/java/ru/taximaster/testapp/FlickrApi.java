package ru.taximaster.testapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.taximaster.testapp.ImagesModel.ImagesModel;
import ru.taximaster.testapp.LocationModel.LocationModel;

public interface FlickrApi {
    @GET("/services/rest/?method=flickr.photos.search&sort=relevance&content_type=1&per_page=10&media=photos&format=json&nojsoncallback=1")
    Call<ImagesModel> getPhoto(@Query("api_key") String api_key, @Query("page") int page, @Query("per_page") int count, @Query("text") String search_key);

    @GET("/services/rest/?method=flickr.photos.geo.getLocation&format=json&nojsoncallback=1")
    Call<LocationModel> getLocation(@Query("api_key") String api_key, @Query("photo_id") String photo_id);
}