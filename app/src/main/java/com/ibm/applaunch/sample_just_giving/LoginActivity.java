package com.ibm.applaunch.sample_just_giving;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibm.mobile.applaunch.android.AppLaunchFailResponse;
import com.ibm.mobile.applaunch.android.AppLaunchResponse;
import com.ibm.mobile.applaunch.android.api.AppLaunch;
import com.ibm.mobile.applaunch.android.api.AppLaunchConfig;
import com.ibm.mobile.applaunch.android.api.AppLaunchListener;
import com.ibm.mobile.applaunch.android.api.AppLaunchUser;
import com.ibm.mobile.applaunch.android.api.ICRegion;
import com.ibm.mobile.applaunch.android.api.RefreshPolicy;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class LoginActivity extends AppCompatActivity implements AppLaunchListener {

    private static final String APP_GUID = "c22af889-26d6-49f0-bd08-0cff596a2606";
    private static final String CLIENT_SECRET = "4fa4e697-f997-4f94-80f4-09723e5f4241";
    private static final String TAG = LoginActivity.class.getSimpleName();

    private TextView tvLogin, tvLoginAsGuest;
    private View contentView;
    private ProgressBar progressBar;
    private PublishSubject<Boolean> showLoader = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {
        final Intent i = new Intent(this, DashboardActivity.class);

        tvLogin = findViewById(R.id.tv_login);
        tvLoginAsGuest = findViewById(R.id.tv_login_as_guest);
        progressBar = findViewById(R.id.progressBar);


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSDK("subscribed");
            }
        });

        tvLoginAsGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSDK("guest");
            }
        });

        showLoader.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        progressBar.setVisibility(View.GONE);

                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initSDK(String userType) {
        progressBar.setVisibility(View.VISIBLE);

        AppLaunchConfig appLaunchConfig = new AppLaunchConfig.Builder().
                eventFlushInterval(10).
                cacheExpiration(10).
                fetchPolicy(RefreshPolicy.REFRESH_ON_EVERY_START).
                deviceId("dfsgggggggdfqqsdfgsdfggsdfgsdfg"+userType).
                build();
        AppLaunchUser appLaunchUser = new AppLaunchUser.Builder().userId("dfsgggqqeeggggdfgsdfgsdfg"+userType).custom("userType", userType).build();
        AppLaunch.getInstance().init(getApplication(), ICRegion.US_SOUTH_STAGING, APP_GUID, CLIENT_SECRET, appLaunchConfig, appLaunchUser, this);
    }

    @Override
    public void onSuccess(AppLaunchResponse appLaunchResponse) {
        Log.i(TAG, appLaunchResponse.toString());
        showLoader.onNext(true);
    }

    @Override
    public void onFailure(AppLaunchFailResponse appLaunchFailResponse) {
        Log.i(TAG, appLaunchFailResponse.toString());
        showLoader.onNext(true);
    }
}
