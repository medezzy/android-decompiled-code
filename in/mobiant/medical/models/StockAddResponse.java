package in.mobiant.medical.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.mobiant.medical.models.StockMedicineInfo.Details;
import java.util.List;

public class StockAddResponse {
    @SerializedName("details")
    @Expose
    private List<Details> details;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("minExpiry")
    @Expose
    private Long minExpiry;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("resultCode")
    @Expose
    private Integer resultCode;
    @SerializedName("updatedStock")
    @Expose
    private Integer updatedStock;

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

    public Integer getUpdatedStock() {
        return this.updatedStock;
    }

    public void setUpdatedStock(Integer updatedStock) {
        this.updatedStock = updatedStock;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Details> getDetails() {
        return this.details;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }

    public Long getMinExpiry() {
        return this.minExpiry;
    }

    public void setMinExpiry(Long minExpiry) {
        this.minExpiry = minExpiry;
    }
}
