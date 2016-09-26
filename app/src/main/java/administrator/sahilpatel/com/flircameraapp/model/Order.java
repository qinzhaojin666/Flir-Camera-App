package administrator.sahilpatel.com.flircameraapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.util.ArrayList;
import java.util.List;




@JsonIgnoreProperties(ignoreUnknown = true)
public class Order{

    public static final String STATUS_NEW = "NEW";
    public static final String STATUS_IN_PROGRESS = "IN-PROGRESS";
    public static final String STAUS_CLOSED ="CLOSED";

    private String orderId;
    private String workOrderNumber;
    private String workOrderTitle;
    private String workOrderDescription;
    private String customerName;
    private String customerAddress;
    private String assignedTo;

    private String status;

    private List<Update> updates;
    private Closure closure;

    private List<ImagePair> images;

    public Order() {
        images = new ArrayList<>();
        updates = new ArrayList<>();
        status = STATUS_NEW;
    }

    public Order(String workOrderTitle, String workOrderNumber, String workOrderDescription,
                 String customerName, String customerAddress) {
        this.workOrderTitle = workOrderTitle;
        this.workOrderNumber = workOrderNumber;
        this.workOrderDescription = workOrderDescription;
        this.customerName = customerName;
        this.customerAddress = customerAddress;

        images = new ArrayList<>();
        updates = new ArrayList<>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Closure getClosure() {
        return closure;
    }

    public void setClosure(Closure closure) {
        this.closure = closure;
    }

    public List<Update> getUpdates() {
        return updates;
    }

    public void setUpdates(List<Update> updates) {
        this.updates = updates;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
