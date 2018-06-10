package ru.taximaster.testapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ru.taximaster.testapp.R;
import ru.taximaster.testapp.SwipeBackLayout;

public class ImageFragment extends Fragment {

    private View view;
    private String photoLink;

    public static ImageFragment getInstance(String photoLink) {
        ImageFragment fragment = new ImageFragment();
        fragment.photoLink = photoLink;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.imageshow,container,false);

        //восстанавливает черный фон
        view.findViewById(R.id.black).getBackground().setAlpha(255);

        //закрытие фрагмента по кнопке
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        Picasso.get().load(photoLink).into((ImageView)view.findViewById(R.id.photo));

        SwipeBackLayout swipeBackLayout = (SwipeBackLayout) view.findViewById(R.id.swipe_layout);

        swipeBackLayout.setOnPullToBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                //регулировка альфы бэкграунда, чтобы при прокрутке картинки менялся и задний фон
                view.findViewById(R.id.black).getBackground().setAlpha(255-((int) (fractionAnchor * 255)));
            }
        });


        return view;
    }

}
