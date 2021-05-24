import java.util.*;
import java.sql.*;
public class Customer {
    Customer(){
        ;
    }
    public void viewPolicies(String customerID, Scanner scan, Connection conn, ResultSet rset){
        PreparedStatement pstmt = null;
        String policy_id = "";
        String policy_type = "";
        boolean flag = false;                        //flag to check if rset is null
        try{
            pstmt = conn.prepareStatement("select policy_id from purchased, customer where customer.id = ? and purchased.id = customer.id");
            pstmt.setString(1, customerID);
            rset = pstmt.executeQuery();
            while (rset.next()){
                policy_id = rset.getString(1);
                if (rset.wasNull()){
                    flag = false;
                }
                else{
                    flag = true;
                }     
                if (flag){  
                    switch (policy_id){
                        case "3": case "5": case "4": case "7":
                            policy_type = "Car Insurance";
                            break;
                        case "6":
                            policy_type = "Home Insurance";
                            break;
                        case "1": case "2":
                            policy_type = "Health Insurance";
                            break;
                }
                System.out.printf("Policy Number: %s\nPolicy Type: %s\n\n", policy_id, policy_type);

                }
            }

        } catch(SQLException se){
            se.printStackTrace();
            System.out.println("[Error]: Policies fetch failed, please try again.");
        }
        if (!flag){ System.out.println("You haven't purchased a policy with us yet!");}
    }

    public String newCustomer(Scanner scan, Connection conn, ResultSet rset){
        String customer_id = "";
        PreparedStatement pstmt = null;
        String name = "";
        int age = 0;
        boolean flag1 = true, flag2 = true, flag3 = true, flag4 = true;             //need multiple while loops to ensure that we can check and get proper input

        while (flag1){
            System.out.print("Enter your first and last name: ");
            while(flag2){
                name = scan.nextLine();
                System.out.println();
                if (name.indexOf("'") != -1 || name.indexOf("\"") != -1 || name.indexOf("*") != -1 || name.indexOf(";") != -1 || name == null){
                    System.out.print("[Error]: Invalid name detected. Please check your input and try again: ");
                }
                else if(name.length() < 2){
                    System.out.print("[Error]: Invalid name detected. Please make sure your name is at least two letters long and try again: ");
                }
                else if (name.length() > 40){
                    System.out.print("[Error]: Name entered is too long. Please try again: ");
                }
                else{
                    flag2 = false;
                }
            }//we now have a valid name

            System.out.print("Enter your age: ");
            while (flag3){
                if (scan.hasNextInt()){
                    age = scan.nextInt();
                    if (age >= 18 && age < 200){
                        flag3 = false;
                    }
                    else{
                        System.out.print("[Error]: You must be 18 or older to start an account. Please check your input and try again: ");
                    }
                }
                else{
                    scan.nextLine();
                    System.out.print("[Error]: Invalid age detected. Please check your input ands try again: ");
                }
            } //we now have a valid age
            scan.nextLine();
            System.out.print("Enter your desired customer id (8 characters or less): ");
            while(flag4){
                customer_id = scan.nextLine();
                if (customer_id.indexOf("'") != -1 || customer_id.indexOf("\"") != -1 ||  customer_id.indexOf("/") != -1 || customer_id.indexOf("*") != -1 || customer_id.indexOf(";") != -1 || customer_id == null){
                    System.out.print("[Error]: Invalid customer id detected. Please check your input and try again: ");
                }
                else if(customer_id.length() > 8){
                    System.out.print("[Error]: Inputted customer id is too long, please check your input and try again: ");
                }
                else{
                    try{
                        pstmt = conn.prepareStatement("insert into customer (id, name, age) values (?, ?, ?)");
                        pstmt.setString(1, customer_id);
                        pstmt.setString(2, name);
                        pstmt.setInt(3, age);
                        pstmt.executeQuery();
                        System.out.println("New Customer Account has been added successfully!");
                        flag4 = false;
                        return customer_id;
                    } catch(SQLException se){
                        //se.printStackTrace();
                        System.out.print("[Error]: Customer id entered is already taken. Please come up with a new id and try again: ");
                    }
                }
            }
            flag1 = false;
        }
        return null;

    }

