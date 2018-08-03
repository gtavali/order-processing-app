package com.orderprocessing.services;

import com.orderprocessing.beans.InputFileBean;
import com.orderprocessing.beans.ResponseFileBean;
import com.orderprocessing.enums.Status;
import com.orderprocessing.repositories.OrderItemRepository;
import com.orderprocessing.repositories.OrderRepository;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service for validations.
 *
 * @author Gabor Tavali
 */
@Service
public class ValidatorService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    enum ValidationStatus {
        OK, ERROR
    }

    public List<ResponseFileBean> validateAnOrderRow(InputFileBean inputFileBean) {

        List<ResponseFileBean> validationResponse = new ArrayList<>();

        if (inputFileBean.getBuyerEmail().isEmpty() || !validateEmail(inputFileBean.getBuyerEmail())) {
            validationResponse.add(new ResponseFileBean(inputFileBean.getLineNumber(), ValidationStatus.ERROR.name(), "Invalid Email address."));
        }

        if (null == inputFileBean.getOrderDate()) {

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
        return orderRepository.findById(orderId).isPresent();
    }

    private boolean validateOrderItemId(Long orderItemId) {
        return orderItemRepository.findById(orderItemId).isPresent();
    }

}
