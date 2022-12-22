package com.example.books.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * 購物車類 (數據庫外的類)
 */
@Data
public class Cart {
    /**
     * 存放購物車中項目的集合<id,CartItem>
     */
    private Map<Integer,CartItem> cartItemMap;
    /**
     * 總金額
     */
    private Double total;
    /**
     * 購物車中單一項目的總數量
     */
    private Integer totalCount;
    /**
     * 購物車中所有項目的總數量
     */
    private Integer totalBookCount;

    public Double getTotal(){
        total = 0.0;
        if (cartItemMap != null && cartItemMap.size()>0){
            Set<Map.Entry<Integer,CartItem>> entries = cartItemMap.entrySet();
            Double total1 = 0.0;
            for (Map.Entry<Integer,CartItem> entry :entries){
                CartItem cartItem = entry.getValue();
                BigDecimal bigDecimalPrice =new BigDecimal(cartItem.getBookItem().getPrice()+"");
                BigDecimal bigDecimalBuyCount = new BigDecimal(""+cartItem.getBuycount());
                BigDecimal bigDecimalTotal1 = bigDecimalPrice.multiply(bigDecimalBuyCount);
                total1 = bigDecimalTotal1.doubleValue();
                total=total+total1;
            }
        }
        return total;
    }


    public Integer getTotalCount() {
        totalCount = 0 ;
        if (cartItemMap!=null &&cartItemMap.size()>0){
            totalCount  =cartItemMap.size();
        }
        return totalCount;
    }

    public Integer getTotalBookCount() {
        totalBookCount = 0 ;
        if (cartItemMap!=null &&cartItemMap.size()>0){
            for (CartItem cartItem : cartItemMap.values()){
                totalBookCount = totalBookCount + cartItem.getBuycount() ;
            }
        }
        return totalBookCount;
    }

}
