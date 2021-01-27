package org.mortgage;

public class Lender {
    private double availableFunds;

    public void addFunds(double funds) {
        availableFunds += funds;
    }

    public double getAvailableFunds() {
        return availableFunds;
    }

    public String checkLoanApplication(LoanApplication loanApplication) {
        if (loanApplication.getDti() < 36 && loanApplication.getCreditScore() > 620
                && loanApplication.getSavings() >= (0.25 * loanApplication.getRequestedAmount())) {
            loanApplication.setQualification("qualified");
            loanApplication.setStatus("qualified");
            loanApplication.setLoan_amount(loanApplication.getRequestedAmount());
            if(!processLoan(loanApplication)){
                return "Funds not available!! Do not Proceed!!";
            }
            return loanApplication.getQualification();
        } else if (loanApplication.getDti() < 36 && loanApplication.getCreditScore() > 620
                && (loanApplication.getRequestedAmount() / loanApplication.getSavings()) > 4) {
            loanApplication.setQualification("partially qualified");
            loanApplication.setLoan_amount(loanApplication.getRequestedAmount() - loanApplication.getSavings());
            if(!processLoan(loanApplication)){
                return "Funds not available!! Do not Proceed!!";
            }
            return loanApplication.getQualification();
        } else {
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
            this.availableFunds -= loanApplication.getRequestedAmount();
            return true;
        }

    }
}
