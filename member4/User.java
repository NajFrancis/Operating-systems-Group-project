//Name: Daynea Chambers 2400660
package member4;
import java.io.*;
import java.util.Scanner;
import java.util.Random;

public class User{

  private int user_ID;
  private String username;
  private String group_assigned;
  private String password;
  private Queue q=new Queue();
  private Scanner input=new Scanner(System.in);

  //Default Constructor 
  public User()
  {
    user_ID=0;
    username= " ";
    group_assigned= " ";
    password=" ";
  }

  // Function to generate random user ID
  public int create_userID() {
      Random random = new Random();
      return random.nextInt(16);
  }

  // Function to hash password
  public int hash_password(String password) {
      int hash = 0;

      for (int i = 0; i < password.length(); i++) {
          char c = password.charAt(i);
          hash = hash * 31 + c;
      }

      return hash;
  }

  // Function to randomly assign users to a group
  public String create_group() {

      String[] groups = {"Admin", "Standard"};
      Random random = new Random();
      return groups[random.nextInt(groups.length)];
  }

  // Function to register a new user
  public void register() {

      System.out.print("Please enter a username: ");
      username = input.nextLine();

      do {
          System.out.print("Please enter a password (minimum 8 characters): ");
          password = input.nextLine();

          if (password.length() < 8) {
              System.out.println("Password must be at least 8 characters.");
          }

      } while (password.length() < 8);

      int hashed_pwd = hash_password(password);
      System.out.println("Registered successfully!");

      // Generate user ID
      user_ID = create_userID();

      // Assign group
      group_assigned = create_group();

      System.out.println("Your User ID is: " + user_ID);
      System.out.println("Your assigned group is: " + group_assigned);

      // Store user information
      try {
          FileWriter passwordWriter = new FileWriter("password.txt",true);
          passwordWriter.write(username + " " + user_ID + " " + hashed_pwd + "\n");
          passwordWriter.close();

          FileWriter groupWriter = new FileWriter("group.txt", true);
          groupWriter.write(user_ID + " " + group_assigned + "\n");
          groupWriter.close();

      } catch (IOException e) {
          System.out.println("Error saving user information.");
      }
  }

  // Function to authenticate an existing user
  public boolean login() {

      System.out.print("Please enter your username: ");
      String enteredUsername = input.nextLine();

      System.out.print("Please enter your password: ");
      String enteredPassword = input.nextLine();

      int enteredHash = hash_password(enteredPassword);

      try {
          BufferedReader reader = new BufferedReader(
                  new FileReader("password.txt"));
          

          String line;

          while ((line = reader.readLine()) != null) {

              String[] details = line.split(" ");

              if (details.length == 3
                      && details[0].equals(enteredUsername)) {

                  int storedHash = Integer.parseInt(details[2]);

                  if (enteredHash == storedHash) {

                      username = details[0];
                      user_ID = Integer.parseInt(details[1]);

                      reader.close();

                      System.out.println("Logged in successfully!");
                      return true;
                  }
              }
          }

          reader.close();

      } catch (IOException e) {
          System.out.println("Error reading user information.");
      }

      System.out.println("Invalid username or password.");
      return false;
  }

  // CPU function
  public void CPU() {
      //System.out.println("CPU resource accessed.");
      create_process("CPU0");
  }

  // Memory function
  public void Memory() {
      //System.out.println("Memory resource accessed.");
      create_process("RAM_Block1");
  }

  // Files function
  public void Files() {
      //System.out.println("Files resource accessed.");
      create_process("FileA.txt");
  }

  // Function to access protected resources
  public void ProtectedResources() {

      if (group_assigned.equals("Admin")) {
          System.out.println("Access granted!");
      } else {
          System.out.println("Access denied!");
      }
  }

  // Function to create a process
  public void create_process(String resource) {
      Process p=new Process();
      int p_id=p.generateProcessID();
      
      p.setresourceRequired(resource);
      System.out.println("Process starting with ID" + p_id + "\n Requesting access to " + resource);
      q.AddProcesstoQueue(p);
      
  }



// Main menu
public void menu() {

    int option;

    do {

        System.out.println("\n*******MAIN MENU********");
        System.out.println("1. CPU");
        System.out.println("2. Memory");
        System.out.println("3. Files");
        System.out.println("4. Protected Resources");
        System.out.println("5. View Waiting Process Queue");
        System.out.println("6. View Ready Process Queue");
        System.out.println("7. Exit");
        System.out.print("Select an option: ");

        
        option = input.nextInt();
        input.nextLine();

        switch (option) {

            case 1:
                CPU();
                break;

            case 2:
                Memory();
                break;

            case 3:
                Files();
                break;

            case 4:
                ProtectedResources();
                break;
            
            case 5:
                q.ViewWaitingQueue();
                break;
                
            case 6:
                q.ViewReadyQueue();
                break;

            case 7:
                System.out.println("Logged out successfully!");
                break;

            default:
                System.out.println("Invalid option...try again.");
        }

    } while (option != 7);
}
  // Authentication function
  public void authentication() {

      while (true) {

          System.out.println("\nAre you a first-time user? (Yes/No)");
          String option = input.nextLine();

          if (option.equalsIgnoreCase("Yes")) {

              register();
              menu();

              return;

          } else if (option.equalsIgnoreCase("No")) {

              if (login()) {
                  menu();
                  return;
              }

          } else {
              System.out.println("Invalid selection. Enter Yes or No.");
          }
      }
  }

  // Main function
  public static void main(String[] args) {
      User user=new User();

      try {
          // Create files if they do not exist
          new File("password.txt").createNewFile();
          new File("group.txt").createNewFile();

      } catch (IOException e) {
          System.out.println("Unable to create files.");
          return;
      }

      System.out.println("*********Group 4 OS Security Presentation**********");

      user.authentication();

  }
}
