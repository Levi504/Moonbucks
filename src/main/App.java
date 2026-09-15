package main;

import model.OrderItem;
import model.Product;
import model.Customer;
import model.Order;

public class Tests{
   public static void main(String[] args) {
      Product p1 = new Product("P001", "Capuccino", 9.99);
      OrderItem coffee = new OrderItem(p1, "Or.001", 3);
      Customer levi = new Customer("C001",
                                 "Levi DUBOIS",
                                 "5 Av. Jules Vernes 75600",
                                 "+33 6 05 04 02 03");
      Order o1 = new Order("O001", levi);
      System.out.println("Id de commande: " +
                        o1.getOrderID() + "\n" +
                        "Id du client: " +
                        o1.getCustomer().getCustomerID());
   }
}