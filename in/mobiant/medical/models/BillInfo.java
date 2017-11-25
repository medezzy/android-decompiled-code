package in.mobiant.medical.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BillInfo {
    @SerializedName("amount")
    @Expose
    private float amount = 0.0f;
    @SerializedName("billMedicinesList")
    @Expose
    private List<BillMedicineItem> billMedicinesList;
    @SerializedName("billNo")
    @Expose
    private String billNo = "";
    @SerializedName("date")
    @Expose
    private long date = 0;
    @SerializedName("patientInfo")
    @Expose
    private PatientInfo patientInfo;

    public String getBillNo() {
        return this.billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public PatientInfo getPatientInfo() {
        return this.patientInfo;
    }

    public void setPatientInfo(PatientInfo patientInfo) {
        this.patientInfo = patientInfo;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public List<BillMedicineItem> getBillMedicinesList() {
        return this.billMedicinesList;
    }

    public void setBillMedicinesList(List<BillMedicineItem> billMedicinesList) {
        this.billMedicinesList = billMedicinesList;
    }
}
