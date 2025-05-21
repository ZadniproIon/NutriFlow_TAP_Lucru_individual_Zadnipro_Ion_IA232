package com.myapp.nutrition.model;

import java.io.Serializable;
import java.time.LocalDate;

public class HydrationEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate date;
    private double amountMl;

    public HydrationEntry() { /* pentru serializare */ }

    public HydrationEntry(LocalDate date, double amountMl) {
        this.date = date;
        this.amountMl = amountMl;
    }

    public LocalDate getDate() { return date; }
    public double getAmountMl()   { return amountMl; }
}
