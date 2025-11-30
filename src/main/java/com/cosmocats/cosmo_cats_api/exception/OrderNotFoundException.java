package com.cosmocats.cosmo_cats_api.exception;

public class OrderNotFoundException extends ResourceNotFoundException {

    private static final String MESSAGE_BY_ID = "Order not found with id: %d";
    private static final String MESSAGE_BY_NUMBER = "Order not found with number: %s";

    public OrderNotFoundException(Long id) {
        super(String.format(MESSAGE_BY_ID, id));
    }

    public OrderNotFoundException(String orderNumber) {
        super(String.format(MESSAGE_BY_NUMBER, orderNumber));
    }
}
