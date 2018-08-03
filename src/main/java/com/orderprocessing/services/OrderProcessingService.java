package com.orderprocessing.services;

import com.orderprocessing.beans.InputFileBean;
import com.orderprocessing.beans.ResponseFileBean;
import com.orderprocessing.entities.Order;
import com.orderprocessing.entities.OrderItem;
import com.orderprocessing.enums.Status;
import com.orderprocessing.repositories.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Order processing service for prepare and persist orders and report.
 *
 * @author Gabor Tavali
 */
@Service
@Slf4j
public class OrderProcessingService {

    @Autowired
    CsvService csvService;

    @Autowired
    ValidatorService validatorService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    FtpService ftpService;

    public void processOrder(List<InputFileBean> inputFile) throws ParseException {
        List<Order> orders = new ArrayList<>();
        List<ResponseFileBean> responseFile = new ArrayList<>();

        String reponseFileName = "response.csv";

        Map<Long, List<InputFileBean>> mapByOrderId = inputFile.stream()
                .collect(Collectors.groupingBy(InputFileBean::getOrderId, Collectors.toList())
        );

        for (Map.Entry<Long, List<InputFileBean>> entry : mapByOrderId.entrySet()) {

            List<OrderItem> orderItems = entry.getValue().stream().map(inputFileBean -> {

                List<ResponseFileBean> validationResult = validatorService.validateAnOrderRow(inputFileBean);

                if (!validationResult.isEmpty()) {
                    responseFile.addAll(validationResult);
                    log.error("Validation failed at line " + inputFileBean.getLineNumber() + "! See the response file: " + reponseFileName + " for further details.");
                    csvService.writeResponseFile(responseFile, reponseFileName);
                    System.exit(1);
                }

                log.info("Validation was successful at line " + inputFileBean.getLineNumber() + ".");

                responseFile.add(new ResponseFileBean(inputFileBean.getLineNumber(), ValidatorService.ValidationStatus.OK.name(), ""));
                csvService.writeResponseFile(responseFile, reponseFileName);

                return new OrderItem(
                        inputFileBean.getOrderItemId(),
                        inputFileBean.getSalePrice(),
                        inputFileBean.getShippingPrice(),
                        inputFileBean.getSku(),
                        Status.valueOf(inputFileBean.getStatus()));
            }).collect(Collectors.toList());

            InputFileBean firstItem = entry.getValue().get(0);
            Order order = new Order(
                    entry.getKey(),
                    firstItem.getBuyerName(),
                    firstItem.getBuyerEmail(),
                    ValidatorService.dateFormat.parse(firstItem.getOrderDate()),
                    firstItem.getAddress(),
                    firstItem.getPostcode(),
                    orderItems
            );
            orders.add(order);
        }

        ftpService.upload(new File("./" + reponseFileName));

        orders.forEach(order -> orderRepository.save(order));
        log.info("Order is saved to database.");

    }

}
