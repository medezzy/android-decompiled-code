package in.mobiant.medical.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MedicinesItem implements Parcelable {
    public static final Creator<MedicinesItem> CREATOR = new C03491();
    @SerializedName("ATC Classification")
    @Expose
    private ATCClassification aTCClassification;
    @SerializedName("CIMSClass")
    @Expose
    private String cIMSClass;
    @SerializedName("Contents")
    @Expose
    private String contents;
    private int d_buy;
    private int d_sell;
    private long dom;
    private String drugType;
    private float eachPrice;
    private float eachPriceWithTax;
    private long expiry;
    @SerializedName("_id")
    @Expose
    private String id;
    private int m_buy;
    private int m_sell;
    @SerializedName("Manufacturer")
    @Expose
    private String manufacturer;
    @SerializedName("meicineType")
    @Expose
    private String meicineType;
    private float packedPrice;
    private String packedQty;
    @SerializedName("presentation")
    @Expose
    private List<Presentation> presentation;
    private String purchasedBill;
    private String purchasedFrom;
    @SerializedName("referencedGenericMedicine")
    @Expose
    private List<String> referencedGenericMedicine;
    private int selectedQty;
    private int stockQty;
    private float tax;
    private int w_buy;
    private int w_sell;

    static class C03491 implements Creator<MedicinesItem> {
        C03491() {
        }

        public MedicinesItem createFromParcel(Parcel in) {
            return new MedicinesItem(in);
        }

        public MedicinesItem[] newArray(int size) {
            return new MedicinesItem[size];
        }
    }

    public MedicinesItem() {
        this.id = "";
        this.referencedGenericMedicine = null;
        this.presentation = null;
        this.eachPrice = 0.0f;
        this.eachPriceWithTax = 0.0f;
        this.tax = 0.0f;
        this.packedQty = "0";
        this.stockQty = 0;
        this.drugType = "";
        this.expiry = 0;
        this.dom = 0;
        this.packedPrice = 0.0f;
        this.selectedQty = 0;
        this.purchasedFrom = "";
        this.purchasedBill = "";
        this.d_sell = 0;
        this.w_sell = 0;
        this.m_sell = 0;
        this.d_buy = 0;
        this.w_buy = 0;
        this.m_buy = 0;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeicineType() {
        return this.meicineType;
    }

    public void setMeicineType(String meicineType) {
        this.meicineType = meicineType;
    }

    public List<String> getReferencedGenericMedicine() {
        return this.referencedGenericMedicine;
    }

    public void setReferencedGenericMedicine(List<String> referencedGenericMedicine) {
        this.referencedGenericMedicine = referencedGenericMedicine;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public ATCClassification getATCClassification() {
        return this.aTCClassification;
    }

    public void setATCClassification(ATCClassification aTCClassification) {
        this.aTCClassification = aTCClassification;
    }

    public String getContents() {
        return this.contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getCIMSClass() {
        return this.cIMSClass;
    }

    public void setCIMSClass(String cIMSClass) {
        this.cIMSClass = cIMSClass;
    }

    public List<Presentation> getPresentation() {
        return this.presentation;
    }

    public void setPresentation(List<Presentation> presentation) {
        this.presentation = presentation;
    }

    public float getEachPrice() {
        return this.eachPrice;
    }

    public void setEachPrice(float eachPrice) {
        this.eachPrice = eachPrice;
    }

    public float getEachPriceWithTax() {
        return this.eachPriceWithTax;
    }

    public void setEachPriceWithTax(float eachPriceWithTax) {
        this.eachPriceWithTax = eachPriceWithTax;
    }

    public float getTax() {
        return this.tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public int describeContents() {
        return 0;
    }

    private MedicinesItem(Parcel in) {
        this.id = "";
        this.referencedGenericMedicine = null;
        this.presentation = null;
        this.eachPrice = 0.0f;
        this.eachPriceWithTax = 0.0f;
        this.tax = 0.0f;
        this.packedQty = "0";
        this.stockQty = 0;
        this.drugType = "";
        this.expiry = 0;
        this.dom = 0;
        this.packedPrice = 0.0f;
        this.selectedQty = 0;
        this.purchasedFrom = "";
        this.purchasedBill = "";
        this.d_sell = 0;
        this.w_sell = 0;
        this.m_sell = 0;
        this.d_buy = 0;
        this.w_buy = 0;
        this.m_buy = 0;
        this.id = in.readString();
        this.meicineType = in.readString();
        this.referencedGenericMedicine = in.createStringArrayList();
        this.manufacturer = in.readString();
        this.aTCClassification = (ATCClassification) in.readParcelable(ATCClassification.class.getClassLoader());
        this.contents = in.readString();
        this.cIMSClass = in.readString();
        this.presentation = in.createTypedArrayList(Presentation.CREATOR);
        this.eachPrice = in.readFloat();
        this.eachPriceWithTax = in.readFloat();
        this.tax = in.readFloat();
        this.packedQty = in.readString();
        this.stockQty = in.readInt();
        this.drugType = in.readString();
        this.expiry = in.readLong();
        this.packedPrice = in.readFloat();
        this.selectedQty = in.readInt();
        this.purchasedFrom = in.readString();
        this.purchasedBill = in.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.meicineType);
        parcel.writeStringList(this.referencedGenericMedicine);
        parcel.writeString(this.manufacturer);
        parcel.writeParcelable(this.aTCClassification, i);
        parcel.writeString(this.contents);
        parcel.writeString(this.cIMSClass);
        parcel.writeTypedList(this.presentation);
        parcel.writeFloat(this.eachPrice);
        parcel.writeFloat(this.eachPriceWithTax);
        parcel.writeFloat(this.tax);
        parcel.writeString(this.packedQty);
        parcel.writeInt(this.stockQty);
        parcel.writeString(this.drugType);
        parcel.writeLong(this.expiry);
        parcel.writeFloat(this.packedPrice);
        parcel.writeInt(this.selectedQty);
        parcel.writeString(this.purchasedFrom);
        parcel.writeString(this.purchasedBill);
    }

    public int getStockQty() {
        return this.stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    public String getDrugType() {
        return this.drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    public long getExpiry() {
        return this.expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public float getPackedPrice() {
        return this.packedPrice;
    }

    public void setPackedPrice(float packedPrice) {
        this.packedPrice = packedPrice;
    }

    public String getPackedQty() {
        return this.packedQty;
    }

    public void setPackedQty(String packedQty) {
        this.packedQty = packedQty;
    }

    public int getSelectedQty() {
        return this.selectedQty;
    }

    public void setSelectedQty(int selectedQty) {
        this.selectedQty = selectedQty;
    }

    public long getDom() {
        return this.dom;
    }

    public void setDom(long dom) {
        this.dom = dom;
    }

    public String getPurchasedFrom() {
        return this.purchasedFrom;
    }

    public void setPurchasedFrom(String purchasedFrom) {
        this.purchasedFrom = purchasedFrom;
    }

    public String getPurchasedBill() {
        return this.purchasedBill;
    }

    public void setPurchasedBill(String purchasedBill) {
        this.purchasedBill = purchasedBill;
    }

    public int getD_sell() {
        return this.d_sell;
    }

    public void setD_sell(int d_sell) {
        this.d_sell = d_sell;
    }

    public int getW_sell() {
        return this.w_sell;
    }

    public void setW_sell(int w_sell) {
        this.w_sell = w_sell;
    }

    public int getM_sell() {
        return this.m_sell;
    }

    public void setM_sell(int m_sell) {
        this.m_sell = m_sell;
    }

    public int getM_buy() {
        return this.m_buy;
    }

    public void setM_buy(int m_buy) {
        this.m_buy = m_buy;
    }

    public int getW_buy() {
        return this.w_buy;
    }

    public void setW_buy(int w_buy) {
        this.w_buy = w_buy;
    }

    public int getD_buy() {
        return this.d_buy;
    }

    public void setD_buy(int d_buy) {
        this.d_buy = d_buy;
    }
}
