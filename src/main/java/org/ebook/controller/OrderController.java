package org.ebook.controller;

import org.ebook.service.OrderService;
import org.ebook.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    private OrderService order;
    private SessionService session;

    @Autowired
    public OrderController(OrderService order, SessionService session){
        this.order = order;
        this.session = session;
    }

    @GetMapping
    public ModelAndView getOrderView(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    @PostMapping
    public Map<String, Object> getOrderList(@RequestBody Map<String, Object> req){
        String username = session.getUsername();
        String identity = session.getIdentity();
        Map<String, Object> timeFilter = (Map<String, Object>) req.get("time");
        return order.getOrderList(username, identity, timeFilter).getJSON();
    }
}
