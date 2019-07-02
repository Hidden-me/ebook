package org.ebook.service.beans;

import org.ebook.dao.OrderManager;
import org.ebook.service.OrderService;
import org.ebook.util.DateUtils;
import org.ebook.util.JSONResponse;
import org.ebook.util.ParserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderManager orderManager;

    @Autowired
    public OrderServiceImpl(OrderManager orderManager){
        this.orderManager = orderManager;
    }

    public JSONResponse getOrderList(String username, String identity, Map<String, Object> timeFilter,
                                     Map<String, Object> uidFilter, Map<String, Object> orderIdFilter,
                                     Map<String, Object> isbnFilter){
        JSONResponse resp = new JSONResponse();
        if(username == null || identity == null){
            resp.put("orders", orderManager.getEmptyOrderListJSON());
            return resp;
        }
        // time filter
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
        List<Object> result = orderManager.getEmptyOrderListJSON();
        if(identity.equals("user")){
            result = orderManager.getOrderListJSONByBuyerName(username, start, end);
        }else if(identity.equals("admin")){
            try{
                // admin filters
                Integer uid = null;
                if((Boolean) uidFilter.get("enabled")){
                    uid = ParserUtils.parseInt(uidFilter.get("value"));
                }
                Integer orderId = null;
                if((Boolean) orderIdFilter.get("enabled")){
                    orderId = ParserUtils.parseInt(orderIdFilter.get("value"));
                }
                String isbn = null;
                if((Boolean) isbnFilter.get("enabled")){
                    isbn = ParserUtils.parseString(isbnFilter.get("value"));
                }
                result = orderManager.getFilteredOrderListJSON(start, end, uid, orderId, isbn);
            }catch(ParseException e){
                e.printStackTrace();
                resp.setMessage("查询参数类型有误，查询失败");
                resp.fail();
                return resp;
            }
        }
        resp.put("orders", result);
        resp.succeed();
        return resp;
    }
}
