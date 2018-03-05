package com.vortexnepal.paypalApp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.vortexnepal.paypalApp.config.PaypalPaymentIntent;
import com.vortexnepal.paypalApp.config.PaypalPaymentMethod;
import com.vortexnepal.paypalApp.repository.ProductRepository;
import com.vortexnepal.paypalApp.service.PaypalService;
import com.vortexnepal.paypalApp.utility.URLLocation;

@Controller
@RequestMapping("/")
public class PaymentController {
	
	public static final String PAYPAL_SUCCESS_URL = "pay/success"; 
	public static final String PAYPAL_CANCEL_URL = "pay/cancel";
	
	@Autowired
	private PaypalService paypalService;
	
	@Autowired
	private ProductRepository productRepo;
	
	@GetMapping
	public String home(Model model) {
		model.addAttribute("products", productRepo.findAll());
		return "index";
	}
	
	@PostMapping(value="pay/{price}")
	public String pay(HttpServletRequest request, @PathVariable("price") double price) {
		String cancelUrl = URLLocation.getBaseUrl(request)+ "/" + PAYPAL_CANCEL_URL;
		String successUrl = URLLocation.getBaseUrl(request)+ "/" + PAYPAL_SUCCESS_URL;
		
		try {
			Payment payment = paypalService.createPayment(price, "USD", 
					PaypalPaymentMethod.paypal, PaypalPaymentIntent.sale,
					"I want the Product very Bad", cancelUrl, successUrl);
			
			for(Links links : payment.getLinks()){
				if(links.getRel().equals("approval_url")){
					return "redirect:" + links.getHref();
				}
			}
			
		}catch(PayPalRESTException pe) {
			pe.printStackTrace();
		}
		return "redirect:/";
	}
	
	@GetMapping(value=PAYPAL_CANCEL_URL)
	public String cancel() {
		return "cancel";
	}
	
	@GetMapping(value=PAYPAL_SUCCESS_URL)
	public String success(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if(payment.getState().equals("approved")) {
				return "success";
			}	
		}catch(PayPalRESTException pe) {
			pe.printStackTrace();
		}
		
		return "redirect:/";
	}
}
