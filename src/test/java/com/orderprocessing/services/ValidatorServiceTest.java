package com.orderprocessing.services;

import com.orderprocessing.beans.InputFileBean;
import com.orderprocessing.beans.ResponseFileBean;
import com.orderprocessing.entities.Order;
import com.orderprocessing.entities.OrderItem;
import com.orderprocessing.repositories.OrderItemRepository;
import com.orderprocessing.repositories.OrderRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Test for {@link ValidatorService}
 *
 * @author Gabor Tavali
 */
@RunWith(MockitoJUnitRunner.class)
public class ValidatorServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderItemRepository orderItemRepository;

    private ValidatorService validatorService;

    @Before
    public void setUp() {
        validatorService = new ValidatorService(orderRepository, orderItemRepository);
    }

    @Test
    public void testValidateAnOrderRow() {
        InputFileBean inputFileBeanToValidation = new InputFileBean();
        inputFileBeanToValidation.setLineNumber(1);
        inputFileBeanToValidation.setOrderId(1L);
        inputFileBeanToValidation.setOrderItemId(1L);
        inputFileBeanToValidation.setBuyerName("");
        inputFileBeanToValidation.setBuyerEmail("");
        inputFileBeanToValidation.setAddress("");
        inputFileBeanToValidation.setSku("");
        inputFileBeanToValidation.setStatus("");
        inputFileBeanToValidation.setOrderDate("");

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(new Order()));
        Mockito.when(orderItemRepository.findById(1L)).thenReturn(Optional.of(new OrderItem()));

        List<ResponseFileBean> response = validatorService.validateAnOrderRow(inputFileBeanToValidation);

        Assert.assertEquals(8, response.size());
        Assert.assertEquals("Invalid Email address.", response.get(0).getMessage());
        Assert.assertEquals("Sale price must be positive decimal.", response.get(1).getMessage());
        Assert.assertEquals("Invalid status.", response.get(2).getMessage());
        Assert.assertEquals("Order id is already existing.", response.get(3).getMessage());
        Assert.assertEquals("Order item id is already existing.", response.get(4).getMessage());
        Assert.assertEquals("Buyer name can not be empty.", response.get(5).getMessage());
        Assert.assertEquals("Address can not be empty.", response.get(6).getMessage());
        Assert.assertEquals("SKU can not be empty.", response.get(7).getMessage());

        inputFileBeanToValidation.setBuyerName("John Doe");
        inputFileBeanToValidation.setBuyerEmail("john@john.com");
        inputFileBeanToValidation.setAddress("Boston");
        inputFileBeanToValidation.setPostcode(1);
        inputFileBeanToValidation.setShippingPrice(1d);
        inputFileBeanToValidation.setSku("sku");
        inputFileBeanToValidation.setStatus("IN_STOCK");
        inputFileBeanToValidation.setOrderDate("2018-08-02");
        inputFileBeanToValidation.setSalePrice(10d);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        response = validatorService.validateAnOrderRow(inputFileBeanToValidation);
        Assert.assertEquals(0, response.size());

        inputFileBeanToValidation.setSalePrice(-10d);
        inputFileBeanToValidation.setShippingPrice(0d);
        inputFileBeanToValidation.setStatus("invalid status");
        inputFileBeanToValidation.setOrderDate("2018/08/02");

        response = validatorService.validateAnOrderRow(inputFileBeanToValidation);

        Assert.assertEquals(3, response.size());
        Assert.assertEquals("Invalid date format. It has to be yyyy-MM-dd.", response.get(0).getMessage());
        Assert.assertEquals("Sale price must be positive decimal.", response.get(1).getMessage());
        Assert.assertEquals("Invalid status.", response.get(2).getMessage());

        inputFileBeanToValidation.setOrderDate("");

        validatorService.validateAnOrderRow(inputFileBeanToValidation);

        Assert.assertEquals(ValidatorService.dateFormat.format(new Date()), inputFileBeanToValidation.getOrderDate());

    }
}
