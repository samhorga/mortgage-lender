package org.mortgage;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mortgage.exceptions.FundsNotAvailableException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class LenderTests {

    private Lender lender;
    private Candidate candidate;

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
    public void addMoneyAsLender() {
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
    public void approveLoansOnlyWhenIhaveAvailableFunds() {
        LoanApplication loanApplication = new LoanApplication(250000d, 21, 700,
                100000, "", 0, "", LocalDate.of(2023, 1, 24));
        String expected = lender.checkLoanApplication(loanApplication);
        assertEquals(expected, loanApplication.getQualification());
    }

    @Test
    public void loanWarningWhenIhaveInsufficientFunds() {
        LoanApplication loanApplication = new LoanApplication(550000d, 21, 700,
                100000, "", 0, "", LocalDate.of(2023, 1, 24));
        Assertions.assertThrows(FundsNotAvailableException.class, ()->{lender.checkLoanApplication(loanApplication); });
        assertEquals("on hold", loanApplication.getStatus());
    }

    /*
    Given I have approved a loan
    Then the requested loan amount is moved from available funds to pending funds
    And I see the available and pending funds reflect the changes accordingly
    */
    @Test
    public void moveAvailableFundsToPendingFundsIfApproved() {
        LoanApplication loanApplication = new LoanApplication(250000d, 21, 700,
                100000, "", 0, "", LocalDate.of(2023, 1, 24));
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
    public void processApprovedLoan() {
        LoanApplication loanApplication = new LoanApplication(250000d, 21, 700,
                100000, "", 0, "", LocalDate.of(2023, 1, 24));
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
        LoanApplication loanApplication = new LoanApplication(250000d, 21, 700,
                100000, "", 0, "", LocalDate.of(2023, 1, 24));
        lender.checkLoanApplication(loanApplication);

        candidate = new Candidate(loanApplication, lender);
        candidate.rejectLoanOffer(loanApplication);

        assertEquals(0, lender.getPendingFunds(), 0.1);
        assertEquals(400000, lender.getAvailableFunds(), 0.1);
        assertEquals("rejected", loanApplication.getStatus());
    }

    /*
    Given there is an approved loan offered more than 3 days ago
    When I check for expired loans
    Then the loan amount is move from the pending funds back to available funds
    And the loan status is marked as expired
     */
    @Test
    public void checkForExpirationOfTheLoan() {
        LoanApplication loanApplication = new LoanApplication(250000d, 21, 700,
                100000, "", 0, "", LocalDate.of(2021, 1, 24));

        lender.checkLoanApplication(loanApplication);

        lender.checkForExpiration(loanApplication, LocalDate.of(2021, 1, 27));

        assertEquals(400000, lender.getAvailableFunds(), 0.1);
        assertEquals("expired", loanApplication.getStatus());
    }

    /*
    Given there are loans in my system
    When I search by loan status (qualified, denied, on hold, approved, accepted, rejected, expired)
    Then I should see a list of loans and their details
     */
    @Test
    public void filterByStatus() {
        //approved
        LoanApplication loanApplication = new LoanApplication(250000d, 21, 700,
                100000, "", 0, "", LocalDate.of(2021, 1, 27));
        //on hold
        LoanApplication loanApplication1 = new LoanApplication(550000d, 21, 700,
                100000, "", 0, "", LocalDate.of(2021, 1, 27));
        //expired
        LoanApplication loanApplication2 = new LoanApplication(250000d, 21, 700,
                100000, "", 0, "", LocalDate.of(2021, 1, 24));
        //denied
        LoanApplication loanApplication3 = new LoanApplication(250000d, 21, 100,
                100000, "", 0, "", LocalDate.of(2021, 1, 24));


        //approved
        lender.checkLoanApplication(loanApplication);
        List<LoanApplication> loanApplicationsApproved = lender.filterBy("approved");
        assertFalse(loanApplicationsApproved.isEmpty());
        assertEquals(loanApplication, lender.getLoanApplicationList().get(0));
        assertEquals("approved", loanApplication.getStatus());

        //on hold
        lender = new Lender();
        Assertions.assertThrows(FundsNotAvailableException.class, ()->{lender.checkLoanApplication(loanApplication1); });
        List<LoanApplication> loanApplicationsOnHold = lender.filterBy("on hold");
        assertFalse(loanApplicationsOnHold.isEmpty());
        assertEquals("on hold", loanApplicationsOnHold.get(0).getStatus());

        //expired
        lender = new Lender();
        lender.addFunds(250000d);
        lender.checkLoanApplication(loanApplication2);
        lender.checkForExpiration(loanApplication2, LocalDate.of(2021, 1, 28));
        List<LoanApplication> loanApplicationsExpired = lender.filterBy("expired");
        assertEquals("expired", loanApplicationsExpired.get(0).getStatus());

        //denied
        lender = new Lender();
        lender.addFunds(250000d);
        lender.checkLoanApplication(loanApplication3);
        List<LoanApplication> loanApplicationsDenied = lender.filterBy("denied");
        assertEquals("denied", loanApplicationsDenied.get(0).getStatus());

        //accepted
        lender = new Lender();
        lender.addFunds(250000d);
        lender.checkLoanApplication(loanApplication);
        candidate = new Candidate(loanApplication, lender);
        candidate.acceptLoanOffer(loanApplication);
        List<LoanApplication> loanApplicationsAccepted = lender.filterBy("accepted");
        assertEquals("accepted", loanApplicationsAccepted.get(0).getStatus());

        //rejected
        lender = new Lender();
        lender.addFunds(250000d);
        lender.checkLoanApplication(loanApplication);
        candidate = new Candidate(loanApplication, lender);
        candidate.rejectLoanOffer(loanApplication);
        List<LoanApplication> loanApplicationsRejected = lender.filterBy("rejected");
        assertEquals("rejected", loanApplicationsRejected.get(0).getStatus());

    }
}
