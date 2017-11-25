package in.mobiant.medical.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BillResponse {
    @SerializedName("billNo")
    @Expose
    private String billNo;
    @SerializedName("mediStock")
    @Expose
    private List<MediStock> mediStocks;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("resultCode")
    @Expose
    private Integer resultCode;

    public class MediStock {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("updatedStock")
        @Expose
        private Integer updatedStock;

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getUpdatedStock() {
            return this.updatedStock;
        }

        public void setUpdatedStock(Integer updatedStock) {
            this.updatedStock = updatedStock;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

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

    public String getBillNo() {
        return this.billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public List<MediStock> getMediStocks() {
        return this.mediStocks;
    }

    public void setMediStocks(List<MediStock> mediStocks) {
        this.mediStocks = mediStocks;
    }
}
