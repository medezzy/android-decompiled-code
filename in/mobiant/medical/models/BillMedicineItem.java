package in.mobiant.medical.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillMedicineItem {
    @SerializedName("expiry")
    @Expose
    private long expiry;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("total")
    @Expose
    private float total;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getExpiry() {
        return this.expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public float getTotal() {
        return this.total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
