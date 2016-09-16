package administrator.sahilpatel.com.flircameraapp.model;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 9/16/2016.
 */

//@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    private String workOrderNumber;
    private String workOrderTitle;
    private String workOrderDescription;
    private String customerName;
    private String customerAddress;
    private String assignedTo;

    private List<ImagePair> images;

    public Order() {
    }

    public Order(String workOrderTitle, String workOrderNumber, String workOrderDescription,
                 String customerName, String customerAddress) {
        this.workOrderTitle = workOrderTitle;
        this.workOrderNumber = workOrderNumber;
        this.workOrderDescription = workOrderDescription;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getWorkOrderTitle() {
        return workOrderTitle;
    }

    public void setWorkOrderTitle(String workOrderTitle) {
        this.workOrderTitle = workOrderTitle;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public String getWorkOrderDescription() {
        return workOrderDescription;
    }

    public void setWorkOrderDescription(String workOrderDescription) {
        this.workOrderDescription = workOrderDescription;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public List<ImagePair> getImages() {
        return images;
    }

    public void setImages(List<ImagePair> images) {
        this.images = images;
    }
}
