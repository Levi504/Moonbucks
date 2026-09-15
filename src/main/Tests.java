package main;

import model.OrderItem;
import model.Product;
import model.Customer;
import model.Order;
import data.DataManager;
import data.EntityType;

public class Tests{
   public static void main(String[] args) {
      try {
         DataManager.view(EntityType.CUSTOMER);
      } catch (Exception e) {
         System.out.println("Erreur: " + e);
      }
   }
}