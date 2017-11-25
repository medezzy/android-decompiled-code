package in.mobiant.medical.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientInfo implements Parcelable {
    public static final Creator<PatientInfo> CREATOR = new C03501();
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("docAddress")
    @Expose
    private String docAddress;
    @SerializedName("doctor")
    @Expose
    private String doctor;
    @SerializedName("doctorRegNo")
    @Expose
    private String doctorRegNo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("mobile")
    @Expose
    private String mobile;

    static class C03501 implements Creator<PatientInfo> {
        C03501() {
        }

        public PatientInfo createFromParcel(Parcel in) {
            return new PatientInfo(in);
        }

        public PatientInfo[] newArray(int size) {
            return new PatientInfo[size];
        }
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDoctor() {
        return this.doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDocAddress() {
        return this.docAddress;
    }

    public void setDocAddress(String docAddress) {
        this.docAddress = docAddress;
    }

    private PatientInfo(Parcel in) {
        this.fullName = in.readString();
        this.age = in.readString();
        this.gender = in.readString();
        this.mobile = in.readString();
        this.email = in.readString();
        this.address = in.readString();
        this.doctor = in.readString();
        this.doctorRegNo = in.readString();
        this.docAddress = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.fullName);
        parcel.writeString(this.age);
        parcel.writeString(this.gender);
        parcel.writeString(this.mobile);
        parcel.writeString(this.email);
        parcel.writeString(this.address);
        parcel.writeString(this.doctor);
        parcel.writeString(this.doctorRegNo);
        parcel.writeString(this.docAddress);
    }

    public String getDoctorRegNo() {
        return this.doctorRegNo;
    }

    public void setDoctorRegNo(String doctorRegNo) {
        this.doctorRegNo = doctorRegNo;
    }
}
