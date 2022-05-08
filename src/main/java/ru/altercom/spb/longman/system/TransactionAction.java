package ru.altercom.spb.longman.system;

import java.util.function.Supplier;

public interface TransactionAction<T> extends Supplier<T> {
}
