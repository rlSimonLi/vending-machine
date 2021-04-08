# Vending Machine
This is a Java website project using [Spark](https://sparkjava.com/). You can buy food as costumer, or stock up items and cash as admin/seller/cashier. It interacts with a Postgres database to access and keep track of inventory and account information.

## Demo Website
- https://vending.piran.xyz

## Demo Account/Password
- Admin: machinemaster/password
- Seller: stockmaster/password
- Cashier: cashmaster/password

## Build and Deploya

```gradle build```

A `DockerFile` and `docker-compose.yml` are included for easy deployment. Simply extract the distribution zip and place the files in the root directory, and:

```docker-compose up```