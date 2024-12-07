package DataEntryOperator.model;

public class Vendor {
    private int vendorId;
    private String vendorName;
    private String vendorAddress;
    private String contactNo;

    public Vendor(int vendorId, String vendorName, String vendorAddress, String contactNo) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.vendorAddress = vendorAddress;
        this.contactNo = contactNo;
    }

    public int getVendorId() {
        return vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public String getContactNo() {
        return contactNo;
    }
}
