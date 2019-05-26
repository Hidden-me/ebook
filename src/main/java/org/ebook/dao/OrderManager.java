package org.ebook.dao;

import org.ebook.entity.Order;
import org.ebook.entity.OrderItem;
import org.ebook.util.DatabaseQuery;
import org.ebook.util.DatabaseUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManager {
    private static Map<String, Object> getOrderItemJSON(OrderItem item){
        if(item == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("isbn", item.getBookIsbn());
        map.put("title", item.getBookTitle());
        map.put("author", item.getBookAuthor());
        map.put("price", item.getPrice());
        map.put("count", item.getAmount());
        return map;
    }
    private static Map<String, Object> getOrderJSON(Order order){
        if(order == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        int orderId = order.getOrderId();
        int uid = order.getBuyerUid();
        String date = order.getTimeCreate().toString();
        String username = UserManager.getUserByUid(uid).getUsername();
        map.put("id", orderId);
        map.put("date", date);
        map.put("buyerUid", uid);
        map.put("buyerName", username);
        List<OrderItem> items = DatabaseUtils.createQuery(OrderItem.class)
                .equal("orderId", orderId)
                .getResult();
        List<Object> list = new ArrayList<>();
        for(OrderItem item : items){
            Map<String, Object> tmp = getOrderItemJSON(item);
            if(tmp != null){
                list.add(tmp);
            }
        }
        map.put("books", list);
        return map;
    }
    public static List<Object> getOrderListJSONByBuyerUid(int uid, Timestamp start, Timestamp end){
        List<Object> list = new ArrayList<>();
        DatabaseQuery<Order> query = DatabaseUtils.createQuery(Order.class)
                .equal("buyerUid", uid);
        if(start != null && end != null){
            query.between("timeCreate", start, end);
        }else if(start != null){
            query.ge("timeCreate", start);
        }else if(end != null){
            query.le("timeCreate", end);
        }
        List<Order> orders = query.getResult();
        for(Order order : orders){
            Map<String, Object> tmp = getOrderJSON(order);
            if(tmp != null){
                list.add(tmp);
            }
        }
        return list;
    }
    public static List<Object> getOrderListJSONByBuyerName(String username, Timestamp start, Timestamp end){
        int uid = UserManager.getUid(username);
        if(uid < 0){
            return getEmptyOrderListJSON();
        }
        return getOrderListJSONByBuyerUid(uid, start, end);
    }
    public static List<Object> getEmptyOrderListJSON(){
        return new ArrayList<Object>();
    }
}
