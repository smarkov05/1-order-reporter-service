package com.jms.reporter.orderservice.consumer;

import com.jms.reporter.orderservice.dto.OrderDTO;
import com.jms.reporter.orderservice.service.ReportWriterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AcceptedOrderConsumer {
    private static final String ACCEPTED_LIQUID_PRODUCT_ORDER_QUEUE = "accepted.liquid.product.order.queue";
    private static final String ACCEPTED_COUNTABLE_PRODUCT_ORDER_QUEUE = "accepted.countable.product.order.queue";
    private final static String ACCEPTED_ORDER_FILENAME = "accepted_orders";


    private final ReportWriterService reportWriterService;

    public AcceptedOrderConsumer(ReportWriterService reportWriterService) {
        this.reportWriterService = reportWriterService;
    }

    @JmsListener(destination = ACCEPTED_LIQUID_PRODUCT_ORDER_QUEUE)
    @JmsListener(destination = ACCEPTED_COUNTABLE_PRODUCT_ORDER_QUEUE)
    public void consumeCountableProducts(@Payload OrderDTO orderDTO) {

        log.info("Consume accepted order. OrderId = {}. OrderType = {}. Customer = {}", orderDTO.getUuid(), orderDTO.getProductType(), orderDTO.getCustomer().fullName());

        reportWriterService.writeCSVReport(orderDTO, ACCEPTED_ORDER_FILENAME);
    }
}