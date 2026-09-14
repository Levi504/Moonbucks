package main;

import model.Customer;

public class Tests{
   public static void main(String[] args) {
      Customer c1 = new Customer("C001", "Levi", "110 Rue CDG 92100", "+33 6 85 45 76 32");
      System.out.println("Adresse client: " + c1.getCustomerAddress());
   }
}