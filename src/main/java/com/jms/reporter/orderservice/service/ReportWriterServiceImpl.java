package com.jms.reporter.orderservice.service;

import com.jms.reporter.orderservice.dto.OrderDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportWriterServiceImpl implements ReportWriterService {

    private final static String REPORT_PATH = "order_reports/";
    private final static String CSV_FILE_EXTENSION = ".csv";
    private final static String CSV_HEADER = "Date;Order_id;Customer_name;Product_name;Quantity_ordered_product;Measuring_unit;Product_type;Order_status;";
    private final static String CSV_ROW_TEMPLATE = "\n%s;%s;%s;%s;%s;%s;%s;%s;";


    @Override
    @SneakyThrows
    public void writeCSVReport(OrderDTO orderDTO, String fileName) {
        if (orderDTO == null || fileName == null || fileName.isEmpty()) {
            return;
        }
        String reportFilePath = REPORT_PATH + fileName + CSV_FILE_EXTENSION;

        if (!Files.exists(Paths.get(reportFilePath))) {
            Files.write(Paths.get(reportFilePath), CSV_HEADER.getBytes());
        }

        String csvData = orderDTO.getOrderItems().stream()
                .map(oi -> CSV_ROW_TEMPLATE.formatted(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        orderDTO.getUuid(),
                        orderDTO.getCustomer().fullName(),
                        oi.getProduct().getName(),
                        oi.quantityOrderedProduct(),
                        oi.getProduct().getMeasuringUnit(),
                        oi.getProduct().getType(),
                        orderDTO.getOrderStatus().name()))
                .collect(Collectors.joining(""));

        Files.write(
                Paths.get(reportFilePath),
                csvData.getBytes(),
                StandardOpenOption.APPEND);

        log.info("Write order [oderId: {}] for {} to report file with type {}",
                orderDTO.getUuid(),
                orderDTO.getCustomer(),
                orderDTO.getOrderStatus());
    }


}