    public void addODropPolicy(String customerID, Scanner scan, Connection conn, ResultSet rset){
        String dora = "";
        PreparedStatement pstmt = null;
        String policy_id = "";
        String policy_type = "";
        String policy_type_raw = "";
        boolean flag1 = true, flag2 = true, flag3 = true, flag4 = true;
        System.out.println();
        System.out.print("Would you like to drop or add a policy? Please enter \"drop\" or \"add\": ");
        while(true){
            dora = scan.nextLine();
            if (dora.equalsIgnoreCase("drop") || dora.equalsIgnoreCase("add")){
                break;
            }
            else{
                System.out.println();
                System.out.print("[Error]: Invalid option. Please try again: ");
            }
        } //we know have drop or add
        if(dora.equalsIgnoreCase("add")){
            System.out.print("What type of policy are you looking to purchase? Enter \"car\" or \"home\" or \"health\": ");
            while(flag1){
                policy_type = scan.nextLine();
                if (policy_type.equalsIgnoreCase("car") || policy_type.equalsIgnoreCase("home") || policy_type.equalsIgnoreCase("health")){
                    flag1 = false;
                }
                else{
                    System.out.println();
                    System.out.print("[Error]: Invalid option selected. Try again: ");
                }
            }

            switch (policy_type){
                case "car":
                System.out.println("Below are the different options for car policies:");
                System.out.print("1: $48740.97\n2: $42849.90\n3: $9069.94\n4: 23689.15\n");
                while(flag3){
                System.out.print("Please enter the number associated with the policy you would like to purchase: ");
                while(flag2){
                    policy_id = scan.nextLine();
                    if(policy_id.equals("1") || policy_id.equals("2") || policy_id.equals("3") || policy_id.equals("4")){
                        flag2 = false;
                    }
                    else{
                        System.out.println();
                        System.out.print("[Error]: Invalid option selected. Try again: ");
                    }
                } //we now have a valid policy_id
                switch (policy_id){
                    case "1":
                        policy_type_raw = "3";
                        break;
                    case "2":
                        policy_type_raw = "4";
                        break;
                    case "3":
                        policy_type_raw = "5";
                        break;
                    case "4":
                        policy_type_raw = "7";
                        break;
                } //we now have the actual policy_id as listed in the policy table
                try{
                    pstmt = conn.prepareStatement("insert into purchased (ID, policy_id) values (?, ?)");
                    pstmt.setString(1, customerID);
                    pstmt.setString(2, policy_type_raw);
                    pstmt.executeUpdate();
                    System.out.println("Policy Purchased successfuly!");
                    flag3 = false;
                } catch (SQLException se){
                    //se.printStackTrace();
                    System.out.print("[Error]: You already own this policy! Enter a different policy you would like to purchase: ");
                    flag2 = true;
                }
                }
                break;

                case "home":
                System.out.println("Below are the different options for home policies:");
                System.out.print("1: $36743.66\n2: $94621.14\n");
                while(flag3){
                System.out.print("Please enter the number associated with the policy you would like to purchase: ");
                while(flag2){
                    policy_id = scan.nextLine();
                    if(policy_id.equals("1") || policy_id.equals("2")){
                        flag2 = false;
                    }
                    else{
                        System.out.println();
                        System.out.print("[Error]: Invalid option selected. Try again: ");
                    }
                } //we now have a valid policy_id
                switch (policy_id){
                    case "1":
                        policy_type_raw = "1";
                        break;
                    case "2":
                        policy_type_raw = "2";
                        break;
                } //we now have the actual policy_id as listed in the policy table
                try{
                    pstmt = conn.prepareStatement("insert into purchased (ID, policy_id) values (?, ?)");
                    pstmt.setString(1, customerID);
                    pstmt.setString(2, policy_type_raw);
                    pstmt.executeUpdate();
                    System.out.println("Policy Purchased successfuly!");
                    flag3 = false;
                } catch (SQLException se){
                    //se.printStackTrace();
                    System.out.print("[Error]: You already own this policy! Enter a different policy you would like to purchase: ");
                    flag2 = true;
                }
                }
                break;
                
                case "health":
                System.out.println("Below are the different options for health policies:");
                System.out.print("1: $55394.0\n");
                while(flag3){
                System.out.print("Please enter the number associated with the policy you would like to purchase: ");
                while(flag2){
                    policy_id = scan.nextLine();
                    if(policy_id.equals("1")){
                        flag2 = false;
                    }
                    else{
                        System.out.println();
                        System.out.print("[Error]: Invalid option selected. Try again: ");
                    }
                } //we now have a valid policy_id
                switch (policy_id){
                    case "1":
                        policy_type_raw = "6";
                        break;
                } //we now have the actual policy_id as listed in the policy table
                try{
                    pstmt = conn.prepareStatement("insert into purchased (ID, policy_id) values (?, ?)");
                    pstmt.setString(1, customerID);
                    pstmt.setString(2, policy_type_raw);
                    pstmt.executeUpdate();
                    System.out.println("Policy Purchased successfuly!");
                    flag3 = false;
                } catch (SQLException se){
                    se.printStackTrace();
                    System.out.print("[Error]: You already own the only health policy we offer!");
                    flag3 = false;
                }
                }
                break;
            }
        }
        if (dora.equalsIgnoreCase("drop")){
            String[] policies = new String[7];
            String drop = "";
            int counter = 0;
            try{
                System.out.println("Below are the policies you currently own:");
                pstmt = conn.prepareStatement("select distinct policy.policy_id, policy.price from policy, purchased, customer where purchased.policy_id = policy.policy_id and purchased.id = ?");
                pstmt.setString(1, customerID);
                rset = pstmt.executeQuery();
                while (rset.next()){
                    policies[counter] = rset.getString("policy_id");
                    counter++;
                    System.out.printf("Policy: %s\nCost: %.2f\n\n", rset.getString("Policy_id"), rset.getDouble("price"));
                }
                System.out.println();
                if (rset.wasNull()){
                    System.out.println("[Error]: You do not own any policies.");
                    return;
                }
                System.out.print("Enter the policy number that you wish to drop: ");
                while(flag4){
                    drop = scan.nextLine();
                    for (int i = 0; i < policies.length; i++){
                        if (drop.equals(policies[i])){
                            flag4 = false;
                        }
                    }
                    if (flag4){
                        System.out.println();
                        System.out.print("[Error]: Invalid option. Try again: ");
                    }
                } //we now know the policy we want to drop
                pstmt = conn.prepareStatement("delete from purchased where id = ? and policy_id = ?");
                pstmt.setString(1, customerID);
                pstmt.setString(2, drop);
                pstmt.executeUpdate();
                System.out.println("Policy successfully dropped.");
                return;


            } catch (SQLException se){
                //se.printStackTrace();
                System.out.println("[Error]: You do not own any policies.");
                return;
            }


        }

    }

