package com.jms.reporter.orderservice.consumer;

import com.jms.reporter.orderservice.dto.OrderDTO;
import com.jms.reporter.orderservice.service.ReportWriterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RejectedOrderConsumer {
    private static final String REJECTED_LIQUID_PRODUCT_ORDER_QUEUE = "rejected.liquid.product.order.queue";
    private static final String REJECTED_COUNTABLE_PRODUCT_ORDER_QUEUE = "rejected.countable.product.order.queue";
    private final static String REJECTED_ORDER_FILENAME = "rejected_orders";

    private final ReportWriterService reportWriterService;

    public RejectedOrderConsumer(ReportWriterService reportWriterService) {
        this.reportWriterService = reportWriterService;
    }

    @JmsListener(destination = REJECTED_LIQUID_PRODUCT_ORDER_QUEUE)
    @JmsListener(destination = REJECTED_COUNTABLE_PRODUCT_ORDER_QUEUE)
    public void consumeCountableProducts(@Payload OrderDTO orderDTO) {

        log.info("Consume rejected order. OrderId = {}. OrderType = {}. Customer = {}", orderDTO.getUuid(), orderDTO.getProductType(), orderDTO.getCustomer().fullName());
        reportWriterService.writeCSVReport(orderDTO, REJECTED_ORDER_FILENAME);
    }
}
