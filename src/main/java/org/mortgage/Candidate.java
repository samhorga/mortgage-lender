package org.mortgage;

public class Candidate {

    private LoanApplication loanApplication;

    private Lender lender;

    public Candidate(LoanApplication loanApplication, Lender lender) {
        this.loanApplication = loanApplication;
        this.lender = lender;
    }

    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }

    public void acceptLoanOffer(LoanApplication loanApplication) {
        lender.setPendingFunds(lender.getPendingFunds() - loanApplication.getRequestedAmount());
        loanApplication.setStatus("accepted");
    }

    public void rejectLoanOffer(LoanApplication loanApplication) {
        lender.setAvailableFunds(lender.getAvailableFunds() + lender.getPendingFunds());
        lender.setPendingFunds(lender.getPendingFunds() - loanApplication.getRequestedAmount());
        loanApplication.setStatus("rejected");
    }
}
