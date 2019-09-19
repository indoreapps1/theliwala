package loop.theliwala.framworkretrofit;

import loop.theliwala.models.ContentData;
import loop.theliwala.models.ContentDataAsArray;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("getPackage.php")
    Call<ContentData> getService();

    @POST("getPagerData.php")
    Call<ContentDataAsArray> getPagerData();

    @FormUrlEncoded
    @POST("GetAllProduct.php")
    Call<ContentDataAsArray> GetAllProduct(@Field("categoryId") int categoryId);


//    @POST("GetAlbumImages.php")
//    Call<GalleryImageResponseParser> getGalleryPagenationData(@Field("pn") int pageIndex);
}

