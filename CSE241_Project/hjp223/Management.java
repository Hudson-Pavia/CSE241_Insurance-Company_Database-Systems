import java.sql.*;
import java.util.*;
public class Management {
    Management(){
        ;
    }
    public String currentRevenue(){                         //returns the sum of the amounts in the payments table (revenue collected)
        return "SELECT Sum(amount) as \"Current Revenue (payments received):\" from payments";
    }

    public String annualExpectedRevenue(){                  //returns the sum of all the policies purchased (expected annual revenue)
        return "SELECT SUM(PRICE) as \"Expected Annual Revenue:\" FROM PURCHASED FULL NATURAL JOIN POLICY";
    }

    public String claimsPaid(){     //returns all claims that have been paid
        return "SELECT paid_amount as \"Claims Paid Out:\", date_compensated as \"Date paid\" from claim_paid";
    }
    
    public PreparedStatement policyProfit(String policy, Connection conn){ //returns the profit of a specific category of policies
        PreparedStatement pstmt = null;
        try{
            switch (policy){
                case "car":
                    pstmt = conn.prepareStatement("select sum(price) as \"Expected Annual Profit:\" from purchased full natural join policy where policy_id = ? or policy_id = ? or policy_id = ? or policy_id = ?");
                    pstmt.setInt(1, 3);
                    pstmt.setInt(2, 5);
                    pstmt.setInt(3, 4);
                    pstmt.setInt(4, 7);
                    break;
                case "home":
                    pstmt = conn.prepareStatement("select sum(price) as \"Expected Annual Profit:\", from purchased full natural join policy where policy_id = ? or policy_id = ?");
                    pstmt.setInt(1, 1);
                    pstmt.setInt(2, 2);
                    break;
                case "health":
                    pstmt = conn.prepareStatement("select sum(price) as \"Expected Annual Profit:\", from purchased full natural join policy where policy_id = ?");
                    pstmt.setInt(1, 6);
                    break;
            }
        }
        catch (SQLException se){
            System.out.println("[Error]: policy profit query failed. Check your input and try again.");
            return null;
        }

        return pstmt;
    }

    public void customerProfits(Scanner scan, Connection conn, ResultSet rset, Statement stmt2){    //returns the expected profit of one or all customers
        PreparedStatement pstmt = null;
        boolean flag;                       //set to true for one customer, false for all customers
        String answer;                      // one or all (one customer or all customers)
        String id = "";                             //hold the id of interest
        int counter = 0;                        //counter for index of ids array that is declared below
        int rows;                        //hold the number of columns in the table. This is the size of the ids array
        System.out.print("Would you like to see the profitability of one customer or all customers?\nFor one customer enter \"one\", for all customers enter \"all\": ");
        while (true){
            answer = scan.nextLine();
            if (answer.equals("one")){
                flag = true;
                break;                    
            }
            else if (answer.equals("all")){
                flag = false;
                break;
            }
            else{
                System.out.print("[Error]: Invalid input. Enter \"one\" or \"all\": ");
            }
        }
        try{
            if (flag){              //we want only one customer's information
                rset = stmt2.executeQuery("select distinct customer.id, customer.name from customer, purchased where customer.id = purchased.id order by customer.id asc");
                rset.last();                        //cursor goes to the last row so we can see how many rows there are
                rows = rset.getRow();               //set rows to the index of the last row (this is how many ids we should expect)
                String ids[] = new String[rows];    //initialize new array with size of ids
                rset.first();                       //move the cursor back to the top of the table
                System.out.println();
                System.out.println("Below is the list of customers who have purchased a policy with the company\n");
                while(rset.next()){
                    ids[counter] = rset.getString("ID");                      //we need to store each id in an array for later checking
                    counter++;                                                //increment index of array
                    System.out.print("Customer ID: " + rset.getString("ID") + ", ");
                    System.out.print("Name: " + rset.getString("name"));
                    System.out.println();
                }
               /* for (int j = 0; j < ids.length; j++){                        //test loop to see what ids are fetched
                    System.out.printf("ids[%d] = %s\n", j, ids[j]);
                }   */
                System.out.print("Enter the the customer ID of interest: ");
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
                boolean flag3 = true;
                while(flag3){    
                    String revenue = null;
                    double revenue_dub = 0;                                    //to hold the expected revenue from particular customer
                    double expenses_dub = 0;                                   //hold expenses (claims paid out out to this customer)
                    pstmt = conn.prepareStatement("select sum(policy.price) from purchased, policy where purchased.policy_id = policy.policy_id and purchased.id = ?");
                    pstmt.setString(1, id);
                    rset = pstmt.executeQuery();
                    while (rset.next()) { revenue = rset.getString(1); }    //apparently even if we are expecting one value to be returned, we have to check rset.next()
                    if (rset.wasNull()){
                        revenue = null;
                    }
                    if (revenue != null){
                        revenue_dub = Double.parseDouble(revenue);
                    }
                    else{
                        System.out.println("This customer has not purchased any policies.");
                        flag3 = false;
                        break;
                    }
                    String expenses = null;
                    pstmt = conn.prepareStatement("select sum(claim_paid.paid_amount) from claim_paid, makes where makes.id = ? and makes.claim_id = claim_paid.claim_id");
                    pstmt.setString(1, id);
                    rset = pstmt.executeQuery();
                    while (rset.next()){ expenses = rset.getString(1); }
                    if (rset.wasNull()){
                        expenses = null;
                    }
                    if (flag3 && expenses != null){
                        expenses_dub = Double.parseDouble(expenses);
                    }
                    if (flag3 && expenses == null){
                        System.out.printf("Expected Profit From Selected Customer: %.2f\n", revenue_dub);
                        flag3 = false;
                        break;
                    }
                    if (flag3){
                        double profit = revenue_dub - expenses_dub;
                        System.out.printf("Expected Profit: %.2f\n", profit);
                        flag3 = false;
                        break;
                    }
                    flag3 = false;
                }              

            }
            else{
                String revenue = "";
                double revenue_dub = 0;
                String expenses = "";
                double expenses_dub = 0;
                rset = stmt2.executeQuery("SELECT SUM(PRICE) as \"Expected Annual Revenue:\" FROM PURCHASED FULL NATURAL JOIN POLICY");
                while (rset.next()){ revenue = rset.getString(1); }
                if (rset.wasNull()){
                    revenue = null;
                }
                if(revenue != null){ revenue_dub = Double.parseDouble(revenue); }
                rset = stmt2.executeQuery("SELECT SUM(PAID_AMOUNT) FROM CLAIM_PAID");
                while (rset.next()){ expenses = rset.getString(1); }
                if (rset.wasNull()){
                    expenses = null;
                }
                if(expenses != null){ expenses_dub = Double.parseDouble(expenses); }

                System.out.printf("Expected Profit For All Customers: %.2f\n", (revenue_dub-expenses_dub));

            }

        }
        catch (SQLException se){
            System.out.println("[Error]: Preparing customer profitability report failed. Try Again.");
            se.printStackTrace();
            return;
        }
        return;
    }

}
