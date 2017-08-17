package com.android.rx2fastandroidnetworkingdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.rx2fastandroidnetworkingdemo.utils.Utils;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class SubscriptionActivity extends AppCompatActivity {

    private static final String TAG = SubscriptionActivity.class.getSimpleName();
    private static final String URL = "http://i.imgur.com/AtbX9iX.png";
    private String dirPath;
    private String fileName = "imgurimage.png";
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        dirPath = Utils.getRootDirPath(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    public Observable<String> getObservable() {
        return Rx2AndroidNetworking.download(URL, dirPath, fileName)
                .build()
                .getDownloadObservable();
    }

    private DisposableObserver<String> getDisposableObserver() {

        return new DisposableObserver<String>() {

            @Override
            public void onNext(String response) {
                Log.d(TAG, "onResponse response : " + response);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onCompleted");
            }
        };
    }

    public void downloadFile(View view) {
        disposables.add(getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getDisposableObserver()));
    }
}