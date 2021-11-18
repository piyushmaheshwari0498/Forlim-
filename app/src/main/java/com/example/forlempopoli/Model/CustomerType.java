package com.example.forlempopoli.Model;

public class CustomerType {
    private String customertype_id;
    private String customerType;

    public CustomerType(String customertype_id, String customerType) {
        this.customertype_id = customertype_id;
        this.customerType = customerType;

    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomertype_id() {
        return customertype_id;
    }

    public void setCustomertype_id(String customertype_id) {
        this.customertype_id = customertype_id;
    }

    @Override
    public String toString() {
        return "CustomerType{" +
                "customerType='" + customerType + '\'' +
                '}';
    }
}
