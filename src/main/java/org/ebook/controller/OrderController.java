package org.ebook.controller;

import org.ebook.entity.OrderManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @GetMapping
    public ModelAndView getOrderView(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    @PostMapping
    public String getOrderList(){
        String username = null, identity = null;
        HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
        if(ss != null){
            username = (String) ss.getAttribute("username");
            identity = (String) ss.getAttribute("identity");
        }
        if(username == null || identity == null){
            return OrderManager.getEmptyOrderListString();
        }
        String result = OrderManager.getEmptyOrderListString();
        if(identity.equals("user")){
            result = OrderManager.getOrderListStringByBuyerName(username);
        }else if(identity.equals("admin")){
            // TODO: admin can see all the orders
        }
        return result;
    }
}
