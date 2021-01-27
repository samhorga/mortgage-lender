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
            return loanApplication.getQualification();
        } else if (loanApplication.getDti() < 36 && loanApplication.getCreditScore() > 620
                && (loanApplication.getRequestedAmount() / loanApplication.getSavings()) > 4) {
            loanApplication.setQualification("partially qualified");
            loanApplication.setStatus("partially qualified");
            loanApplication.setLoan_amount(loanApplication.getRequestedAmount() - loanApplication.getSavings());
            return loanApplication.getQualification();
        } else {
            loanApplication.setQualification("not qualified");
            loanApplication.setStatus("denied");
            return loanApplication.getQualification();
        }
    }
}
