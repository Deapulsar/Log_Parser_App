package com.example.model;

import com.example.model.constants.OperationType;
import com.example.model.impl.BalanceInquiry;
import com.example.model.impl.Transfer;
import com.example.model.impl.User;
import com.example.model.impl.Withdrawal;
import com.example.service.Util;

import java.util.Date;

/**
 * Объект класса представляет собой операцию над пользователями.
 */
abstract public class Operation {
    protected final Date date;
    protected final User executor;
    protected final Double amount;

    public Operation(Date date, User executor, Double amount){
        this.date = date;
        this.executor = executor;
        this.amount = amount;
    }

    /**
     * Разбивает строку из log-файла на части, создает и возвращает соответсвующую операцию.
     * @param line - строка описывающая операцию.
     * @return соответсвующая строке операция.
     */
    public static Operation get(String line){
        Util util = Util.get();
        Date date = util.datePart(line);
        Double amount = util.amountPart(line);
        OperationType type = util.operationTypePart(line);
        User executor = util.executorPart(line);
        if (type.equals(OperationType.valueOf("TRANSFERRED"))) {
            User target = util.targetPart(line);
            return new Transfer(date,executor,target,amount);
        }
        else if (type.equals(OperationType.valueOf("BALANCE"))) return new BalanceInquiry(date,executor,amount);
        else return new Withdrawal(date,executor,amount);
    }

    /**
     * Выполнение операции над пользователем/пользователями.
     */
    abstract public void execute();

    @Override
    public String toString() {
        return ""+this.getClass().getSimpleName()+"{" +
                "date=" + date +
                ", executor=" + executor +
                ", amount=" + amount +
                '}';
    }

    public Date getDate(){
        return this.date;
    }

    public User getExecutor(){
        return this.executor;
    }
}