    public void payBill(String customerID, Scanner scan, Connection conn, ResultSet rset, Statement stmt2){
        String ppolicy = "";
        int payment = 0;
        boolean flag4 = true, flag5 = true;
        String[] policies = new String[7];
        String drop = "";
        int counter = 0;
        String lastUsedID = "";
        int newID = 0;
        String newIDS = "";
        PreparedStatement pstmt;
            try{
                System.out.println("Below are the policies you own:");
                pstmt = conn.prepareStatement("select distinct policy.policy_id, policy.price from policy, purchased, customer where purchased.policy_id = policy.policy_id and purchased.id = ?");
                pstmt.setString(1, customerID);
                rset = pstmt.executeQuery();
                while (rset.next()){
                    policies[counter] = rset.getString("policy_id");
                    counter++;
                    System.out.printf("Policy: %s\nCost: %.2f\n\n", rset.getString("Policy_id"), rset.getDouble("price"));
                }
                System.out.println();
                if (rset.wasNull()){
                    System.out.println("[Error]: You do not own any policies to make a payment for.");
                    return;
                }
                System.out.print("Enter the policy number that you wish to pay off: ");
                while(flag4){
                    ppolicy = scan.nextLine();
                    for (int i = 0; i < policies.length; i++){
                        if (ppolicy.equals(policies[i])){
                            flag4 = false;
                        }
                    }
                    if (flag4){
                        System.out.println();
                        System.out.print("[Error]: Invalid option. Try again: ");
                    }
                } //we now know the policy we want to pay
                
                System.out.print("Enter the amount you want to pay: $");
                while(flag5){
                    if (scan.hasNextInt()){
                        payment = scan.nextInt();
                        if (payment > 0 && payment < 1000000){
                            System.out.println();
                            flag5 = false;
                        }
                        else{
                            System.out.println();
                            System.out.print("[Error]: Payments must be at least one dollar. Please try again: $");
                        }
                    }
                    else{
                        scan.nextLine();
                        System.out.print("[Error]: Invalid amount entered. Check your input and try again: $");
                    }
                } //we now have the amount to be paid and the policy it belongs to
                //Need to generate a new, unique payment_id
                rset = stmt2.executeQuery("select payment_id from payments");
                rset.last();
                lastUsedID = rset.getString("payment_id");
                newID = Integer.parseInt(lastUsedID) + 1;
                newIDS += newID;
                //update payments table
                System.out.printf("%s|%d", newIDS, payment);
                pstmt = conn.prepareStatement("insert into payments (payment_id, date_payed, amount) values (?, (select to_char (sysdate, 'DD-Mon-yy') from dual), ?)");
                pstmt.setString(1, newIDS);
                pstmt.setInt(2, payment);
                pstmt.executeUpdate();
                System.out.println();
                //update completes table
                pstmt = conn.prepareStatement("insert into completes (payment_id, id) values (?, ?)");
                pstmt.setString(1, newIDS);
                pstmt.setString(2, customerID);
                pstmt.executeUpdate();
                //lastly, update pays_off table
                pstmt = conn.prepareStatement("insert into pays_off (policy_id, payment_id) values (?, ?)");
                pstmt.setString(1, ppolicy);
                pstmt.setString(2, newIDS);
                pstmt.executeUpdate();
                System.out.println("Payment competed successfully!");
                return;
            } catch (SQLException se){
                se.printStackTrace();
                System.out.println("[Error]: Payment service failed. Try again");
                return;
            }

    }

