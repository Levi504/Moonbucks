package main;

import model.Product;

public class TestProduct{
   public static void main(String[] args) {
      Product p1 = new Product("P001", "Capuccino", 9.99);
      System.out.println("Prix du produit: " + p1.getProductRate());
   }
}