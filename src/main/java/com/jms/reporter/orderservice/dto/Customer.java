package com.jms.reporter.orderservice.dto;

import java.util.UUID;

public record Customer (
        UUID uuid,
        String fullName) {
}
