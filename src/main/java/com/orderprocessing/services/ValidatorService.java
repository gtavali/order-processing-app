package com.orderprocessing.services;

import com.orderprocessing.beans.InputFileBean;
import com.orderprocessing.beans.ResponseFileBean;
import com.orderprocessing.enums.Status;
import com.orderprocessing.repositories.OrderItemRepository;
import com.orderprocessing.repositories.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service for validations.
 *
 * @author Gabor Tavali
 */
@Service
@Slf4j
public class ValidatorService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    enum ValidationStatus {
        OK, ERROR
    }

    //For tests
    public ValidatorService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public List<ResponseFileBean> validateAnOrderRow(InputFileBean inputFileBean) {

        List<ResponseFileBean> validationResponse = new ArrayList<>();

        if (inputFileBean.getBuyerEmail().isEmpty() || !validateEmail(inputFileBean.getBuyerEmail())) {
            validationResponse.add(new ResponseFileBean(inputFileBean.getLineNumber(), ValidationStatus.ERROR.name(), "Invalid Email address."));
        }

        if (inputFileBean.getOrderDate().isEmpty()) {
            inputFileBean.setOrderDate(dateFormat.format(new Date()));
        } else {
            try {
                dateFormat.parse(inputFileBean.getOrderDate());
            } catch (ParseException ex) {
                validationResponse.add(new ResponseFileBean(inputFileBean.getLineNumber(), ValidationStatus.ERROR.name(), "Invalid date format. It has to be yyyy-MM-dd."));
            }
        }

        if (!validateSalePrice(inputFileBean.getSalePrice())) {
            validationResponse.add(new ResponseFileBean(inputFileBean.getLineNumber(), ValidationStatus.ERROR.name(), "Sale price must be positive decimal."));
        }

        if (!validateShippingPrice(inputFileBean.getShippingPrice())) {
            validationResponse.add(new ResponseFileBean(inputFileBean.getLineNumber(), ValidationStatus.ERROR.name(), "Shipping price must be at least 1.00."));
        }

        if (!validateStatus(inputFileBean.getStatus())) {
            validationResponse.add(new ResponseFileBean(inputFileBean.getLineNumber(), ValidationStatus.ERROR.name(), "Invalid status."));
        }

        if (!validateOrderId(inputFileBean.getOrderId())) {
            validationResponse.add(new ResponseFileBean(inputFileBean.getLineNumber(), ValidationStatus.ERROR.name(), "Order id is already existing."));
        }

        if (!validateOrderItemId(inputFileBean.getOrderItemId())) {
            validationResponse.add(new ResponseFileBean(inputFileBean.getLineNumber(), ValidationStatus.ERROR.name(), "Order item id is already existing."));
        }

        if (inputFileBean.getBuyerName().isEmpty()) {
            validationResponse.add(new ResponseFileBean(inputFileBean.getLineNumber(), ValidationStatus.ERROR.name(), "Buyer name can not be empty."));
        }

        if (inputFileBean.getAddress().isEmpty()) {
            validationResponse.add(new ResponseFileBean(inputFileBean.getLineNumber(), ValidationStatus.ERROR.name(), "Address can not be empty."));
        }

        if (inputFileBean.getSku().isEmpty()) {
            validationResponse.add(new ResponseFileBean(inputFileBean.getLineNumber(), ValidationStatus.ERROR.name(), "SKU can not be empty."));
        }

        return validationResponse;
    }

    private boolean validateEmail(String email) {
        String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).matches();
    }

    private boolean validateShippingPrice(double shippingPrice) {
        return shippingPrice >= 0.00d;
    }

    private boolean validateSalePrice(double salePrice) {
        return salePrice >= 1.00d;
    }

    private boolean validateStatus(String status) {
        return EnumUtils.isValidEnum(Status.class, status);
    }

    private boolean validateOrderId(Long orderId) {
        return !orderRepository.findById(orderId).isPresent();
    }

    private boolean validateOrderItemId(Long orderItemId) {
        return !orderItemRepository.findById(orderItemId).isPresent();
    }

}
