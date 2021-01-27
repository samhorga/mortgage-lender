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


}
