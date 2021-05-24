import java.sql.*;
import java.util.*;
public class App {
     static final String DB_URL = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";

    public static void main(String[] args) throws SQLException{
       Connection conn = null;
       Scanner scan = new Scanner(System.in);
       do {
           try{
                // Collect login credentials
                System.out.print("Enter Oracle user id: ");
                String user_id = scan.next();
                System.out.print("Enter Oracle user password: ");
                String user_pass = scan.next();
                scan.nextLine();
                // connect to DB
                conn = DriverManager.getConnection(DB_URL, user_id, user_pass);
           }
           catch (SQLException se) {
                //se.printStackTrace();
                System.out.println("[Error]: Connection failed. Re-enter login information:");
            }
       } while (conn == null);
       System.out.println("Connection established...\n");
       Statement stmt = conn.createStatement();
       Statement stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
       ResultSet rset = null;
       PreparedStatement pstmt;
       String loop = "";
       String exit_key = "exit";
       int selection = 0;
       String garbage = "";
       boolean flag = true;
       String interfaces = "";
       String customerID = "";
       //have user select which interface they want
       System.out.print("Which interface are you looking to access? Enter \"Corporate\", \"Customer\" or \"Agent\": ");
       while(true){
            interfaces = scan.nextLine();
            if (interfaces.equalsIgnoreCase("Corporate") || interfaces.equalsIgnoreCase("Customer") || interfaces.equalsIgnoreCase("agent")){
                 break;
            }
            else{
                 System.out.print("[Error]: Invalid option selected. Try again: ");
            }
       }

       //interface controllers below

       if (interfaces.equalsIgnoreCase("Customer")){
          Customer cust = new Customer();
          System.out.println("Welcome to the Customer portal!");
          String login = "";
          System.out.print("Would you like to sign in or create a new account? Enter \"sign in\" or \"new account\": ");
          while(true){
               login = scan.nextLine();
               if (login.equalsIgnoreCase("sign in") || login.equalsIgnoreCase("new account")){
                    break;
               }
               else{
                    System.out.println("[Error]: Invalid option selected. Try again: ");
               }
          }
          
          if (login.equalsIgnoreCase("sign in")){
          String id = "";                             //hold the id of interest
          int counter = 0;                        //counter for index of ids array that is declared below
          int rows;          
           rset = stmt2.executeQuery("select customer.id from customer");
               rset.last();                        //cursor goes to the last row so we can see how many rows there are
               rows = rset.getRow();               //set rows to the index of the last row (this is how many ids we should expect)
               String ids[] = new String[rows];    //initialize new array with size of ids
               rset.first();                       //move the cursor back to the top of the table
               while(rset.next()){
                    ids[counter] = rset.getString("ID");                      //we need to store each id in an array for later checking
                    counter++;                                                //increment index of array
                }
                System.out.print("Enter your id number: ");
                boolean flag2 = true;
                while(flag2){
                    id = scan.nextLine();
                    for (int i = 0; i < ids.length; i++){
                        if (id.equals(ids[i])){
                            System.out.println("Valid ID entered");
                            flag2 = false;
                            break;
                        }
                    }
                    if(flag2){
                        System.out.print("[Error]: ID entered does not match any records. PLease try again: ");
                    }
                } //we now know what id we want to query.
                customerID = id;
          }
          if (login.equalsIgnoreCase("new account")){
               customerID = cust.newCustomer(scan, conn, rset);
          }

          while (!loop.equalsIgnoreCase(exit_key)){
               System.out.println();
               System.out.print("[1]: View Your Policies\n");
               System.out.print("[2]: Add or Drop a Policy\n");
               System.out.print("[3]: Make a Payment\n");
               System.out.print("[4]: Make a Claim\n");
               System.out.print("[5]: Exit\n");
               System.out.print("Enter the number of the report of interest: ");
            
               while(flag){
                    if (scan.hasNextInt()){
                         selection = scan.nextInt();
                         if (selection == 1 || selection == 2 || selection == 3 || selection == 4 || selection == 5){
                              flag = false;
                         }
                         else{
                              System.out.print("\n[Error]: Invalid option. Try again: ");
                         }  
                    }
                    else{
                         garbage = scan.nextLine();
                         System.out.print("\n[Error]: Invalid input. Try again: ");
                    }
               }
               flag = true;
               switch(selection){
                    case 1:
                         cust.viewPolicies(customerID, scan, conn, rset);
                         break;
                    case 2:
                         cust.addODropPolicy(customerID, scan, conn, rset);
                         break;
                    case 3:
                         cust.payBill(customerID, scan, conn, rset, stmt2);
                         break;
                    case 4:
                         cust.makeClaim(customerID, scan, conn, rset, stmt2);
                         break;
                    case 5:
                         System.out.println("Exiting");
                         loop = "exit";
                         break;
               }
          }
     }
       
       


     if (interfaces.equalsIgnoreCase("Corporate")){
          System.out.println("Welcome to the Corporate Management interface.");
          Management corp = new Management();
          while (!loop.equalsIgnoreCase(exit_key)){
               System.out.println();
               System.out.print("[1]: View Current Revenue Collected\n");
               System.out.print("[2]: View Expected Annual Revenue\n");
               System.out.print("[3]: View All Claims Paid Out\n");
               System.out.print("[4]: View Profit of a Policy\n");
               System.out.print("[5]: View Customer Profit Data\n");
               System.out.print("[6]: Exit\n");
               System.out.print("Enter the number of the report of interest: ");
            
               while(flag){
                    if (scan.hasNextInt()){
                         selection = scan.nextInt();
                         if (selection == 1 || selection == 2 || selection == 3 || selection == 4 || selection == 5 || selection == 6){
                              flag = false;
                         }
                         else{
                              System.out.print("\n[Error]: Invalid option. Try again: ");
                         }  
                    }
                    else{
                         garbage = scan.nextLine();
                         System.out.print("\n[Error]: Invalid input. Try again: ");
                    }
               }
               flag = true;
               switch(selection){
                    case 1:
                         pstmt = conn.prepareStatement(corp.currentRevenue());  
                         rset = pstmt.executeQuery();
                         printQuery(rset);
                         break;
                    case 2:
                         //expected revenue
                         pstmt = conn.prepareStatement(corp.annualExpectedRevenue());
                         rset = pstmt.executeQuery();
                         printQuery(rset);
                         break;
                    case 3:
                         pstmt = conn.prepareStatement(corp.claimsPaid());
                         rset = pstmt.executeQuery();
                         printQuery(rset);
                         break;
                    case 4:
                         pstmt = corp.policyProfit("car", conn);
                         rset = pstmt.executeQuery();
                         printQuery(rset);
                         break;
                    case 5:
                         corp.customerProfits(scan, conn, rset,stmt2);
                         break;
                    case 6:
                         System.out.println("Exiting");
                         loop = "exit";
                         break;
               }
          }
     }
     loop = "";    //reset loop for next interface 
     if (interfaces.equalsIgnoreCase("agent")){
          System.out.println("Welcome to the Agent interface!");
          Agent agent = new Agent();
          boolean hasCustomers = false;
          String agentID = "";
          String id = "";                             //hold the id of interest
          int counter = 0;                        //counter for index of ids array that is declared below   
          String[] ids = new String[100];       
           rset = stmt2.executeQuery("select agent_id from agent");
               while(rset.next()){
                    ids[counter] = rset.getString("agent_id");                      //we need to store each id in an array for later checking
                    counter++;                                                //increment index of array
                }
                System.out.print("Enter your agent id number: ");
                boolean flag2 = true;
                while(flag2){
                    id = scan.nextLine();
                    for (int i = 0; i < counter; i++){
                        if (id.equals(ids[i])){
                            System.out.println("Valid ID entered");
                            flag2 = false;
                            break;
                        }
                    }
                    if(flag2){
                        System.out.print("[Error]: ID entered does not match any records. PLease try again: ");
                    }
                } //we now know what id we want to query.
                agentID = id;
                //signed in now//
                 while (!loop.equalsIgnoreCase(exit_key)){
               System.out.println();
               System.out.print("[1]: View Assigned Customers\n");
               System.out.print("[2]: View Claims Made by Assigned Customers\n");
               System.out.print("[3]: Exit\n");
               while(flag){
                    if (scan.hasNextInt()){
                         selection = scan.nextInt();
                         if (selection == 1 || selection == 2 || selection == 3){
                              flag = false;
                         }
                         else{
                              System.out.print("\n[Error]: Invalid option. Try again: ");
                         }  
                    }
                    else{
                         garbage = scan.nextLine();
                         System.out.print("\n[Error]: Invalid input. Try again: ");
                    }
               }
               flag = true;
               switch(selection){
                    case 1:
                         hasCustomers = agent.assignments(agentID, conn, rset);
                         break;
                    case 2:
                         agent.unservicedClaims(hasCustomers,agentID, conn, rset);
                         break;
                    case 3:
                         System.out.println("Exiting");
                         loop = "exit";
                         break;
               }
          }
     }

       
       //Closing everything 
       try {
          conn.close();
       } catch (SQLException e) {
          e.printStackTrace();
          System.out.println("[Error]: Attempt to close connection failed.");
      }
      scan.close();
      stmt.close();
      stmt2.close();
      if (rset != null) { rset.close(); }
      return;
    }


    public static void printQuery(ResultSet rset){
     try{
          ResultSetMetaData rsmd = rset.getMetaData();
          int colmns = rsmd.getColumnCount();
          String col_value;
          while(rset.next()){
               for (int i = 1; i <= colmns; i++){
                    col_value = rset.getString(i);
                    System.out.println(rsmd.getColumnName(i) + " " + col_value);
               }
               System.out.println("");   
               }
     }
     catch (SQLException se){
          System.out.println("[Error]: printing results failed");
     }
     }
}
