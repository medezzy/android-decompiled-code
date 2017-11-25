package in.mobiant.medical.net;

import in.mobiant.medical.models.BillResponse;
import in.mobiant.medical.models.MedicineItemNew;
import in.mobiant.medical.models.StockAddResponse;
import in.mobiant.medical.models.SubUserInfoResponse;
import in.mobiant.medical.models.UserInfoItem;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiCallInterface {
    @GET("data.php?type=add_bill_record")
    Call<BillResponse> addBillRecord(@Query("username") String str, @Query("mobile") String str2, @Query("bill_info") String str3, @Query("role") String str4, @Query("deviceId") String str5);

    @GET("data.php?type=add_stock")
    Call<StockAddResponse> addStock(@Query("username") String str, @Query("mobile") String str2, @Query("stock_info") String str3, @Query("role") String str4, @Query("deviceId") String str5);

    @GET("data.php?type=add_sub_user")
    Call<SubUserInfoResponse> addSubUser(@Query("username") String str, @Query("mobile") String str2, @Query("s_un") String str3, @Query("s_pass") String str4, @Query("sub_users") String str5);

    @GET("data.php?type=get_medicines")
    Call<List<MedicineItemNew>> getMedicines(@Query("username") String str, @Query("mobile") String str2, @Query("role") String str3, @Query("deviceId") String str4, @Query("skip") Integer num, @Query("limit") Integer num2);

    @FormUrlEncoded
    @POST("data.php?type=sign_up")
    Call<UserInfoItem> signUp(@Field("sign_up_info") String str);

    @FormUrlEncoded
    @POST("data.php?type=check_user")
    Call<UserInfoItem> userCheck(@Field("username") String str, @Field("pass") String str2, @Field("deviceId") String str3, @Field("role") String str4);
}
