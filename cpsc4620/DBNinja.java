package cpsc4620;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

/*
 * This file is where most of your code changes will occur You will write the code to retrieve
 * information from the database, or save information to the database
 * 
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 * 
 * This class also has static string variables for pickup, delivery and dine-in. If your database
 * stores the strings differently (i.e "pick-up" vs "pickup") changing these static variables will
 * ensure that the comparison is checking for the right string in other places in the program. You
 * will also need to use these strings if you store this as boolean fields or an integer.
 * 
 * 
 */

/**
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
	private static Connection conn;

	// Change these variables to however you record dine-in, pick-up and delivery, and sizes and crusts
	public final static String pickup = "pickup";
	public final static String delivery = "delivery";
	public final static String dine_in = "dinein";

	public final static String size_s = "Small";
	public final static String size_m = "Medium";
	public final static String size_l = "Large";
	public final static String size_xl = "XLarge";

	public final static String crust_thin = "Thin";
	public final static String crust_orig = "Original";
	public final static String crust_pan = "Pan";
	public final static String crust_gf = "Gluten-Free";



	
	private static boolean connect_to_db() throws SQLException, IOException {

		try {
			conn = DBConnector.make_connection();
			return true;
		} catch (SQLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	
	public static void addOrder(Order o) throws SQLException, IOException 
	{
		connect_to_db();
		/*
		 * add code to add the order to the DB. Remember that we're not just
		 * adding the order to the order DB table, but we're also recording
		 * the necessary data for the delivery, dinein, and pickup tables
		 * 
		 */
	

		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void addPizza(Pizza p) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Add the code needed to insert the pizza into into the database.
		 * Keep in mind adding pizza discounts and toppings associated with the pizza,
		 * there are other methods below that may help with that process.
		 * 
		 */
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	
	public static void useTopping(Pizza p, Topping t, boolean isDoubled) throws SQLException, IOException //this method will update toppings inventory in SQL and add entities to the Pizzatops table. Pass in the p pizza that is using t topping
	{
		connect_to_db();
		/*
		 * This method should do 2 two things.
		 * - update the topping inventory every time we use t topping (accounting for extra toppings as well)
		 * - connect the topping to the pizza
		 *   What that means will be specific to your yimplementatinon.
		 * 
		 * Ideally, you should't let toppings go negative....but this should be dealt with BEFORE calling this method.
		 * 
		 */
		
		
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	
	public static void usePizzaDiscount(Pizza p, Discount d) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * This method connects a discount with a Pizza in the database.
		 * 
		 * What that means will be specific to your implementatinon.
		 */
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void useOrderDiscount(Order o, Discount d) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * This method connects a discount with an order in the database
		 * 
		 * You might use this, you might not depending on where / how to want to update
		 * this information in the dabast
		 */
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void addCustomer(Customer c) throws SQLException, IOException {
		connect_to_db();
		/*
		 * This method adds a new customer to the database.
		 * 
		 */
		try {
			connect_to_db();
			String sql = "insert into customer(CustomerFirstName, CustomerLastName, CustomerPhoneNo) values(?, ?, ?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, c.getFName());
			preparedStatement.setString(2, c.getLName());
			preparedStatement.setString(3, c.getPhone());
			preparedStatement.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static void completeOrder(Order o) throws SQLException, IOException {
		/*
		 * Find the specifed order in the database and mark that order as complete in the database.
		 * 
		 */

		try {
			connect_to_db();

			String updateStatement = "update ordert set IsCompleted = 1 where OrdertID = " + o.getOrderID() + " ;";

			PreparedStatement preparedStatement = conn.prepareStatement(updateStatement);

			preparedStatement.executeUpdate();
			String completed="Completed";
			String updatePizzaStatement = "update pizza set PizzaState = ?  where PizzaOrderID = ?" ;

			PreparedStatement pizzaPreparedStatement = conn.prepareStatement(updatePizzaStatement);
			pizzaPreparedStatement.setString(1,"Completed");
			pizzaPreparedStatement.setInt(2,o.getOrderID());

			pizzaPreparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		


		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	public static ArrayList<Order> getOrders(boolean openOnly) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Return an arraylist of all of the orders.
		 * 	openOnly == true => only return a list of open (ie orders that have not been marked as completed)
		 *           == false => return a list of all the orders in the database
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 * 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		 */
		ArrayList<Order> orders = new ArrayList<Order>();

		try {
			connect_to_db();

			String selectQuery = "select * from ordert";
			if (openOnly) {
				selectQuery += " where (IsCompleted = false)";
			}
			selectQuery += " order by OrdertTimeStamp desc;";

			Statement statement = conn.createStatement();

			ResultSet record = statement.executeQuery(selectQuery);

			while (record.next()) {
				Integer orderId = record.getInt("OrdertID");
				String orderType = record.getString("OrdertType");
				Integer customerId = record.getInt("OrdertCustomerID");
				Double orderCost = record.getDouble("OrdertCustomerPrice");
				Double orderPrice = record.getDouble("OrdertBusinessPrice");
				String orderTimeStamp = record.getString("OrdertTimeStamp");
				Integer OrderCompleteState = record.getInt("IsCompleted");

				orders.add(
						new Order(orderId, customerId, orderType, orderTimeStamp, orderCost, orderPrice, OrderCompleteState));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}



		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return orders;
	}
	
	public static Order getLastOrder(){
		/*
		 * Query the database for the LAST order added
		 * then return an Order object for that order.
		 * NOTE...there should ALWAYS be a "last order"!
		 */
		




		 return null;
	}

	public static ArrayList<Order> getOrdersByDate(String date){
		/*
		 * Query the database for ALL the orders placed on a specific date
		 * and return a list of those orders.
		 *  
		 */
		ArrayList<Order> orders = new ArrayList<Order>();

		try {
			connect_to_db();

			String selectQuery = "select * from ordert";

			selectQuery += " where (OrdertTimeStamp >= '" + date + " 00:00:00')";

			selectQuery += " order by OrdertTimeStamp desc;";

			Statement statement = conn.createStatement();

			ResultSet record = statement.executeQuery(selectQuery);

			while (record.next()) {
				Integer orderId = record.getInt("OrdertID");
				String orderType = record.getString("OrdertType");
				Integer customerId = record.getInt("OrdertCustomerID");
				Double orderCost = record.getDouble("OrdertCustomerPrice");
				Double orderPrice = record.getDouble("OrdertBusinessPrice");
				String orderTimeStamp = record.getString("OrdertTimeStamp");
				Integer OrderCompleteState = record.getInt("IsCompleted");

				orders.add(
						new Order(orderId, customerId, orderType, orderTimeStamp, orderCost, orderPrice, OrderCompleteState));

			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		




		 return orders;
	}
	public static ArrayList<Order> getCompletedOrders() throws SQLException, IOException {
		connect_to_db();
		/*
		 * Return an arraylist of all of the orders.
		 * 	openOnly == true => only return a list of open (ie orders that have not been marked as completed)
		 *           == false => return a list of all the orders in the database
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 *
		 * Don't forget to order the data coming from the database appropriately.
		 *
		 */
		ArrayList<Order> orders = new ArrayList<Order>();

		try {
			connect_to_db();

			String selectQuery = "select * from ordert";

				selectQuery += " where (IsCompleted = True)";

			selectQuery += " order by OrdertTimeStamp desc;";

			Statement statement = conn.createStatement();

			ResultSet record = statement.executeQuery(selectQuery);

			while (record.next()) {
				Integer orderId = record.getInt("OrdertID");
				String orderType = record.getString("OrdertType");
				Integer customerId = record.getInt("OrdertCustomerID");
				Double orderCost = record.getDouble("OrdertCustomerPrice");
				Double orderPrice = record.getDouble("OrdertBusinessPrice");
				String orderTimeStamp = record.getString("OrdertTimeStamp");
				Integer OrderCompleteState = record.getInt("IsCompleted");

				orders.add(
						new Order(orderId, customerId, orderType, orderTimeStamp, orderCost, orderPrice, OrderCompleteState));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}




		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return orders;
	}
		
	public static ArrayList<Discount> getDiscountList() throws SQLException, IOException {
		connect_to_db();
		/* 
		 * Query the database for all the available discounts and 
		 * return them in an arrayList of discounts.
		 * 
		*/
		
		
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return null;
	}

	public static Discount findDiscountByName(String name){
		/*
		 * Query the database for a discount using it's name.
		 * If found, then return an OrderDiscount object for the discount.
		 * If it's not found....then return null
		 *  
		 */




		 return null;
	}


	public static ArrayList<Customer> getCustomerList() throws SQLException, IOException {
		connect_to_db();
		/*
		 * Query the data for all the customers and return an arrayList of all the customers. 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		*/
		ArrayList<Customer> customers = new ArrayList<Customer>();
		try {
			String sql = "SELECT * FROM customer";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			ResultSet records = preparedStatement.executeQuery();
			while (records.next()) {
				int customerID = records.getInt("CustomerID");
				String firstName = records.getString("CustomerFirstName");
				String lastName = records.getString("CustomerLastName");
				String phoneNo = records.getString("CustomerPhoneNo");


				customers.add(
						new Customer(customerID, firstName, lastName, phoneNo));

			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}


		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return customers;
	}

	public static Customer findCustomerByPhone(String phoneNumber){
		/*
		 * Query the database for a customer using a phone number.
		 * If found, then return a Customer object for the customer.
		 * If it's not found....then return null
		 *  
		 */

		/*
		 * Query the data for all the customers and return an arrayList of all the customers.
		 * Don't forget to order the data coming from the database appropriately.
		 *
		 */
		return null;
	}


	public static ArrayList<Topping> getToppingList() throws SQLException, IOException {
		connect_to_db();
		/*
		 * Query the database for the aviable toppings and 
		 * return an arrayList of all the available toppings. 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		 */
		ArrayList<Topping> toppings = new ArrayList<Topping>();
		try {
			connect_to_db();
			String sql = "SELECT * FROM topping";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			ResultSet records = preparedStatement.executeQuery();
			while (records.next()) {
				int toppingID = records.getInt("ToppingID");
				String toppingName = records.getString("ToppingName");
				Double toppingCustomerPrice = records.getDouble("ToppingCustomerPrice");
				Double toppingBusinessPrice = records.getDouble("ToppingBusinessPrice");
				Double toppingQuantityForPersonal = records.getDouble("ToppingQuantityForPersonal");
				Double toppingQuantityForMediumUnits = records.getDouble("ToppingQuantityForMediumUnits");
				Double toppingQuantityForLargeUnits = records.getDouble("ToppingQuantityForLargeUnits");
				Double toppingQuantityForXLargeUnits = records.getDouble("ToppingQuantityForXLargeUnits");
				int toppingMinInvLvl = records.getInt("ToppingMinInvLvl");
				int toppingCurrentInvLvl = records.getInt("ToppingCurrentInvLvl");
				toppings.add(
						new Topping(toppingID,toppingName, toppingCustomerPrice, toppingBusinessPrice, toppingQuantityForPersonal,
								toppingQuantityForMediumUnits,toppingQuantityForLargeUnits,toppingQuantityForXLargeUnits,toppingMinInvLvl
								,toppingCurrentInvLvl));

			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return toppings;

	}

	public static Topping findToppingByName(String name){
		/*
		 * Query the database for the topping using it's name.
		 * If found, then return a Topping object for the topping.
		 * If it's not found....then return null
		 *  
		 */
		




		 return null;
	}


	public static void addToInventory(Topping t, double quantity) throws SQLException, IOException {
		/*
		 * Updates the quantity of the topping in the database by the amount specified.
		 * 
		 * */
		try {
			connect_to_db();
			String sql = "UPDATE topping SET ToppingCurrentInvLvl = ToppingCurrentInvLvl+? WHERE ToppingID = ?";
			Connection conn = DBConnector.make_connection();
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setDouble(1, quantity);
			preparedStatement.setInt(2, t.getTopID());
			preparedStatement.executeUpdate();
			/*
			 * Adds toAdd amount of topping to topping t.
			 */
			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}








		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		/* 
		 * Query the database fro the base customer price for that size and crust pizza.
		 * 
		*/
		
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return 0.0;
	}

	public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		/* 
		 * Query the database fro the base business price for that size and crust pizza.
		 * 
		*/
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return 0.0;
	}

	public static void printInventory() throws SQLException, IOException {

		/*
		 * Queries the database and prints the current topping list with quantities.
		 *  
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */
		try {
			connect_to_db();

			String sql = "SELECT ToppingID, ToppingName, ToppingCurrentInvLvl FROM topping order by ToppingName";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			ResultSet results = preparedStatement.executeQuery();

			while (results.next()) {
				System.out.println("ToppingID: " + results.getString("ToppingID") + " | Name: " + results.getString("ToppingName") + " | CurrentInvLvl: " + results.getString("ToppingCurrentInvLvl"));
			}

			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}


		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION


	}
	
	public static void printToppingPopReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ToppingPopularity view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */
		try {
			String maxOrdSql = "SELECT * FROM ToppingPopularity";
			PreparedStatement prepared = conn.prepareStatement(maxOrdSql);
			ResultSet report = prepared.executeQuery();
			int maxOrderID = -1;
			System.out.printf("%-20s  %-4s %n", "Topping", "ToppingCount");
			while (report.next()) {
				String topping = report.getString("Topping");
				Integer toppingCount = report.getInt("ToppingCount");
				System.out.printf("%-20s  %-4s %n", topping, toppingCount);
			}

			//DO NOT FORGET TO CLOSE YOUR CONNECTION
			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}


		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void printProfitByPizzaReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByPizza view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */
		try {
			String maxOrdSql = "SELECT * FROM ProfitByPizza";
			PreparedStatement prepared = conn.prepareStatement(maxOrdSql);
			ResultSet report = prepared.executeQuery();
			System.out.printf("%-15s  %-15s  %-10s %-30s%n", "Size", "Crust", "Profit", "OrderMonth");
			while (report.next()) {

				String size = report.getString("Size");
				String crust = report.getString("Crust");
				Double profit = report.getDouble("Profit");
				String orderDate = report.getString("OrderMonth");

				System.out.printf("%-15s  %-15s  %-10s %-30s%n", size, crust, profit, orderDate);

			}

			//DO NOT FORGET TO CLOSE YOUR CONNECTION
			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void printProfitByOrderType() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByOrderType view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */
		try {
			String maxOrdSql = "SELECT * FROM ProfitByOrderType";
			PreparedStatement prepared = conn.prepareStatement(maxOrdSql);
			ResultSet report = prepared.executeQuery();
			System.out.printf("%-15s  %-15s  %-18s %-18s %-8s%n", "CustomerType", "OrderMonth", "TotalOrderPrice",
					"TotalOrderCost", "Profit");
			while (report.next()) {

				String customerType = report.getString("CustomerType");
				String orderMonth = report.getString("OrderMonth");
				Double totalPrice = report.getDouble("TotalOrderPrice");
				Double totalCost = report.getDouble("TotalOrderCost");
				Double profit = report.getDouble("Profit");
				System.out.printf("%-15s  %-15s  %-18s %-18s %-8s%n", customerType, orderMonth, totalPrice,
						totalCost, profit);

			}

			//DO NOT FORGET TO CLOSE YOUR CONNECTION
			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION	
	}
	
	
	
	public static String getCustomerName(int CustID) throws SQLException, IOException
	{
	/*
		 * This is a helper method to fetch and format the name of a customer
		 * based on a customer ID. This is an example of how to interact with 
		 * your database from Java.  It's used in the model solution for this project...so the code works!
		 * 
		 * OF COURSE....this code would only work in your application if the table & field names match!
		 *
		 */

		 connect_to_db();

		/* 
		 * an example query using a constructed string...
		 * remember, this style of query construction could be subject to sql injection attacks!
		 * 
		 */
		String cname1 = "";
		String query = "Select CustomerFirstName, CustomerLastName From customer WHERE CustomerID=" + CustID + ";";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		
		while(rset.next())
		{
			cname1 = rset.getString(1) + " " + rset.getString(2); 
		}

		/* 
		* an example of the same query using a prepared statement...
		* 
		*/
		String cname2 = "";
		PreparedStatement os;
		ResultSet rset2;
		String query2;
		query2 = "Select CustomerFirstName, CustomerLastName From customer WHERE CustomerID=?;";
		os = conn.prepareStatement(query2);
		os.setInt(1, CustID);
		rset2 = os.executeQuery();
		while(rset2.next())
		{
			cname2 = rset2.getString("CustomerFirstName") + " " + rset2.getString("CustomerLastName"); // note the use of field names in the getSting methods
		}

		conn.close();
		return cname1; // OR cname2
	}

	/*
	 * The next 3 private methods help get the individual components of a SQL datetime object. 
	 * You're welcome to keep them or remove them.
	 */
	private static int getYear(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(0,4));
	}
	private static int getMonth(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(5, 7));
	}
	private static int getDay(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(8, 10));
	}

	public static boolean checkDate(int year, int month, int day, String dateOfOrder)
	{
		if(getYear(dateOfOrder) > year)
			return true;
		else if(getYear(dateOfOrder) < year)
			return false;
		else
		{
			if(getMonth(dateOfOrder) > month)
				return true;
			else if(getMonth(dateOfOrder) < month)
				return false;
			else
			{
				if(getDay(dateOfOrder) >= day)
					return true;
				else
					return false;
			}
		}
	}


}