package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {
    private HashMap<String,Order> orderMap;
    private HashMap<String,DeliveryPartner> partnerMap;
    private HashMap<String ,HashSet<String>> partnerToOrderMap;
    private HashMap<String,String> orderToPartnerMap;
    private Order order;

    public OrderRepository(){
        this.orderMap=new HashMap<String,Order>();
        this.partnerMap=new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap=new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap=new HashMap<String, String>();
    }
    public void saveOrder(Order Order){
        orderMap.put(order.getId(), order);
    }
    public void savePartner(String partnerID){
      DeliveryPartner deliveryPartner=new DeliveryPartner(partnerID);
      partnerMap.put(deliveryPartner.getId(), deliveryPartner);
    }
    public void saveOrderPartnerMap(String orderId,String partnerId){
      if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
          HashSet<String>currentOrders=new HashSet<String>();
          if(partnerToOrderMap.containsKey(partnerId))
              currentOrders=partnerToOrderMap.get(partnerId);
              currentOrders.add(orderId);
              partnerToOrderMap.put(partnerId, currentOrders);
              DeliveryPartner partner=partnerMap.get(partnerId);  //increase order count of partner
              partner.setNumberOfOrders(currentOrders.size());


              // assign partner to this order
          orderToPartnerMap.put(orderId,partnerId);
      }
    }

    public Order findOrderById(String orderId) {
        return orderMap.get(orderId);
    }
public DeliveryPartner findPartnerById(String partnerId){
    return partnerMap.get(partnerId);
    }
    public Integer findOrderCountByPartnerId(String partnerId){
        Integer orderCount=0;
        if(partnerMap.containsKey(partnerId)){
            orderCount=partnerMap.get(partnerId).getNumberOfOrders();
        }
        return orderCount;
    }
    public List<String> findOrdersByPartnerId(String partnerId){
        HashSet<String> orderList=new HashSet<String>();
        if(partnerToOrderMap.containsKey(partnerId)) orderList=partnerToOrderMap.get(partnerId);
        return new ArrayList<>(orderList);
    }
    public List<String> findAllOrders(){
        return new ArrayList<>(orderMap.keySet());
    }
    public void deletePartner(String partnerId){
        HashSet<String> orders=new HashSet<String>();
        if(partnerToOrderMap.containsKey(partnerId)){
            orders=partnerToOrderMap.get(partnerId);
            for(String order: orders){
                if(orderToPartnerMap.containsKey(order)){
                    //free all the orders assigned to this delivery partner
                    orderToPartnerMap.remove(order);
                }
            }
            partnerToOrderMap.remove(partnerId);
            if (partnerMap.containsKey(partnerId)){
                partnerMap.remove(partnerId);
            }
        }
    }
        public void deleteOrder(String orderId){
        if(orderToPartnerMap.containsKey(orderId)){
            String partnerId = orderToPartnerMap.get(orderId);
            HashSet<String>orders=partnerToOrderMap.get(partnerId);
            orders.remove(orderId);
            partnerToOrderMap.put(partnerId, orders);
            //change order count of partner
            DeliveryPartner partner=partnerMap.get(partnerId);
            partner.setNumberOfOrders(orders.size());
        }
        if (orderMap.containsKey(orderId)){
            orderMap.remove(orderId);
        }
    }
    public Integer findCountOfUnassignedOrders(){
        Integer countOfOrders=0;
        List<String> orders = new ArrayList<>(orderMap.keySet());
        for (String orderId: orders){
            if(!orderToPartnerMap.containsKey(orderId)) {
                countOfOrders += 1;
            }
        }
        return countOfOrders;
    }
    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString,String partnerId){
        Integer hour =Integer.valueOf(timeString.substring(0,2));
        Integer minutes= Integer.valueOf(timeString.substring(3));
        Integer time = hour*60 + minutes;
        Integer countOfOrders=0;
        if (partnerToOrderMap.containsKey(partnerId)){
            HashSet<String>orders=partnerToOrderMap.get(partnerId);
            for(String order:orders){
                if(orderMap.containsKey(order)){
                    Order currOrder = orderMap.get(order);
                    if(time<currOrder.getDeliveryTime()){
                        countOfOrders += 1;
                    }
                }
            }
        }
        return countOfOrders;
}
public String findLastDeliveryPartnerByPartnerId(String partnerId){
        Integer time=0;
        if(partnerToOrderMap.containsKey(partnerId)){
            HashSet<String> orders = partnerToOrderMap.get(partnerId);
            for (String order: orders){
                if (orderMap.containsKey(order)){
                    Order currOrder = orderMap.get(order);
                    time=Math.max(time,currOrder.getDeliveryTime());
                }
            }
        }
        // converting time to string
        Integer hour=time/60;
        Integer minutes=time%60;
        String hourInString=String.valueOf(hour);
        String minString = String.valueOf(minutes);
        if(hourInString.length()==1){
            hourInString="0" + minString;
        }
        return hourInString + ";" + minString;
}


    public String findLastDeliveryTimeByPartnerId(String partnerId) {
        return partnerId;
    }
}
