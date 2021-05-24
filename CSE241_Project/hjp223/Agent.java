import java.util.*;


import java.sql.*;
public class Agent {
    Agent(){
        ;
    }

    public boolean assignments(String agentID, Connection conn, ResultSet rset){ // return assigned accounts
        PreparedStatement pstmt = null;
        String customerID = "";
        String customerName = "";
        
        try{
            pstmt = conn.prepareStatement("select customer.id, customer.name from assigned, customer where assigned.agent_id = ? and assigned.id = customer.id");
            pstmt.setString(1, agentID);
            rset = pstmt.executeQuery();
            System.out.println("Below are the customer's accounts you are responsible for:");
            while(rset.next()){
                customerID = rset.getString("id");
                customerName = rset.getString("name");
                System.out.printf("Customer id: %S\nCustomer name: %s\n\n", customerID, customerName);
            }
            if (rset.wasNull()){
                System.out.println("error");
                return false;
            }
        }
        catch (SQLException se){
            //se.printStackTrace();
            System.out.println("You currently have no assigned customers.");
            return false;
        }
        return true;
    }
    
    public void unservicedClaims(boolean flag,String agentID, Connection conn, ResultSet rset){
        String customerID = "";
        PreparedStatement pstmt = null;
        int counter = 0;
        String[] customers = new String[100];
        if(!flag){
            System.out.println("You aren't responsible for any customers right now!");
        }
        else{
        try{
            pstmt = conn.prepareStatement("select customer.id from assigned, customer where assigned.agent_id = ? and assigned.id = customer.id");
            pstmt.setString(1, agentID);
            rset = pstmt.executeQuery();
            while(rset.next()){
                customers[counter] = rset.getString("id");
                counter++;
            }
            

            for (int i = 0; i < counter; i++){
                pstmt = conn.prepareStatement("select claim.claim_id, claim.descript, claim.claim_date from claim, makes where makes.claim_id = claim.claim_id and makes.id = ?");
                pstmt.setString(1, customers[i]);
                rset = pstmt.executeQuery();
                try{
                    while (rset.next()){
                        System.out.printf("Customer id: %s\nClaim id: %s\nClaim description: %s\nClaim date: %s\n\n",customers[i], rset.getString("claim_id"), rset.getString("descript"), rset.getString("claim_date"));
                        if(rset.wasNull()){
                            System.out.printf("Customer with id %s has not made any claims.\n", customers[i]);
                        }
                    }
                } catch (SQLException se){
                    System.out.printf("Customer with id %s has not made any claims.\n", customers[i]);
                }
            }
        }
        catch (SQLException se){
            se.printStackTrace();;
            System.out.println("[Error]: failed to fetch claims report.");
            return;
        }
        }
    }
}


