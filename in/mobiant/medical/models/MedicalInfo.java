package in.mobiant.medical.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedicalInfo {
    @SerializedName("address")
    @Expose
    private String address = "Shop No1 1, Vikrant Complex, Pimpri, Pimpri, Pune, Maharashtra 411017";
    @SerializedName("gstin")
    @Expose
    private String gstin = "1234567890";
    @SerializedName("name")
    @Expose
    private String name = "Vighnaharta Medical & General Stores";
    @SerializedName("phone")
    @Expose
    private String phone = "020 2741 0269";

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGstin() {
        return this.gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
