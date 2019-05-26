package org.ebook.service.beans;

import org.ebook.dao.OrderManager;
import org.ebook.service.OrderService;
import org.ebook.util.DateUtils;
import org.ebook.util.JSONResponse;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    public JSONResponse getOrderList(String username, String identity, Map<String, Object> timeFilter){
        JSONResponse resp = new JSONResponse();
        if(username == null || identity == null){
            resp.put("orders", OrderManager.getEmptyOrderListJSON());
            return resp;
        }
        boolean timeFilterEnabled = (Boolean) timeFilter.get("enabled");
        Timestamp start = null;
        Timestamp end = null;
        if(timeFilterEnabled){
            String format = "yyyy-MM-dd HH:mm";
            String startStr = (String) timeFilter.get("start");
            String endStr = (String) timeFilter.get("end");
            startStr = startStr.replace('T', ' ');
            endStr = endStr.replace('T', ' ');
            start = DateUtils.parse(startStr, format);
            end = DateUtils.parse(endStr, format);
        }
        List<Object> result = OrderManager.getEmptyOrderListJSON();
        if(identity.equals("user")){
            result = OrderManager.getOrderListJSONByBuyerName(username, start, end);
        }else if(identity.equals("admin")){
            // TODO: admin can see all the orders
        }
        resp.put("orders", result);
        resp.succeed();
        return resp;
    }
}
