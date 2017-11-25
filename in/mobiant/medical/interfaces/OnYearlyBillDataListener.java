package in.mobiant.medical.interfaces;

import in.mobiant.medical.models.BillInfo;
import java.util.List;

public interface OnYearlyBillDataListener {
    void onDataFetched(List<BillInfo> list);

    void onNameChanged(List<BillInfo> list);
}
