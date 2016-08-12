import java.sql.*;

import java.util.ArrayList;

import DatabaseModel.*;
import DatabaseModel.DatabaseInitializer.FieldNotInitializedException;
import DatabaseModel.Tables.*;
	
public class Program {
	
	public static DatabaseConnection connection;
	
	public static void main(String[] args) {
		System.out.println("===INITIALIZING====");
		try {
			Program.connection = new DatabaseConnection(true);
			
//			ArrayList<Product> test = connection.select(Product.class);
//			
//			connection.closeConnection();
//			
//			for (Product product : test) {
//				System.out.println(product);
//			}
			
			/*connection = new DatabaseConnection(true);
			try {
				connection.update(Product.class, new HashMap<Field, Object>() {{ 
					put(Product.class.getField("name"), "nope");
				}}, new HashMap<Field, Object>() {{ 
					put(Product.class.getField("id"), 1);
				}});
				
				
				for (Product product : connection.select(Product.class)) {
					System.out.println(product);
				}
				
				connection.closeConnection();
			}
			catch (Exception e) {
				System.out.println(e);	
			}*/
			
			/*connection = new DatabaseConnection(true);
			
			try {
				connection.delete(Product.class, new HashMap<Field, Object>() {{ 
					put(Product.class.getField("id"), 2);
				}});
				
				for (Product product : connection.select(Product.class)) {
					System.out.println(product);
				}
				
				connection.connection.rollback();// Do this so that TRANSACTIONS are disposed on the SQL server. Suspecting that non-commited and non-rolledback transactions stay in memory forever.
				connection.closeConnection();
			}
			catch (Exception e) {
				System.out.println(e);	
			}*/
			
//			connection = new DatabaseConnection(true);
//			try {
//				
//				Product p = new Product();
//				p.name = "Test";
//				p.price = 1d;
//				
//				connection.insert(p);
//				
//				ArrayList<Product> test = connection.select(Product.class);
//				for (Product product : test) {
//					System.out.println(product);
//				}
//			}
//			catch (Exception e) {
//				System.out.println(e);
//			}
			
//			ArrayList<Table> tables = new ArrayList<Table>();
//			tables.add(new Licence());
//			
//			DatabaseModel model = new DatabaseModel(tables);
//			
//			new Licence().Create(connection);
//			DatabaseInitializer.CleanupDatabase(
//				connection, 
//				model
//			);
			
			ArrayList<Table> tables = new ArrayList<Table>();
			tables.add(new Licence());
			tables.add(new Purchase());
			tables.add(new Product());
			tables.add(new Sale());
			tables.add(new User());
			
			ArrayList<Table> tableData = new ArrayList<Table>();
			Product p1 = new Product();
			p1.name = "Kopimaskine";
			p1.price = 100.5d;
			
			tableData.add(p1);
			
			DatabaseModel model = new DatabaseModel(tables, tableData);
			
			try {			
				if (DatabaseInitializer.IncompleteModel(connection, model)) {
					System.out.println("Model invalidated - Reconstructing database model...");
					DatabaseInitializer.CleanupDatabase(connection, model);
					try {
						new DatabaseInitializer(model).InitializeDatabase(connection);
					}
					catch (FieldNotInitializedException exception) {
						//Well we're stupid...
						connection.closeConnection();
						System.out.println(exception);
						return;
					}
					System.out.println("Database reconstruction complete...");
				}
			}
			catch (FieldNotInitializedException exception) {
				// Eh, look at your code?
				connection.closeConnection();
				return;
			}
			catch (SQLException exception) {
				System.out.println(exception);
				connection.closeConnection();
				return;
			}
			
			connection.closeConnection();
		}
		catch (SQLException e) {
			System.out.println(e);
		}
		
		System.out.println("===FINALIZED===");
	}
}
