package in.mobiant.medical.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SubUserInfoResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("resultCode")
    @Expose
    private Integer resultCode;
    @SerializedName("subUsers_arr")
    @Expose
    private List<SubUsersArr> subUsersArr = null;

    public Integer getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<SubUsersArr> getSubUsersArr() {
        return this.subUsersArr;
    }

    public void setSubUsersArr(List<SubUsersArr> subUsersArr) {
        this.subUsersArr = subUsersArr;
    }
}
