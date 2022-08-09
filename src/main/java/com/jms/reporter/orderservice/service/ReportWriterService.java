package com.jms.reporter.orderservice.service;

import com.jms.reporter.orderservice.dto.OrderDTO;

public interface ReportWriterService {
    void writeCSVReport(OrderDTO orderDTO, String fileName);
}
