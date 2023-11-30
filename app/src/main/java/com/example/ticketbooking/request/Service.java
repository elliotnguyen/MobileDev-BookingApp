package com.example.ticketbooking.request;

public class Service {
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(TheMovieDb.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit = retrofitBuilder.build();
    private static MovieApi movieApi = retrofit.create(MovieApi.class);

    public static MovieApi getMovieApi() {
        return movieApi;
    }
}
