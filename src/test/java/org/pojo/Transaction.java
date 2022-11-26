package org.pojo;

import com.github.javafaker.Faker;

/**
 * Transaction class - POJO
 *
 * @author je.sarmiento
 */
public class Transaction {

    private String name;
    private String lastName;
    private long accountNumber;
    private double amount;
    private String transactionType;
    private String email;
    private boolean active;
    private String country;
    private String phone;
    private int id;

    /**
     * Email getter
     *
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * First name getter
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Last name getter
     *
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Account number getter
     *
     * @return long
     */
    public long getAccountNumber() {
        return accountNumber;
    }

    /**
     * Amount getter
     *
     * @return double
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Transaction type getter
     *
     * @return String
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Is active method
     *
     * @return boolean
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Country getter
     *
     * @return String
     */
    public String getCountry() {
        return country;
    }

    /**
     * Phone getter
     *
     * @return String
     */
    public String getPhone() {
        return phone;
    }

    /**
     * ID getter
     *
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * First name setter
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Last name setter
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Account number setter
     *
     * @param accountNumber
     */
    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Amount setter
     *
     * @param amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Transaction type setter
     *
     * @param type
     */
    public void setTransactionType(String type){
        this.transactionType = type;
    }

    /**
     * Email setter
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Active status setter
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Country setter
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Phone setter
     *
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * toString method configured to return the object in JSON format as a String
     *
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + "\"" +
                ", \"lastName\":\"" + lastName + "\"" +
                ", \"accountNumber\":\"" + accountNumber + "\"" +
                ", \"amount\":\"" + amount + "\"" +
                ", \"transactionType\":\"" + transactionType + "\"" +
                ", \"email\":\"" + email + "\"" +
                ", \"active\":\"" + active + "\"" +
                ", \"country\":\"" + country + "\"" +
                ", \"phone\":\"" + phone + "\"" +
                "}";
    }
}
