package com.example.model.impl;

import com.example.model.Operation;
import com.example.service.Util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class User {

    private Long id;
    private String name;
    private Double balance;
    private List<Operation> operations;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", operations=" + operations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public User(String name){
        this.id = Util.get().
                getIdByNameUser(name);
        this.name = name;
        this.balance = 0d;
        this.operations = new ArrayList<>();
    }

    public Long getId(){
        return this.id;
    }

    public void setBalance(Double amount){
        this.balance = amount;
    }

    public Double getBalance(){
        return this.balance;
    }

    public String getName(){
        return this.name;
    }

    public String getFinalBalance(){
        return "[" + Util.get().getDateFormat().format(Date.from(Instant.now())) + "] "
                + this.getName() + " final balance " + this.balance;
    }

    public List<Operation> getOperations(){
        return this.operations;
    }

    public void addOperation(Operation op){
        this.operations.add(op);
    }
}
