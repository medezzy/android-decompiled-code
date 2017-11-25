package in.mobiant.medical.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StringResponse {
    @SerializedName("subUsers_arr")
    @Expose
    private String subUsers_arr;

    public String getSubUsers_arr() {
        return this.subUsers_arr;
    }

    public void setSubUsers_arr(String subUsers_arr) {
        this.subUsers_arr = subUsers_arr;
    }
}
