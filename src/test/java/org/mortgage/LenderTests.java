package org.mortgage;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LenderTests {

    Lender lender;

    @Before
    public void setUp() {
        lender = new Lender();
    }

    /* When I check my available funds
    Then I should see how much funds I currently have*/
    @Test
    public void showLenderAvailableFunds() {
        lender.addFunds(400000);

        assertEquals(400000, lender.getAvailableFunds(), 0.1);
    }

    /* Given I have <current_amount> available funds
       When I add <deposit_amount>
       Then my available funds should be <total> */

    @Test
    public void addMoneyALender() {
        lender.addFunds(40000);
        lender.addFunds(10000);

        assertEquals(50000, lender.getAvailableFunds(), 0.1);
    }
}
