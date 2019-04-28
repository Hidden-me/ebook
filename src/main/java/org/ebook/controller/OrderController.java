package org.ebook.controller;

import org.ebook.entity.OrderManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;

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
    public String getOrderList(@RequestBody Map<String, Object> req){
        String username = null, identity = null;
        HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
        if(ss != null){
            username = (String) ss.getAttribute("username");
            identity = (String) ss.getAttribute("identity");
        }
        if(username == null || identity == null){
            return OrderManager.getEmptyOrderListString();
        }
        Map<String, Object> time = (Map<String, Object>) req.get("time");
        boolean timeFilterEnabled = (Boolean) time.get("enabled");
        Timestamp start = null;
        Timestamp end = null;
        if(timeFilterEnabled){
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String startStr = (String) time.get("start");
            String endStr = (String) time.get("end");
            startStr = startStr.replace('T', ' ');
            endStr = endStr.replace('T', ' ');
            try{
                start = new Timestamp(sdf.parse(startStr).getTime());
                end = new Timestamp(sdf.parse(endStr).getTime());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        String result = OrderManager.getEmptyOrderListString();
        if(identity.equals("user")){
            result = OrderManager.getOrderListStringByBuyerName(username, start, end);
        }else if(identity.equals("admin")){
            // TODO: admin can see all the orders
        }
        return result;
    }
}
