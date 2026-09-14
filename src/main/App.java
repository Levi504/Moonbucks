package main;

import model.OrderItem;
import model.Product;

public class Tests{
   public static void main(String[] args) {
      Product p1 = new Product("P001", "Capuccino", 9.99);
      OrderItem ot1 = new OrderItem(p1, "Or.001", 3);
      System.out.println("Votre Commande: " +
                        ot1.getProduct().getProductName() + " " + 
                        ot1.getOrderID() + " " + 
                        ot1.getQuantity());
   }
}