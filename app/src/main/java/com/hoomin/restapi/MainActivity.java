package com.hoomin.restapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String API_URL="https://api.github.com/";

    @BindView(R.id.tv_name)
    protected TextView tv_name;
    @BindView(R.id.tv_realm_test)
    protected TextView tv_realm_test;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        ButterKnife.bind(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GithubAPI githubAPI = retrofit.create(GithubAPI.class);
        Call<List<ResponseBody>> repos = githubAPI.listRepos("octocat");
        repos.enqueue(new Callback<List<ResponseBody>>() {
            @Override
            public void onResponse(Call<List<ResponseBody>> call, Response<List<ResponseBody>> response) {
                List<ResponseBody> list =response.body();

                tv_name.setText(list.get(0).getName());

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(list);
                realm.commitTransaction();

                tv_realm_test.setText(realm.where(ResponseBody.class).findFirst().getName());
            }

            @Override
            public void onFailure(Call<List<ResponseBody>> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
