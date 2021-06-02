package com.BOEC.web;

import javax.servlet.http.HttpServletRequest;

import com.BOEC.model.processing.order.Order;
import com.BOEC.repository.OrderRepository;
import com.BOEC.service.util.PaypalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    OrderRepository orderRepository;

    private Logger log = LoggerFactory.getLogger(getClass());
    public static final String URL_PAYPAL_SUCCESS = "http://14.177.12.121:8088/payment/pay/success";
    public static final String URL_PAYPAL_CANCEL = "http://14.177.12.121:8088/payment/pay/cancel";
    public static final String EXTERNAL_URL_SUCCESS = "http://171.245.81.129:3000/paypal-success";
    public static final String EXTERNAL_URL_FAIL = "http://171.245.81.129:3000/paypal-fail";

    @Autowired
    private PaypalService paypalService;

    @PostMapping
    public String pay(@RequestParam int orderId,@RequestParam PaymentType paymentType){
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) return "not found id order";
        if(order.get().getStatus().equals(Order.OrderStatus.PAID_BY_CARD)) return "order has been paid";
        if(paymentType.equals(PaymentType.CREDIT_PAY)) {
            try {
                Payment payment = paypalService.createPayment(
                        order.get().getTotal()/230000,
                        "USD",
                        "payment description",
                        URL_PAYPAL_SUCCESS+"?orderId="+orderId,
                        URL_PAYPAL_CANCEL);
                for (Links links : payment.getLinks()) {
                    if (links.getRel().equals("approval_url")) {
                        return links.getHref();
                    }
                }
            } catch (PayPalRESTException e) {
                return e.getMessage();
            }
        } else {

        }

        return "fail";
    }

    @GetMapping("/pay/cancel")
    public RedirectView cancelPay(){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(EXTERNAL_URL_FAIL);
        return redirectView;
    }

    @GetMapping("/pay/success")
    public RedirectView successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
                                    @RequestParam int orderId){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                Optional<Order> order = orderRepository.findById(orderId);
                if (order.isPresent()) {
                    order.get().setStatus(Order.OrderStatus.PAID_BY_CARD);
                    orderRepository.save(order.get());
                }
                RedirectView redirectView = new RedirectView();
                redirectView.setUrl(EXTERNAL_URL_SUCCESS);
                return redirectView;
            }
        } catch (PayPalRESTException e) {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(EXTERNAL_URL_FAIL);
            return redirectView;
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(EXTERNAL_URL_FAIL);
        return redirectView;
    }

    public enum PaymentType {
        CASH_PAY,
        CREDIT_PAY
    }

}
