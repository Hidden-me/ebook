package org.ebook.controller;

import org.ebook.service.OrderService;
import org.ebook.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@RestController
public class OrderController {
    private OrderService order;
    private SessionService session;

    @Autowired
    public OrderController(OrderService order, SessionService session){
        this.order = order;
        this.session = session;
    }

    @GetMapping("/order")
    public ModelAndView getOrderView(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    @PostMapping("/order")
    public Map<String, Object> getOrderList(@RequestBody Map<String, Object> req){
        String username = session.getUsername();
        String identity = session.getIdentity();
        Map<String, Object> timeFilter = (Map<String, Object>) req.get("time");
        Map<String, Object> uidFilter = (Map<String, Object>) req.get("uid");
        Map<String, Object> orderIdFilter = (Map<String, Object>) req.get("orderid");
        Map<String, Object> isbnFilter = (Map<String, Object>) req.get("isbn");
        return order.getOrderList(username, identity, timeFilter,
                uidFilter, orderIdFilter, isbnFilter).getJSON();
    }
    @PostMapping("/admin/order")
    public Map<String, Object> getAllOrders(@RequestBody Map<String, Object> req){
        return getOrderList(req);
    }
}
