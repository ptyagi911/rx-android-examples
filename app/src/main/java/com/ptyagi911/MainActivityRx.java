package com.ptyagi911;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

/**
 * Example to show-off how the same thing (updating the textView)
 * is achieved using RxAndroid
 */
public class MainActivityRx extends AppCompatActivity implements Observer<String>{

    static TextView textView = null;
    static String helloStr = "Hello There !";

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView)findViewById(R.id.textView);

        subscription = AndroidObservable.fromActivity(this, getStringObservable())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this);

//.subscribeOn(Schedulers.newThread())
//.observeOn(AndroidSchedulers.mainThread())

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private Observable<String> getStringObservable() {
        return Observable.create(new Observable.OnSubscribeFunc<String>() {
            @Override
            public Subscription onSubscribe(Observer<? super String> strObserver) {
                try {
                    String str = getString();
                    strObserver.onNext(str);
                    strObserver.onCompleted();
                } catch (Exception e) {
                    strObserver.onError(e);
                }
                return Subscriptions.empty();
            }
        });
    }

    private String getString() {
        return helloStr;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    @Override
    public void onCompleted() {
        Toast.makeText(this, "Finished receiving String", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(this, "Failed to receive String: " + e.getMessage(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNext(String args) {
        textView.setText(args);
        Toast.makeText(this, "Received String: " + args, Toast.LENGTH_LONG).show();
    }
}
