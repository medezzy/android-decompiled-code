package in.mobiant.medical.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ATCClassification implements Parcelable {
    public static final Creator<ATCClassification> CREATOR = new C03481();
    @SerializedName("C01CA06 ")
    @Expose
    private String c01CA06;
    @SerializedName("R01AA04 ")
    @Expose
    private String r01AA04;
    @SerializedName("R01AB01 ")
    @Expose
    private String r01AB01;
    @SerializedName("R01BA03 ")
    @Expose
    private String r01BA03;
    @SerializedName("S01FB01 ")
    @Expose
    private String s01FB01;
    @SerializedName("S01GA05 ")
    @Expose
    private String s01GA05;

    static class C03481 implements Creator<ATCClassification> {
        C03481() {
        }

        public ATCClassification createFromParcel(Parcel in) {
            return new ATCClassification(in);
        }

        public ATCClassification[] newArray(int size) {
            return new ATCClassification[size];
        }
    }

    protected ATCClassification(Parcel in) {
        this.c01CA06 = in.readString();
        this.r01AA04 = in.readString();
        this.r01AB01 = in.readString();
        this.r01BA03 = in.readString();
        this.s01FB01 = in.readString();
        this.s01GA05 = in.readString();
    }

    public String getC01CA06() {
        return this.c01CA06;
    }

    public void setC01CA06(String c01CA06) {
        this.c01CA06 = c01CA06;
    }

    public String getR01AA04() {
        return this.r01AA04;
    }

    public void setR01AA04(String r01AA04) {
        this.r01AA04 = r01AA04;
    }

    public String getR01AB01() {
        return this.r01AB01;
    }

    public void setR01AB01(String r01AB01) {
        this.r01AB01 = r01AB01;
    }

    public String getR01BA03() {
        return this.r01BA03;
    }

    public void setR01BA03(String r01BA03) {
        this.r01BA03 = r01BA03;
    }

    public String getS01FB01() {
        return this.s01FB01;
    }

    public void setS01FB01(String s01FB01) {
        this.s01FB01 = s01FB01;
    }

    public String getS01GA05() {
        return this.s01GA05;
    }

    public void setS01GA05(String s01GA05) {
        this.s01GA05 = s01GA05;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.c01CA06);
        parcel.writeString(this.r01AA04);
        parcel.writeString(this.r01AB01);
        parcel.writeString(this.r01BA03);
        parcel.writeString(this.s01FB01);
        parcel.writeString(this.s01GA05);
    }
}
