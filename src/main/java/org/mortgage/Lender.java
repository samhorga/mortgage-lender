package org.mortgage;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lender {
    private List<LoanApplication> loanApplicationList = new ArrayList<>();
    private double availableFunds;
    private double pendingFunds;

    public void addFunds(double funds) {
        availableFunds += funds;
    }

    public double getAvailableFunds() {
        return availableFunds;
    }

    public double getPendingFunds() {
        return pendingFunds;
    }

    public void setAvailableFunds(double availableFunds) {
        this.availableFunds = availableFunds;
    }

    public void setPendingFunds(double pendingFunds) {
        this.pendingFunds = pendingFunds;
    }

    public String checkLoanApplication(LoanApplication loanApplication) {
        if (loanApplication.getDti() < 36 && loanApplication.getCreditScore() > 620
                && loanApplication.getSavings() >= (0.25 * loanApplication.getRequestedAmount())) {
            this.loanApplicationList.add(loanApplication);
            loanApplication.setQualification("qualified");
            loanApplication.setStatus("qualified");
            loanApplication.setLoan_amount(loanApplication.getRequestedAmount());
            if(!processLoan(loanApplication)){
                return "Funds not available!! Do not Proceed!!";
            }
            this.pendingFunds = loanApplication.getRequestedAmount();
            this.availableFunds-= loanApplication.getRequestedAmount();

            return loanApplication.getQualification();
        } else if (loanApplication.getDti() < 36 && loanApplication.getCreditScore() > 620
                && (loanApplication.getRequestedAmount() / loanApplication.getSavings()) > 4) {
            this.loanApplicationList.add(loanApplication);
            loanApplication.setQualification("partially qualified");
            loanApplication.setLoan_amount(loanApplication.getRequestedAmount() - loanApplication.getSavings());
            if(!processLoan(loanApplication)){
                return "Funds not available!! Do not Proceed!!";
            }
            this.pendingFunds = loanApplication.getRequestedAmount();
            this.availableFunds-= loanApplication.getRequestedAmount();
            return loanApplication.getQualification();
        } else {
            this.loanApplicationList.add(loanApplication);
            loanApplication.setStatus("denied");
            loanApplication.setQualification("not qualified");
            return loanApplication.getQualification();
        }

    }

    public boolean processLoan(LoanApplication loanApplication){
        if(loanApplication.getLoan_amount() > this.availableFunds)
        {
            loanApplication.setStatus("on hold");
            return false;
        }
        else{
            loanApplication.setStatus("approved");
            return true;
        }

    }

    public void checkForExpiration(LoanApplication loanApplication) {
        Period period = Period.between(loanApplication.getApprovedDate(), LocalDate.now());
        int diff = Math.abs(period.getDays());
        if(diff >= 3) {
            //Then the loan amount is move from the pending funds back to available funds
            this.setAvailableFunds(this.getAvailableFunds() + this.getPendingFunds());
            this.setPendingFunds(this.getPendingFunds() - loanApplication.getRequestedAmount());
            loanApplication.setStatus("expired");
        }
    }

    public List<LoanApplication> getLoanApplicationList() {
        return loanApplicationList;
    }

    public List<LoanApplication> filterBy(String criteria) {
        return loanApplicationList.stream().filter(loanApplication -> loanApplication.getStatus().equals(criteria)).collect(Collectors.toList());
    }
}
