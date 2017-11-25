package in.mobiant.medical.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OTPSendInterface {
    @GET("pushsms.php?username=sky-cable&password=ENXeVn&sender=GJWLER")
    Call<String> sendOTPMessage(@Query("message") String str, @Query("numbers") String str2);
}
