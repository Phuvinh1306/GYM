package com.hotrodoan.controller;

import com.hotrodoan.model.Member_Package;
import com.hotrodoan.model.VnPayPayment;
import com.hotrodoan.service.Member_PackageService;
import com.hotrodoan.service.VNPayService;
import com.hotrodoan.service.VnPayPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private VnPayPaymentService vnPayPaymentService;

    @Autowired
    private Member_PackageService member_packageService;


    @GetMapping("/pay")
    public String home(){
        return "index";
    }

    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model){
        int paymentStatus =vnPayService.orderReturn(request);

        Member_Package member_package = (Member_Package) request.getSession().getAttribute("member_package");
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        VnPayPayment vnPayPayment = new VnPayPayment();
        vnPayPayment.setVnpAmount(totalPrice);
        vnPayPayment.setVnpOrderInfo(orderInfo);
        vnPayPayment.setVnpPayDate(paymentTime);
        vnPayPayment.setVnpTransactionNo(transactionId);
        vnPayPaymentService.addVnPayPayment(vnPayPayment);

        if (paymentStatus == 1){
            member_packageService.addMember_Package(member_package);
            return "ordersuccess";
        }else
            return "orderfail";

//        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }
}