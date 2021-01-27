package org.mortgage;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LenderTests {

    Lender lender;

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
        assertEquals(expected,loanApplication.getStatus());
    }

    @Test
    public void loanWarningWhenIhaveInsufficientFunds(){
        LoanApplication loanApplication = new LoanApplication(550000d, 21, 700, 100000, "", 0, "");
        assertEquals("Funds not available!! Do not Proceed!!",lender.checkLoanApplication(loanApplication));
        assertEquals("on hold",loanApplication.getStatus());
    }
}
