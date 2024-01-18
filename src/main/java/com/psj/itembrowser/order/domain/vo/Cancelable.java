package com.psj.itembrowser.order.domain.vo;

@FunctionalInterface
public interface Cancelable {
    public boolean isNotCancelable();
}