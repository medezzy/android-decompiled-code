package in.mobiant.medical.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StockMedicineInfo {
    @SerializedName("details")
    @Expose
    private List<Details> details;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("minExpiry")
    @Expose
    private Long minExpiry;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("updatedStock")
    @Expose
    private Integer updatedStock;

    public class Details {
        @SerializedName("batch")
        @Expose
        private String batch;
        @SerializedName("bill")
        @Expose
        private String bill;
        @SerializedName("dom")
        @Expose
        private Long dom;
        @SerializedName("expiry")
        @Expose
        private Long expiry;
        @SerializedName("purchasedFrom")
        @Expose
        private String purchasedFrom;
        @SerializedName("quantity")
        @Expose
        private Integer quantity;

        public Long getExpiry() {
            return this.expiry;
        }

        public void setExpiry(Long expiry) {
            this.expiry = expiry;
        }

        public Long getDom() {
            return this.dom;
        }

        public void setDom(Long dom) {
            this.dom = dom;
        }

        public Integer getQuantity() {
            return this.quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getPurchasedFrom() {
            return this.purchasedFrom;
        }

        public void setPurchasedFrom(String purchasedFrom) {
            this.purchasedFrom = purchasedFrom;
        }

        public String getBatch() {
            return this.batch;
        }

        public void setBatch(String batch) {
            this.batch = batch;
        }

        public String getBill() {
            return this.bill;
        }

        public void setBill(String bill) {
            this.bill = bill;
        }
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

    public Integer getUpdatedStock() {
        return this.updatedStock;
    }

    public void setUpdatedStock(Integer updatedStock) {
        this.updatedStock = updatedStock;
    }

    public Long getMinExpiry() {
        return this.minExpiry;
    }

    public void setMinExpiry(Long minExpiry) {
        this.minExpiry = minExpiry;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
