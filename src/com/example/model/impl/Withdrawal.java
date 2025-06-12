package com.example.model.impl;

import com.example.model.Operation;
import com.example.service.Util;

import java.util.Date;

/**
 * Объект класса представляет собой операцию снятия суммы.
 */
public class Withdrawal extends Operation {

    public Withdrawal(Date date, User executor, Double amount) {
        super(date, executor, amount);
    }

    /**
     * Вычитает сумму у пользователя.
     */
    @Override
    public void execute() {
        this.executor.setBalance(this.executor.getBalance() - this.amount);
    }

    @Override
    public String toString() {
        return "[" + Util.get().getDateFormat().format(date) + "] "
                + this.executor.getName()+ " withdrew " + this.amount;
    }
}
