package com.example.model.impl;

import com.example.model.Operation;
import com.example.service.Util;

import java.util.Date;

/**
 * Объект класса представляет собой операцию запроса счёта для пользователя.
 */
public class BalanceInquiry extends Operation {

    public BalanceInquiry(Date date, User executor, Double amount) {
        super(date, executor, amount);
    }

    /**
     * Устанавливает пользователю сумму, указанную в логе.
     */
    @Override
    public void execute() {
        this.executor.setBalance(this.amount);
    }

    @Override
    public String toString() {
        return "[" + Util.get().getDateFormat().format(date) + "] "
                + this.executor.getName() + " balance inquiry " + this.amount;
    }
}
