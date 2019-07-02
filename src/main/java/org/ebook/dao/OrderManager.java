package org.ebook.dao;

import java.sql.Timestamp;
import java.util.List;

public interface OrderManager {
    List<Object> getOrderListJSONByBuyerUid(int uid, Timestamp start, Timestamp end);
    List<Object> getOrderListJSONByBuyerName(String username, Timestamp start, Timestamp end);
    List<Object> getFilteredOrderListJSON(Timestamp start, Timestamp end, Integer uid, Integer orderId, String isbn);
    List<Object> getEmptyOrderListJSON();
    List<Object> getAllOrdersJSON();
}
