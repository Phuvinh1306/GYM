package com.hotrodoan.controller;

import com.hotrodoan.model.Member_Package;
import com.hotrodoan.model.Member_PackageSub;
import com.hotrodoan.model.VnPayPayment;
import com.hotrodoan.service.Member_PackageService;
import com.hotrodoan.service.Member_PackageSubService;
import com.hotrodoan.service.VNPayService;
import com.hotrodoan.service.VnPayPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "*")
//@org.springframework.stereotype.Controller
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private VnPayPaymentService vnPayPaymentService;

    @Autowired
    private Member_PackageService member_packageService;

    @Autowired
    private Member_PackageSubService member_packageSubService;


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
    public String vnPayPayment(HttpServletRequest request, Model model){
        int paymentStatus =vnPayService.orderReturn(request);

//        Object obj = request.getSession().getAttribute("m_p");
//        Member_Package m_p = (Member_Package) obj;
        HttpSession session = request.getSession();
        Member_Package m_p = (Member_Package) session.getAttribute("m_p");

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        int totalPriceInt = Integer.parseInt(totalPrice);
        totalPriceInt = totalPriceInt / 100;
        totalPrice = String.valueOf(totalPriceInt);

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date = dateFormat.parse(paymentTime);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            VnPayPayment vnPayPayment = new VnPayPayment();
            vnPayPayment.setVnpAmount(totalPrice);
            vnPayPayment.setVnpOrderInfo(orderInfo);
            vnPayPayment.setVnpPayDate(sqlDate);
            vnPayPayment.setVnpTransactionNo(transactionId);
            vnPayPaymentService.addVnPayPayment(vnPayPayment);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (paymentStatus == 1){
            if (orderInfo.contains("pack")){
                String idString = orderInfo.replace("pack", "");
                Long id = Long.parseLong(idString);
                Member_PackageSub memberPackageSub = member_packageSubService.getMember_PackageSub(id);
                member_packageService.createMember_PackageBySub(memberPackageSub);
                member_packageSubService.deleteMember_PackageSub(id);
            }
            return "ordersuccess";
        }else
            return "orderfail";

//        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }
}