package in.mobiant.medical.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MedicineItemNew {
    @SerializedName("ATC Classification")
    @Expose
    private List<Object> aTCClassification = null;
    @SerializedName("CIMSClass")
    @Expose
    private String cIMSClass;
    @SerializedName("contents")
    @Expose
    private String contents;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;
    @SerializedName("medicineType")
    @Expose
    private String medicineType;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("presentation")
    @Expose
    private List<Presentation> presentation = null;
    @SerializedName("referencedGenericMedicine")
    @Expose
    private List<Object> referencedGenericMedicine = null;

    public class Presentation {
        @SerializedName("batch")
        @Expose
        private String batch;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("type")
        @Expose
        private String type;

        public String getPrice() {
            return this.price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getBatch() {
            return this.batch;
        }

        public void setBatch(String batch) {
            this.batch = batch;
        }

        public String getQuantity() {
            return this.quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContents() {
        return this.contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public List<Object> getATCClassification() {
        return this.aTCClassification;
    }

    public void setATCClassification(List<Object> aTCClassification) {
        this.aTCClassification = aTCClassification;
    }

    public List<Object> getReferencedGenericMedicine() {
        return this.referencedGenericMedicine;
    }

    public void setReferencedGenericMedicine(List<Object> referencedGenericMedicine) {
        this.referencedGenericMedicine = referencedGenericMedicine;
    }

    public String getMedicineType() {
        return this.medicineType;
    }

    public void setMedicineType(String medicineType) {
        this.medicineType = medicineType;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
