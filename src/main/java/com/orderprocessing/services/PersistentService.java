package com.orderprocessing.services;

import com.orderprocessing.beans.InputFileBean;
import com.orderprocessing.beans.ResponseFileBean;
import com.orderprocessing.entities.Order;
import com.orderprocessing.entities.OrderItem;
import com.orderprocessing.enums.Status;
import com.orderprocessing.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Persistent service for prepare and persist orders and report.
 *
 * @author Gabor Tavali
 */
@Service
public class PersistentService {

    @Autowired
    CsvService csvService;

    @Autowired
    ValidatorService validatorService;

    @Autowired
    OrderRepository orderRepository;

    public void processInputFile(List<InputFileBean> inputFile) {
        List<Order> orders = new ArrayList<>();
        List<ResponseFileBean> responseFile = new ArrayList<>();

        Map<Long, List<InputFileBean>> mapByOrderId = inputFile.stream()
                .collect(Collectors.groupingBy(InputFileBean::getOrderId, Collectors.toList())
        );

        for (Map.Entry<Long, List<InputFileBean>> entry : mapByOrderId.entrySet()) {

            List<OrderItem> orderItems = entry.getValue().stream().map(inputFileBean -> {

                List<ResponseFileBean> validationResult = validatorService.validateAnOrderRow(inputFileBean);

                if (!validationResult.isEmpty()) {
                    responseFile.addAll(validationResult);
                    System.out.println("Validation failed!");
                    csvService.writeResponseFile(responseFile);
                    System.exit(1);
                }

                responseFile.add(new ResponseFileBean(inputFileBean.getLineNumber(), ValidatorService.ValidationStatus.OK.name(), ""));
                csvService.writeResponseFile(responseFile);

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
                    firstItem.getOrderDate(),
                    firstItem.getAddress(),
                    firstItem.getPostcode(),
                    orderItems
            );
            orders.add(order);
        }

        orders.forEach(order -> orderRepository.save(order));

    }

}
