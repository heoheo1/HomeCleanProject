package com.hj.homecleanproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;
import java.util.concurrent.Flow;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PictureFragment extends Fragment {

    private GalleryAdapter adapter;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager manager;

    private String filePath;
    private Bitmap bitmap;
    private File[] imageList;

    public static PictureFragment newInstance() {
        return new PictureFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        adapter = new GalleryAdapter();
        recyclerView = view.findViewById(R.id.galleryRecycler);
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        Handler handler = new android.os.Handler();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        imageList = getContext().getDir("HomeCleanProject", Context.MODE_PRIVATE).listFiles();

        //Ui 스레드에 직접 접근 불가 -> just메서드가 UI Blocking에 걸림
        //그래서 create메서드를 통해서 직접 subscribe를 구현
        Observable<String> filePath = Observable.create(subscriber -> { //생산자
            try {
                if (!subscriber.isDisposed()) {
                    for (File file : imageList) {
                        subscriber.onNext(file.getAbsolutePath()); //데이터 생산
                    }
                }
                subscriber.onComplete(); //데이터 생산이 완료되었음을 통지
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });

        filePath.observeOn(Schedulers.io()) // 구독자, 쓰레드 풀에 있는 스레드를 하나 이용하겠다.
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> { //생산한 data를 통해 bitmap을 decode한다.
                            bitmap = BitmapFactory.decodeFile((String) data, options);

                            handler.post(() -> {
                                adapter.addItem(bitmap);
                                adapter.notifyDataSetChanged();
                            });
                        }
                );

        return view;
    }
}