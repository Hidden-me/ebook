package org.ebook.entity;

import org.ebook.util.DatabaseQuery;
import org.ebook.util.DatabaseUtils;

import java.sql.Timestamp;
import java.util.List;

public class OrderManager {
    private static String getOrderItemString(OrderItem item){
        if(item == null){
            return null;
        }
        String result = "{\"isbn\":\"" + item.getBookIsbn() + "\","
                + "\"title\":\"" + item.getBookTitle() + "\","
                + "\"author\":\"" + item.getBookAuthor() + "\","
                + "\"price\":\"" + item.getPrice() + "\","
                + "\"count\":\"" + item.getAmount() + "\"}";
        return result;
    }
    private static String getOrderString(Order order){
        if(order == null){
            return null;
        }
        int orderId = order.getOrderId();
        int uid = order.getBuyerUid();
        String date = order.getTimeCreate().toString();
        String username = UserManager.getUserByUid(uid).getUsername();
        String result = "{\"id\":\"" + orderId + "\","
                + "\"date\":\"" + date + "\","
                + "\"buyerUid\":\"" + uid + "\","
                + "\"buyerName\":\"" + username + "\","
                + "\"books\":[";
        List<OrderItem> items = DatabaseUtils.createQuery(OrderItem.class)
                .equal("orderId", orderId)
                .getResult();
        boolean first = true;
        for(OrderItem item : items){
            String str = getOrderItemString(item);
            if(str != null){
                if(first){
                    first = false;
                }else{
                    result += ",";
                }
                result += str;
            }
        }
        result += "]}";
        return result;
    }
    public static String getOrderListStringByBuyerUid(int uid, Timestamp start, Timestamp end){
        String result = "{\"orders\":[";
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
        boolean first = true;
        for(Order order : orders){
            String str = getOrderString(order);
            if(str != null){
                if(first){
                    first = false;
                }else{
                    result += ",";
                }
                result += str;
            }
        }
        result += "]}";
        return result;
    }
    public static String getOrderListStringByBuyerName(String username, Timestamp start, Timestamp end){
        int uid = UserManager.getUid(username);
        if(uid < 0){
            return getEmptyOrderListString();
        }
        return getOrderListStringByBuyerUid(uid, start, end);
    }
    public static String getEmptyOrderListString(){
        return "{\"orders\":[]}";
    }
}
