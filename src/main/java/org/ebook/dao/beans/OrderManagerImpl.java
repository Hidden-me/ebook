package org.ebook.dao.beans;

import org.ebook.dao.OrderManager;
import org.ebook.dao.UserManager;
import org.ebook.entity.Order;
import org.ebook.entity.OrderItem;
import org.ebook.util.DatabaseQuery;
import org.ebook.util.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class OrderManagerImpl implements OrderManager {
    private UserManager userManager;

    @Autowired
    public OrderManagerImpl(UserManager userManager){
        this.userManager = userManager;
    }

    private Map<String, Object> getOrderItemJSON(OrderItem item){
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
    private Map<String, Object> getOrderJSON(Order order){
        return getOrderJSON(order, null);
    }
    private Map<String, Object> getOrderJSON(Order order, String includeIsbn){
        if(order == null){
            return null;
        }
        int orderId = order.getOrderId();
        int uid = order.getBuyerUid();
        String date = order.getTimeCreate().toString();
        String username = userManager.getUserByUid(uid).getUsername();
        DatabaseQuery<OrderItem> query = DatabaseUtils.createQuery(OrderItem.class)
                .equal("orderId", orderId);
        if(includeIsbn != null){
            query.equal("bookIsbn", includeIsbn);
        }
        List<OrderItem> items = query.getResult();
        if(items.isEmpty()){
            // does not contain the required book
            return null;
        }
        List<Object> list = new ArrayList<>();
        for(OrderItem item : items){
            Map<String, Object> tmp = getOrderItemJSON(item);
            if(tmp != null){
                list.add(tmp);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", orderId);
        map.put("date", date);
        map.put("buyerUid", uid);
        map.put("buyerName", username);
        map.put("books", list);
        return map;
    }
    public List<Object> getOrderListJSONByBuyerUid(int uid, Timestamp start, Timestamp end){
        return getFilteredOrderListJSON(start, end, uid, null, null);
    }
    public List<Object> getOrderListJSONByBuyerName(String username, Timestamp start, Timestamp end){
        int uid = userManager.getUid(username);
        if(uid < 0){
            return getEmptyOrderListJSON();
        }
        return getOrderListJSONByBuyerUid(uid, start, end);
    }
    public List<Object> getFilteredOrderListJSON(Timestamp start, Timestamp end, Integer uid, Integer orderId, String isbn){
        List<Object> list = new ArrayList<>();
        DatabaseQuery<Order> query = DatabaseUtils.createQuery(Order.class);
        // time filter
        if(start != null && end != null){
            query.between("timeCreate", start, end);
        }else if(start != null){
            query.ge("timeCreate", start);
        }else if(end != null){
            query.le("timeCreate", end);
        }
        // uid filter
        if(uid != null){
            query.equal("buyerUid", uid);
        }
        // orderId filter
        if(orderId != null){
            query.equal("orderId", orderId);
        }
        List<Order> orders = query.getResult();
        for(Order order : orders){
            Map<String, Object> tmp = getOrderJSON(order, isbn);
            if(tmp != null){
                list.add(tmp);
            }
        }
        return list;
    }
    public List<Object> getEmptyOrderListJSON(){
        return new ArrayList<Object>();
    }
    public List<Object> getAllOrdersJSON(){
        List<Object> list = new LinkedList<>();
        DatabaseQuery<Order> query = DatabaseUtils.createQuery(Order.class);
        List<Order> orders = query.getResult();
        for(Order order : orders){
            Map<String, Object> tmp = getOrderJSON(order);
            if(tmp != null){
                list.add(tmp);
            }
        }
        return list;
    }
}
