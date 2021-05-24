# CSE241_Insurance-Company_Database-Systems
----------------------------------

PROJECT TITLE: CSE241 Final Project
PURPOSE OF PROJECT: Demonstrate my grit
VERSION or DATE:    V1    
HOW TO START THIS PROJECT:  cd into the directory titled hjp223. run java -jar hjp223.jar
AUTHORS:    Hudson Pavia
USER INSTRUCTIONS: Instructions vary based on interface. Start with the Corporate Interface. The exit option in each interface terminates the program.
 You will have to run the jar 3 times to test all the interfaces. I'm sorry for this inconvenience, I found this to be the best way for me to manage the interfaces. The original data generation is in the directory titled dataGeneration.

Corporate Interface:    Do this one first
    This interface is the more straightforward one out of the bunch. Many of its features don't require the user to input any data.
    - When you first run the jar file, you will be asked which interface you want to access and you should type out corporate.

    - Once you are inside this interface you will see a welcome message followed by a numbered list of options available to you
      *note the last option titled "exit" will terminate the program*

    - Options 1, 2, and 3 which are "View Current Revenue Collected", "View Expected Annual Revenue", and "View All Claims Paid Out" respectively,
      require no participation by the user outside of selecting their option number.

    - Option 4, titled "View Profit of a Policy" asks the user to enter car, home, or health. Feel free to choose any of these options and the number
      you get back will be the expected annual profit for that particular policy type. Since the interface is always looping you can choose option 4 as many
      times as you'd like to see the different outputs for the different policy types (car, home, health).

    -Option 5, titled "View Customer Profit Data" is the most involved feature in this interface.
     First, you will be asked if you want to see all the customer data combined or the data for one individual customer.
     I suggest that you select the option for one customer as this better shows off the capabilities of this feature.
     After you select the option for one customer, the program will show you a list of the all the customers who have PURCHASED at least one policy.
     I decided to omit customers with no purchases from this list because I felt that they would be pointless (no policies means no valid claims: no $ involved)
     From this list, you will be asked to enter the id number of the customer of interest (id numbers are listed next to the names).
     Select any id on the list and the program will output the profit for that customer 
     (sum of policy revenue for that customer minus claims paid to that customer).
     If you select id 3, you can see that the amount returned is negative. This is because the claim paid to them was more money than the cost of his policies.

     -When you are finished, select the option exit. For simplicity, this will terminate the program. 
      You will have to run the executable again to access the other interfaces

    -Come back to this interface after playing with the customer interface if you want to see how adding/dropping policies affects the numbers

Customer Interface:
    -Upon entering this interface you will be given the option to sign in (if you are an existing customer) or make a new account

    -For your first run through, choose the 'sign in' option and enter 2 as the customer id. This will sign you in and take you to the real interface

    -Once signed in, all the options will be catered toward that specific customer. 
     Just like in the corporate interface, you will presented with numbered features for you to choose from.

     -Option 1 is titled "View Your Policies". Select this option first, and it will print this customer's purchased policy numbers along with their type.

     -Option 2 is titled "Add or Drop a policy" When you select this option you will be asked if you want to add or drop a policy.
     For your first run through this feature, choose add. The program will then prompt you to choose which type of policy you're looking to buy.
     Customer 2 already has two car policies and a home policy so select the option 'health'.
     Next, you will be presented with the different options and prices for health policies. I only populated one health policy so select that one.
     The program will check to make sure you don't already own this policy and will print a message upon successfully purchase.

     For your second run through this feature select drop. The program will display the policies you own and will ask you to select one to drop.
     Once you select a valid option the program will output a message upon successful deletion of the policy

     -Option3 is titled "Make a Payment". 
     Upon entry in this feature you will be presented with the policies you own once again.
     You will be asked which policy you want to make the payment toward.
     Feel free to pick any policy number presented. Next it will ask you the amount you would like to pay.
     Enter whatever number you want, the program will check and complain if the entered amount is invalid or unreasonable.
     After collecting this information, the program will preform some queries in the background to insure that the payment receives a unique id.
     The program will also automatically add today's date the payment.
     Finally, you should see a message saying that the payment was made successfully.

     -Option4 is titled "Make a Claim".
     Upon selecting this feature, you will be prompted to describe the circumstances surrounding your claim.
     Enter "I had a heart attack grading so many projects"
     This is all the user input required for this feature.
     The program will take this description and your customerID to make a claim with today's date.
     Some of the relationship tables will be automatically populated but I leave it to the agent and adjuster to verify other aspects based on policy information

Agent Interface: Use agent id '1'

    This is a small add-on interface with 3 options.
   - When you enter this interface it will prompt you for your agent ID. Enter 1.

   -Option 1 is titled "View Assigned Customers". This feature will print out the names and ids of the customers assigned to this agent.

   -Option 2 is titled "View Claims made by assigned customers". This feature will print out the information of each claim made my the customers assigned to this agent.


All done!! I hope you enjoy my hard-work!
Hudson Pavia
