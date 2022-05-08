package ru.altercom.spb.longman.system;

public interface TransactionManager {

    <T> T doInTransaction(TransactionAction<T> action);

}
