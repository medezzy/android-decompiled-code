package in.mobiant.medical.models;

public class AlertInfoItem {
    private long count = 0;
    private int days = 0;

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getDays() {
        return this.days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
