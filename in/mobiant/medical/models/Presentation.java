package in.mobiant.medical.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Presentation implements Parcelable {
    public static final Creator<Presentation> CREATOR = new C03511();
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("type")
    @Expose
    private String type;

    static class C03511 implements Creator<Presentation> {
        C03511() {
        }

        public Presentation createFromParcel(Parcel in) {
            return new Presentation(in);
        }

        public Presentation[] newArray(int size) {
            return new Presentation[size];
        }
    }

    protected Presentation(Parcel in) {
        this.type = in.readString();
        this.quantity = in.readString();
        this.price = in.readString();
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.type);
        parcel.writeString(this.quantity);
        parcel.writeString(this.price);
    }
}
