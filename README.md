# Project Architecture

src/
├── main/
│   ├── App.java                      <- Menus uniquement
│   ├── controller/
│   │   ├── CustomerController.java   <- Customer Actions
│   │   ├── ProductController.java    <- Product Actions
│   │   └── OrderController.java      <- Order Actions
│   └── util/
│       └── InputHelper.java          <- Helpers UI
├── data/
│   ├── DataManager.java              <- CRUD
│   ├── EntityType.java               <- Enum
│   ├── FileUtils.java                <- Helpers for files reading
│   ├── CustomerView.java             <- customers Display
│   ├── ProductView.java              <- products Display
│   └── OrderView.java                <- orders Display
└── model/
    ├── Customer.java
    ├── Product.java
    ├── FragileProduct.java
    ├── NonFragileProduct.java
    ├── Order.java
    └── OrderItem.java