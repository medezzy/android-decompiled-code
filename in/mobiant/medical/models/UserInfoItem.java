package in.mobiant.medical.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class UserInfoItem implements Serializable {
    @SerializedName("accType")
    @Expose
    private String accType;
    @SerializedName("Address")
    @Expose
    private Address address;
    @SerializedName("devices_arr")
    @Expose
    private List<String> devicesArr = null;
    @SerializedName("drugLicence")
    @Expose
    private String drugLicence;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("expiry")
    @Expose
    private Long expiry;
    @SerializedName("Gstin")
    @Expose
    private String gstin;
    @SerializedName("lastLogin")
    @Expose
    private Long lastLogin;
    @SerializedName("medical_name")
    @Expose
    private String medical_name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("Owner")
    @Expose
    private Owner owner;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("registrationDate")
    @Expose
    private String registrationDate;
    @SerializedName("resultCode")
    @Expose
    private Integer resultCode;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("subUsers_arr")
    @Expose
    private List<SubUsersArr> subUsersArr = null;
    @SerializedName("username")
    @Expose
    private String username;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getDevicesArr() {
        return this.devicesArr;
    }

    public void setDevicesArr(List<String> devicesArr) {
        this.devicesArr = devicesArr;
    }

    public List<SubUsersArr> getSubUsersArr() {
        return this.subUsersArr;
    }

    public void setSubUsersArr(List<SubUsersArr> subUsersArr) {
        this.subUsersArr = subUsersArr;
    }

    public String getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Long getExpiry() {
        return this.expiry;
    }

    public void setExpiry(Long expiry) {
        this.expiry = expiry;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getGstin() {
        return this.gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getDrugLicence() {
        return this.drugLicence;
    }

    public void setDrugLicence(String drugLicence) {
        this.drugLicence = drugLicence;
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

    public String getAccType() {
        return this.accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public Long getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(Long lastLogin) {
        this.lastLogin = lastLogin;
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

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMedical_name() {
        return this.medical_name;
    }

    public void setMedical_name(String medical_name) {
        this.medical_name = medical_name;
    }
}
