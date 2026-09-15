package main;

import data.DataManager;
import data.EntityType;

public class Tests{
   public static void main(String[] args) {
      try {
         DataManager.search(EntityType.CUSTOMER, "C004");
      } catch (Exception e) {
         System.out.println("Erreur: " + e);
      }
   }
}