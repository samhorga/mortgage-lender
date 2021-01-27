package org.mortgage;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class LoanApplicationTests {

    private LoanApplication loanApplication;
    private Candidate candidate;
    private Lender lender;

    @Before
    public void setUp() {
        lender = new Lender();
    }

    /* Given a loan applicant with <dti>, <credit_score>, and <savings>
    When they apply for a loan with <requested_amount>
    Then their qualification is <qualification>
    And their loan amount is <loan_amount>
    And their loan status is <status> */
    @Test
    public void checkLoanApplicationForFullAmount() {
        loanApplication = new LoanApplication(250000d, 21, 700, 100000,
                "", 0, "", LocalDate.of(2023, 1, 24));
        candidate = new Candidate(loanApplication, lender);
        lender.checkLoanApplication(candidate.getLoanApplication());
        assertEquals("qualified", candidate.getLoanApplication().getQualification());
        assertEquals(250000d, candidate.getLoanApplication().getLoan_amount(), 0.1);
    }

    @Test
    public void checkLoanApplicationForPartialAmount() {
        loanApplication = new LoanApplication(250000d, 30, 700,
                50000, "", 0, "", LocalDate.of(2023, 1, 24));
        candidate = new Candidate(loanApplication, lender);
        lender.checkLoanApplication(candidate.getLoanApplication());
        assertEquals("partially qualified", candidate.getLoanApplication().getQualification());
        assertEquals(200000d, candidate.getLoanApplication().getLoan_amount(), 0.1);
    }
}
