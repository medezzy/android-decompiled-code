package in.mobiant.medical.utility;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

public class HttpClient {
    public static OkHttpClient getClient() {
        return new Builder().connectTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
    }
}