    public void makeClaim(String customerID, Scanner scan, Connection conn, ResultSet rset, Statement stmt2){
        PreparedStatement pstmt;
        String oldClaimID = "";
        int newID = 0;
        String newIDS = "";
        String description = "";
        boolean flag1 = true, flag2 = true;
        System.out.println("Please give a brief description of the circumstances behind your claim (what happened):");
        while(flag1){
            description = scan.nextLine();
            if (description.indexOf("'") != -1 || description.indexOf("\"") != -1 || description.indexOf("*") != -1 || description.indexOf(";") != -1 || description == null){
                System.out.println("[Error]: Invalid symbols detected. Please try again:");
            }
            else if (description.length() > 250){
                System.out.println("[Error]: Description provided is too long. Please try again:");
            }
            else{
                flag1 = false;
            }
        }//we now have a description
        //create new unique claim_id
        try{
            rset = stmt2.executeQuery("select claim_id from claim");
            rset.last();
            oldClaimID = rset.getString("claim_id");
            newID = Integer.parseInt(oldClaimID) + 1;
            newIDS += newID;
            //update claim table
            pstmt = conn.prepareStatement("insert into claim (claim_id, descript, claim_date) values (?, ?, (select to_char (sysdate, 'DD-Mon-yy') from dual))");
            pstmt.setString(1, newIDS);
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            //update makes table
            pstmt = conn.prepareStatement("insert into makes (id, claim_id) values (?, ?)");
            pstmt.setString(1, customerID);
            pstmt.setString(2, newIDS);
            pstmt.executeUpdate();
            System.out.println("Claim submitted successfully.");
            return;

        } catch(SQLException se){
            se.printStackTrace();
            System.out.println("[Error]: Preparing Claim failed. Please Try again.");
            return;
        }

    }

    public String modifyPossessions(){
        return "";
    }
}
