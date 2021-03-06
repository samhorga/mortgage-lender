package org.mortgage;

import java.time.LocalDate;
import java.time.Period;

public class LoanApplication {

    private double requestedAmount;
    private int dti;
    private int creditScore;
    private double savings;
    private String qualification;
    private double loan_amount;
    private String status;
    private LocalDate approvedDate;

    public LoanApplication(double requestedAmount, int dti, int creditScore, double savings,
                           String qualification, double loan_amount, String status, LocalDate approvedDate) {
        this.requestedAmount = requestedAmount;
        this.dti = dti;
        this.creditScore = creditScore;
        this.savings = savings;
        this.qualification = qualification;
        this.loan_amount = loan_amount;
        this.status = status;
        this.approvedDate = approvedDate;
    }

    public LocalDate getApprovedDate() {
        return approvedDate;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public int getDti() {
        return dti;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public double getSavings() {
        return savings;
    }

    public String getQualification() {
        return qualification;
    }

    public double getLoan_amount() {
        return loan_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLoan_amount(double loan_amount) {
        this.loan_amount = loan_amount;
    }

    public void setApprovedDate(LocalDate approvedDate) {
        this.approvedDate = approvedDate;
    }
}
