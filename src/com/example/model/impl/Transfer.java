package com.example.model.impl;

import com.example.model.Operation;
import com.example.service.Util;

import java.util.Date;

/**
 * Объект класса представляет собой операцию перевода суммы между пользователями.
 */
public class Transfer extends Operation {
    private final User target;

    public Transfer(Date date, User executor, User target, Double amount) {
        super(date, executor, amount);
        this.target = target;
    }

    /**
     * Снимает сумму с одного счёта и переводит её на другой.
     */
    @Override
    public void execute() {
        this.executor.setBalance(this.executor.getBalance()-this.amount);
        this.target.setBalance(this.target.getBalance()+this.amount);
    }

    /**
     * @return Строковое представление операции для переводящего.
     */
    @Override
    public String toString() {
        return "[" + Util.get().getDateFormat().format(date) + "] "
                + this.executor.getName() + " transferred " + this.amount
                + " to " + this.target.getName();
    }

    /**
     * @return Строковое представление операции для получающего.
     */
    public String toStringSwapUsers() {
        return "[" + Util.get().getDateFormat().format(date) + "] "
                + this.target.getName() + " received " + this.amount
                + " from " + this.executor.getName();
    }

    public User getTarget(){
        return this.target;
    }
}
