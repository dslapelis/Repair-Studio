# Repair-Studio
Repair business SaaS

This is a SaaS that I have been intermittently working on during my free time to test my abilities both server-side and application-side. 

If you'd like a working example, I'd be happy to publish the app on the Play Store for testing purposes.

HOW IT WORKS:

- Server Side
  * REST API written using the Slim Framework in PHP.
  * Login, register, authenticate, data, addcustomer, and readcustomers functions.
  * Login takes an email and a password -- returns a randomly generated token and that token is stored in a MySQL table.
  * Register takes an email, password, and name. Returns a successful or an unsuccessful response.
  * Authenticate takes a token from the Authentication header. Returns a success or unsuccessful response.
  * data takes a token from the Authentication header. Returns the details for the respective user.
  * addcustomer takes a token in the header and a name, email, and phone number from the body. Returns a successful or unsuccessful response.
  * readcustomers takes a token in the header and returns all of the customers corresponding to the user associated with the token.
  
- Client Side
  * Written in Java using Android Studio
  * Begins with a splash screen. If an auth token exists in shared preferences, it will try to authenticate with the token. If unsuccessful, a proper login is required.
  * A user can login or register a new account.
  * After login, a user is presented with a navigation drawer with many options. The only ones currently working are the dashboard and the customers page.
  * The user can add a new customer and see the list of their current customers.
  * A user can logout. This will clear all shared preferences and bring them back to the login page. 
