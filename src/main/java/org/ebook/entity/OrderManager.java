package org.ebook.entity;

import org.ebook.util.DatabaseUtils;

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
    public static String getOrderListStringByBuyerUid(int uid){
        String result = "{\"orders\":[";
        List<Order> orders = DatabaseUtils.createQuery(Order.class)
                .equal("buyerUid", uid)
                .getResult();
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
    public static String getOrderListStringByBuyerName(String username){
        int uid = UserManager.getUid(username);
        if(uid < 0){
            return getEmptyOrderListString();
        }
        return getOrderListStringByBuyerUid(uid);
    }
    public static String getEmptyOrderListString(){
        return "{\"orders\":[]}";
    }
}
