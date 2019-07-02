package org.ebook.service;

import org.ebook.util.JSONResponse;

import java.util.Map;

public interface OrderService {
    JSONResponse getOrderList(String username, String identity, Map<String, Object> timeFilter,
                              Map<String, Object> uidFilter, Map<String, Object> orderIdFilter,
                              Map<String, Object> isbnFilter);
}
