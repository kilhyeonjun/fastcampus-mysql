package com.example.fastcampusmysql.domain.post;


import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class PageHelper {

  public static String orderBy(Sort sort) {
    if (sort.isEmpty()) {
      return "id DESC";
    }

    List<Order> orders = sort.toList();
    var orderBys = orders.stream()
        .map(order -> order.getProperty() + " " + order.getDirection())
        .toList();

    return String.join(", ", orderBys);
  }
}
