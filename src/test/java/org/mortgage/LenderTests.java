package org.mortgage;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LenderTests {

    Lender lender;
    Candidate candidate;

    @Before
    public void setUp() {
        lender = new Lender();
        lender.addFunds(400000);
    }

    /* When I check my available funds
    Then I should see how much funds I currently have*/
    @Test
    public void showLenderAvailableFunds() {
        assertEquals(400000, lender.getAvailableFunds(), 0.1);
    }

    /* Given I have <current_amount> available funds
       When I add <deposit_amount>
       Then my available funds should be <total> */

    @Test
    public void addMoneyALender() {
        lender.addFunds(100000);

        assertEquals(500000, lender.getAvailableFunds(), 0.1);
    }
/*
Given I have <available_funds> in available funds
When I process a qualified loan
Then the loan status is set to <status>

When I process a not qualified loan
Then I should see a warning to not proceed
 */

    @Test
public void approveLoansOnlyWhenIhaveAvailableFunds(){
        LoanApplication loanApplication = new LoanApplication(250000d, 21, 700, 100000, "", 0, "");
        String expected = lender.checkLoanApplication(loanApplication);
        assertEquals(expected,loanApplication.getQualification());
    }

    @Test
    public void loanWarningWhenIhaveInsufficientFunds(){
        LoanApplication loanApplication = new LoanApplication(550000d, 21, 700, 100000, "", 0, "");
        assertEquals("Funds not available!! Do not Proceed!!",lender.checkLoanApplication(loanApplication));
        assertEquals("on hold",loanApplication.getStatus());
    }

    /*
    Given I have approved a loan
    Then the requested loan amount is moved from available funds to pending funds
    And I see the available and pending funds reflect the changes accordingly
    */
    @Test
    public void moveAvailableFundsToPendingFundsIfApproved() {
        LoanApplication loanApplication = new LoanApplication(250000d, 21, 700, 100000, "", 0, "");
        double expectedAvailableFunds = lender.getAvailableFunds() - loanApplication.getRequestedAmount();
        double expectedPendingFunds = loanApplication.getRequestedAmount();
        lender.checkLoanApplication(loanApplication);
        assertEquals(expectedAvailableFunds, lender.getAvailableFunds(), 0.1);
        assertEquals(expectedPendingFunds, lender.getPendingFunds(), 0.1);
    }

    /*
    Given I have an approved loan
    When the applicant accepts my loan offer
    Then the loan amount is removed from the pending funds
    And the loan status is marked as accepted
     */

    @Test
    public void processAprovedLoan() {
        LoanApplication loanApplication = new LoanApplication(250000d, 21, 700, 100000, "", 0, "");
        lender.checkLoanApplication(loanApplication);
        candidate = new Candidate(loanApplication, lender);
        candidate.acceptLoanOffer(loanApplication);

        assertEquals(0, lender.getPendingFunds(), 0.1);
        assertEquals("accepted", loanApplication.getStatus());
    }

    /*
    Given I have an approved loan
    When the applicant rejects my loan offer
    Then the loan amount is moved from the pending funds back to available funds
    And the loan status is marked as rejected
     */
    @Test
    public void processRejectedLoan() {
        LoanApplication loanApplication = new LoanApplication(250000d, 21, 700, 100000, "", 0, "");
        lender.checkLoanApplication(loanApplication);
        // AVAILABLE FUNDS = 400000 - 250000d = 150000
        // PENDING FUNDS = 250000d
        candidate = new Candidate(loanApplication, lender);
        candidate.rejectLoanOffer(loanApplication);
        // AVAILABLE FUNDS 250000d = 150000 + 250000d = 400000
        // PENDING FUNDS = 0

        assertEquals(0, lender.getPendingFunds(), 0.1);
        assertEquals(400000, lender.getAvailableFunds(), 0.1);
        assertEquals("rejected", loanApplication.getStatus());
    }
}
