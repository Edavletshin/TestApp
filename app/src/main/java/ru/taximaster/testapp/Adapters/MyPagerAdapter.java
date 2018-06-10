package ru.taximaster.testapp.Adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.taximaster.testapp.Fragments.GridFragment;
import ru.taximaster.testapp.ImagesModel.ImagesModel;

import static ru.taximaster.testapp.MainActivity.PAGE_COUNT;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private List<GridFragment> fragments;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments = new ArrayList<>();
        for (int i = 0; i < PAGE_COUNT; i++){
            fragments.add(GridFragment.getNewInstance(i));
        }

    }

    @Override
    public GridFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    public void loadImages(int page, String searchKey){
        fragments.get(page).loadImages(searchKey);
    }

    public ImagesModel getImagesModel(int page){
        return fragments.get(page).getImages();
    }

}
