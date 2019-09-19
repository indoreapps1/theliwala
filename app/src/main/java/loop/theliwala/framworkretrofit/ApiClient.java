package loop.theliwala.framworkretrofit;

import loop.theliwala.utilities.Contants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static Retrofit retrofit = null;

    public static Retrofit getAPIClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Contants.SERVICE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

