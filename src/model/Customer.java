package model;

public class Customer {
    //Fields
    private String customerID;
    private String customerName;
    private String customerAddress;
    private String customerContact;

    //Constructor
    public Customer(String customerID,
                        String customerName,
                        String customerAddress,
                        String customerContact) {
        this.customerID = customerID;
        setCustomerName(customerName);
        setCustomerAddress(customerAddress);
        setCustomerContact(customerContact);
    }

    //Getters
    public String getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    //Setters
    public void setCustomerName(String newCustomerName) {
        if (newCustomerName == null || newCustomerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer Name cannot be empty");
        }
        this.customerName = newCustomerName;
    }

    public void setCustomerAddress(String newCustomerAddress) {
        if (newCustomerAddress == null || newCustomerAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer Address cannot be empty");
        }
        this.customerAddress = newCustomerAddress;
    }

    public void setCustomerContact(String newCustomerContact) {
        if (newCustomerContact == null || newCustomerContact.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer Contact cannot be empty");
        }
        this.customerContact = newCustomerContact;
    }
}