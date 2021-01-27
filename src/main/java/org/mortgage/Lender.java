package org.mortgage;

public class Lender {
    private double availableFunds;

    public void addFunds(double funds) {
        availableFunds+=funds;
    }

    public double getAvailableFunds() {
        return availableFunds;
    }
}
