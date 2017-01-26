package com.hoomin.restapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Hooo on 2017-01-26.
 */

public interface GithubAPI {
    @GET("users/{user}/repos")
    Call<List<ResponseBody>> listRepos(@Path("user") String user);}
