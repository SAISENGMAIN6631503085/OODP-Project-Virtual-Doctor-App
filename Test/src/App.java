import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

interface UserData
{
  String getName();

  void setName(String name);

  double getAge();

  void setAge(double age);

  double getWeight();

  void setWeight(double weight);

  double getHeight();

  void setHeight(double height);

  String getGender();

  void setGender(String gender);

  String isSmoker();

  void setSmoker(String smoker);

  String isDrinker();

  void setDrinker(String drinker);

  String getPatientCode();
}

class User implements UserData
{
  private static int patientCounter = 1;
  private String patientCode;
  private String name;
  private double age;
  private double weight;
  private double height;
  private String gender;
  private String smoker;
  private String drinker;
  private List<String> medicalHistory = new ArrayList<>();

  public User(String name, double age, double weight, double height, String gender,
    String smoker, String drinker)
  {
    this.patientCode = "A" + String.format("%04d", patientCounter++);
    this.name = name;
    this.age = age;
    this.weight = weight;
    this.height = height;
    this.gender = gender;
    this.smoker = smoker;
    this.drinker = drinker;
  }

  public static User loadUser(String patientCode, String name, double age,
                              double weight, double height, String gender, String smoker,
                              String drinker)
  {
    User user = new User(name, age, weight, height, gender, smoker, drinker);
    user.patientCode = patientCode;
    return user;
  }

  public void addMedicalHistory(String diagnosis) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String currentDate = dateFormat.format(new Date());
    medicalHistory.add("Date of Diagnosis: " + currentDate + ": " + diagnosis);
}

  public List<String> getMedicalHistory()
  {
    return medicalHistory;
  }

  public void setPatientCode(String code)
  {
    this.patientCode = code;
  }

  @Override
  public String getPatientCode()
  {
    return patientCode;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public double getAge()
  {
    return age;
  }

  @Override
  public void setAge(double age)
  {
    this.age = age;
  }

  @Override
  public double getWeight()
  {
    return weight;
  }

  @Override
  public void setWeight(double weight)
  {
    this.weight = weight;
  }

  @Override
  public double getHeight() {
      return height;
  }

  @Override
  public void setHeight(double height) {
      this.height = height;
  }

  @Override
  public String getGender()
  {
    return gender;
  }

  @Override
  public void setGender(String gender)
  {
    this.gender = gender;
  }

  @Override
  public String isSmoker()
  {
    return smoker;
  }

  @Override
  public void setSmoker(String smoker)
  {
    this.smoker = smoker;
  }

  @Override
  public String isDrinker()
  {
    return drinker;
  }

  @Override
  public void setDrinker(String drinker)
  {
    this.drinker = drinker;
  }

  @Override
  public String toString()
  {
    return "Patient Code: " + patientCode + ", Name: " + name + ", Age: " + age
      + ", Weight: " + weight + ", Height: " + height + ", Gender: " + gender + ", Smoker: " + smoker
      + ", Drinker: " + drinker;
  }
}

interface HealthAdvisor
{
  void diagnose(List<String> symptoms, User user);

  void giveEnvironmentalAdvice1(double pm25Level, User currentUser);
}

class ConsoleVirtualDoctorApp implements HealthAdvisor
{
  private List<User> patients = new ArrayList<>();
  private Scanner scanner = new Scanner(System.in);
  private static final List<String> symptoms =
    Arrays.asList("Cough", "Fever", "Headache", "Fatigue",
                  "Shortness of breath");
  private static final List<String> healthProblems =
    Arrays.asList("Common Cold or Flu", "Possible Infection or Flu",
                  "Migraine or Tension Headaches",
                  "General Fatigue or Overwork", "Possible Asthma or COVID-19");
  private static final List<String> cures =
    Arrays
      .asList("Sip warm liquid and stay hydrated",
              "Warm Shower, Plenty of rest, and avoid Alcohol, tea and coffee",
              "Rest in a quiet, dark room, also hot or cold compresses to your head or neck",
              "Get plenty of rest while avoid all stress. Drink plenty of water",
              "Purchase quick-relief inhalers (bronchodilators) quickly open swollen airways that are limiting breathing, else if it is COVID-19 then purchase acetaminophen or ibuprofen, to help feel better and stay healthy by getting plenty of rest, fluid and stay active while also avoid alcohol and smoking.");

  public static void main(String[] args)
  {
    ConsoleVirtualDoctorApp app = new ConsoleVirtualDoctorApp();
    app.run();
  }

  private void providePM25Advice(User user) {
    double pm25Level = -1;
    while (pm25Level < 0) {
        System.out.println("Enter the PM2.5 level in µg/m³:");
        try {
            pm25Level = Double.parseDouble(scanner.nextLine());
            if (pm25Level < 0) {
                System.out.println("Invalid input. PM2.5 level cannot be negative. Please enter a valid number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number for PM2.5 level.");
        }
    }
    giveEnvironmentalAdvice1(pm25Level, user);
}

  private void run()
  {
    loadPatientsFromFile();
    System.out.println("Welcome to the Virtual Doctor App");

    User currentUser = userLoginOrRegister();

    boolean running = true;
    while (running)
    {
      System.out.println("\nPlease select an option:");
      System.out.println("1. Diagnose a symptom");
      System.out.println("2. Get PM2.5 advice");
      System.out.println("3. Calculate BMI");
      System.out.println("4. Change patient");
      System.out.println("5. View medical history");
      System.out.println("6. Edit patient information");
      System.out.println("0. Exit");
      String option = scanner.nextLine();

      switch (option)
      {
        case "1":
          diagnoseSymptom(currentUser);
          break;
        case "2":
          providePM25Advice(currentUser);
          break;
        case "3":
          calculateBMI(currentUser);
          break;
        case "4":
          User tester = changePatient();
          if(tester != null) 
          {
            currentUser = tester;
          }
          break;
        case "5":
          viewMedicalHistory(currentUser);
          break;
        case "6":
          editPatientInfo(currentUser);  
          break;
        case "0":
          running = false;
          savePatientsToFile();
          System.out.println("Exiting and saving data...");
          break;
        default:
          System.out.println("Invalid option. Please try again.");
      }
    }
  }

  private void editPatientInfo(User user) {
    System.out.println("Editing information for: " + user.getName());
    System.out.println("Select the information to edit:");
    System.out.println("1. Name");
    System.out.println("2. Age");
    System.out.println("3. Weight");
    System.out.println("4. Height");
    System.out.println("5. Gender");
    System.out.println("6. Smoker");
    System.out.println("7. Drinker");
    System.out.println("0. Cancel");

    String choice = scanner.nextLine();
    while (!"0".equals(choice)) {
        switch (choice) {
            case "1":
                editAttributeName(user);
                break;
            case "2":
                editAttributeAge(user);
                break;
            case "3":
                editAttributeWeight(user);
                break;
            case "4":
                editAttributeHeight(user);
                break;
            case "5":
                editAttributeGender(user);
                break;
            case "6":
                editAttributeSmoker(user);
                break;
            case "7":
                editAttributeDrinker(user);
                break;
            default:
                System.out.println("Invalid selection. Please try again.");
        }
        System.out.println("Do you want to edit another attribute? (Enter number or '0' to exit)");
        choice = scanner.nextLine();
    }
    System.out.println("Exiting edit mode.");
}

private boolean confirmChange(String message) {
    while (true) {
        System.out.println(message + " (yes/no)");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if ("yes".equals(confirmation)) {
            return true;
        } else if ("no".equals(confirmation)) {
            return false;
        } else {
            System.out.println("Invalid response. Please type 'yes' or 'no'.");
        }
    }
}


private void editAttributeName(User user) {
    while (true) {
        System.out.print("Enter new name (or type 'cancel' to stop): ");
        String newName = scanner.nextLine();
        if ("cancel".equalsIgnoreCase(newName)) {
            System.out.println("Edit cancelled.");
            return;
        }
        if (!newName.isEmpty() && newName.matches("^[A-Za-z ]+$")) {
            if (confirmChange("Are you sure you want to change the name to " + newName)) {
                user.setName(newName);
                System.out.println("Name changed successfully to " + newName + ".");
                return;
            } else {
                System.out.println("Name change cancelled.");
                return;
            }
        } else {
            System.out.println("Invalid name. Please try again.");
        }
    }
}

private void editAttributeAge(User user) {
    while (true) {
        System.out.print("Enter new age (or type 'cancel' to stop): ");
        String ageInput = scanner.nextLine();
        if ("cancel".equalsIgnoreCase(ageInput)) {
            System.out.println("Edit cancelled.");
            return;
        }
        try {
            double newAge = Double.parseDouble(ageInput);
            if (newAge > 0) {
                if (confirmChange("Are you sure you want to change the age to " + newAge)) {
                    user.setAge(newAge);
                    System.out.println("Age changed successfully to " + newAge + ".");
                    return;
                } else {
                    System.out.println("Age change cancelled.");
                    return;
                }
            } else {
                System.out.println("Invalid age. Please enter a positive number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }
}


private void editAttributeWeight(User user) {
    while (true) {
        System.out.print("Enter new weight in kg (or type 'cancel' to stop): ");
        String weightInput = scanner.nextLine();
        if ("cancel".equalsIgnoreCase(weightInput)) {
            System.out.println("Edit cancelled.");
            return;
        }
        try {
            double newWeight = Double.parseDouble(weightInput);
            if (newWeight > 0) {
                if (confirmChange("Are you sure you want to change the weight to " + newWeight)) {
                    user.setWeight(newWeight);
                    System.out.println("Weight changed successfully to " + newWeight + " kg.");
                    return;
                } else {
                    System.out.println("Weight change cancelled.");
                    return;
                }
            } else {
                System.out.println("Invalid weight. Please enter a positive number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }
}


private void editAttributeHeight(User user) {
    while (true) {
        System.out.print("Enter new height in cm (or type 'cancel' to stop): ");
        String heightInput = scanner.nextLine();
        if ("cancel".equalsIgnoreCase(heightInput)) {
            System.out.println("Edit cancelled.");
            return;
        }
        try {
            double newHeight = Double.parseDouble(heightInput);
            if (newHeight > 0) {
                if (confirmChange("Are you sure you want to change the height to " + newHeight)) {
                    user.setHeight(newHeight);
                    System.out.println("Height changed successfully to " + newHeight + " cm.");
                    return;
                } else {
                    System.out.println("Height change cancelled.");
                    return;
                }
            } else {
                System.out.println("Invalid height. Please enter a positive number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }
}


private void editAttributeGender(User user) {
    while (true) {
        System.out.print("Enter gender (male/female) or 'cancel' to stop: ");
        String newGender = scanner.nextLine().trim();
        if ("cancel".equalsIgnoreCase(newGender)) {
            System.out.println("Edit cancelled.");
            return;
        }
        if ("male".equalsIgnoreCase(newGender) || "female".equalsIgnoreCase(newGender)) {
            if (confirmChange("Are you sure you want to change the gender to " + newGender)) {
                user.setGender(newGender);
                System.out.println("Gender changed successfully to " + newGender + ".");
                return;
            } else {
                System.out.println("Gender change cancelled.");
                return;
            }
        } else {
            System.out.println("Invalid gender. Please enter 'male' or 'female'.");
        }
    }
}


private void editAttributeSmoker(User user) {
    while (true) {
        System.out.print("Are you a smoker? (yes/no) or 'cancel' to stop: ");
        String newSmoker = scanner.nextLine().trim();
        if ("cancel".equalsIgnoreCase(newSmoker)) {
            System.out.println("Edit cancelled.");
            return;
        }
        if ("yes".equalsIgnoreCase(newSmoker) || "no".equalsIgnoreCase(newSmoker)) {
            if (confirmChange("Are you sure you want to change smoker status to " + newSmoker)) {
                user.setSmoker(newSmoker);
                System.out.println("Smoker status changed successfully to " + newSmoker + ".");
                return;
            } else {
                System.out.println("Smoker status change cancelled.");
                return;
            }
        } else {
            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
        }
    }
}


private void editAttributeDrinker(User user) {
    while (true) {
        System.out.print("Are you a drinker? (yes/no) or 'cancel' to stop: ");
        String newDrinker = scanner.nextLine().trim();
        if ("cancel".equalsIgnoreCase(newDrinker)) {
            System.out.println("Edit cancelled.");
            return;
        }
        if ("yes".equalsIgnoreCase(newDrinker) || "no".equalsIgnoreCase(newDrinker)) {
            if (confirmChange("Are you sure you want to change drinker status to " + newDrinker)) {
                user.setDrinker(newDrinker);
                System.out.println("Drinker status changed successfully to " + newDrinker + ".");
                return;
            } else {
                System.out.println("Drinker status change cancelled.");
                return;
            }
        } else {
            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
        }
    }
}


  public void calculateBMI(User currentUser) {
    double weight = currentUser.getWeight();
    double height = currentUser.getHeight();
    double heightMeter = height / 100.0;
    double bmi = weight / (heightMeter * heightMeter);
    System.out.println("Your BMI calculated from your stored height and weight is: " + String.format("%.2f", bmi));
    
    String classification = "";

    if (bmi >= 30) {
        classification = "Obese";
    } else if (bmi >= 25) {
        classification = "Overweight";
    } else if (bmi >= 18.5) {
        classification = "Healthy";
    } else {
        classification = "Underweight";
    }
    System.out.println("Which is classified as: " + classification);
}
  
  public User changePatient() 
  {
    while (true) 
    {
        System.out.println("Please enter your patient code (or type 'back' to return to the main menu):");
        String code = scanner.nextLine().trim();

        if ("back".equalsIgnoreCase(code)) 
        {
            return null; 
        }

        for (User user : patients)
        {
            if (user.getPatientCode().equalsIgnoreCase(code))
            {
                System.out.println("Welcome back, " + user.getName());
                return user; 
            }
        }
        System.out.println("Patient code not found. Please try again.");
    }
  }

  private User userLoginOrRegister()
  {
    String hasCode = "";
    while (true)
    {
      System.out.println("Do you have a patient code? (yes/no)");
      hasCode = scanner.nextLine().trim();
      if ("yes".equalsIgnoreCase(hasCode) || "no".equalsIgnoreCase(hasCode))
      {
        break;
      }
      else
      {
        System.out.println("Invalid input. Please answer 'yes' or 'no'.");
      }
    }

    if ("yes".equalsIgnoreCase(hasCode))
    {
      boolean tryAgain = true;
      while (tryAgain)
      {
        System.out.println("Please enter your patient code:");
        String code = scanner.nextLine().trim();
        for (User user : patients)
        {
          if (user.getPatientCode().equals(code))
          {
            System.out.println("Welcome back, " + user.getName());
            return user;
          }
        }

        while (true)
        {
          System.out.println("Patient code not found. Would you like to try again or register as a new user? (Type 'yes' [Try Again] or Type 'no' [Register])");
          String response = scanner.nextLine().trim();
          if ("yes".equalsIgnoreCase(response))
          {
            break;
          }
          else if ("no".equalsIgnoreCase(response))
          {
            tryAgain = false;
            break;
          }
          else
          {
            System.out.println("Invalid input. Please answer 'yes' or 'no'.");
          }
        }
      }
    }
    System.out.println("Please fill in your information to register as a new user.");
    return inputUserData();
  }

  private User inputUserData()
{
    System.out.print("Enter name: ");
    String name = scanner.nextLine();
    while(name.trim().isEmpty() || !name.matches("^[A-Za-z ]+$")) {
        System.out.println("Invalid name. Names should contain only letters and spaces. Please enter a valid name.");
        System.out.print("Enter name: ");
        name = scanner.nextLine();
    }

    double age = 0;
    while (true) {
        System.out.print("Enter age: ");
        String ageInput = scanner.nextLine();
        try {
            age = Double.parseDouble(ageInput);
            if(age <= 0) {
                System.out.println("Age must be a positive number. Please enter a valid age.");
            } else {
                break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid age. Please enter a valid number.");
        }
    }

    double weight = 0;
    while (true) {
        System.out.print("Enter weight: ");
        String weightInput = scanner.nextLine();
        try {
            weight = Double.parseDouble(weightInput);
            if(weight <= 0) {
                System.out.println("Weight must be a positive number. Please enter a valid weight.");
            } else {
                break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid weight. Please enter a valid number.");
        }
    }

    double height = 0;
    while (true) {
        System.out.print("Enter height in cm: ");
        String heightInput = scanner.nextLine();
        try {
            height = Double.parseDouble(heightInput);
            if(height <= 0) {
                System.out.println("Height must be a positive number. Please enter a valid height.");
            } else {
                break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid height. Please enter a valid number.");
        }
    }

    String gender;
    while (true)
    {
        System.out.print("Enter gender (male/female): ");
        gender = scanner.nextLine().trim();
        if ("female".equalsIgnoreCase(gender) || "male".equalsIgnoreCase(gender))
        {
            break;
        }
        else
        {
            System.out.println("Invalid gender. Please enter 'male' or 'female'.");
        }
    }

    String smoker;
    while (true)
    {
        System.out.print("Are you a smoker? (yes/no): ");
        smoker = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(smoker) || "no".equalsIgnoreCase(smoker))
        {
            break;
        }
        else
        {
            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
        }
    }

    String drinker;
    while (true)
    {
        System.out.print("Are you a drinker? (yes/no): ");
        drinker = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(drinker) || "no".equalsIgnoreCase(drinker))
        {
            break;
        }
        else
        {
            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
        }
    }

    User newUser = new User(name, age, weight, height, gender, smoker, drinker);
    patients.add(newUser);
    System.out.println("Your patient code is: " + newUser.getPatientCode());
    return newUser;
}

  @Override
  public void diagnose(List<String> symptoms, User user)
  {
    if (symptoms.isEmpty())
    {
      System.out.println("No symptoms were provided.");
      return;
    }

    String symptom = symptoms.get(0);
    int index = ConsoleVirtualDoctorApp.symptoms.indexOf(symptom);

    if (index != -1)
    {
      String diagnosis = healthProblems.get(index);
      String cure = cures.get(index);
      String result =
        "Diagnosis based on symptom (" + symptom + "): " + diagnosis
          + ". Recommended action: " + cure;

      user.addMedicalHistory(result);
      System.out.println(result);
    }
    else
    {
      System.out.println("Symptom not recognized.");
    }
  }

  private void diagnoseSymptom(User currentUser)
  {
    System.out.println("Select a symptom:");
    for (int i = 0; i < symptoms.size(); i++)
    {
      System.out.println((i + 1) + ". " + symptoms.get(i));
    }
    int choice = Integer.parseInt(scanner.nextLine());

    if (choice > 0 && choice <= symptoms.size())
    {
      List<String> selectedSymptoms = new ArrayList<>();
      selectedSymptoms.add(symptoms.get(choice - 1));
      diagnose(selectedSymptoms, currentUser);
    }
    else
    {
      System.out.println("Invalid selection.");
    }
  }

  @Override
  public void giveEnvironmentalAdvice1(double pm25Level, User currentUser)
  {
    boolean sensitive = false;
    if (currentUser.isSmoker() == "yes" || currentUser.getAge() > 64
      || currentUser.getAge() < 14)
    {
      sensitive = true;
    }

    String advice;
    if (pm25Level >= 0 && pm25Level <= 12)
    {
      advice =
        "Low (0-12 µg/m³ PM2.5): Air quality is considered safe for the general population.";
    }
    else if (pm25Level <= 35.4)
    {
      if (!sensitive)
      {
        advice =
          "Moderate (12.1-35.4 µg/m³ PM2.5): Air quality is acceptable and safe.";
      }
      else
      {
        advice =
          "Moderate (12.1-35.4 \u00B5g/m\u00B3 PM2.5): Please limit your time outdoors for moderate health concerns due body sensitivity to air pollution.";
      }
    }
    else if (pm25Level <= 55.4)
    {

      if (!sensitive)
      {
        advice =
          "Health-concerned (35.5-55.4 µg/m³ PM2.5): Not likely to be affected.";
      }
      else
      {
        advice =
          "Health-concerned (35.5-55.4 \u00B5g/m\u00B3 PM2.5): Likely to experiance negative health effects.";
      }
    }
    else if (pm25Level <= 150.4)
    {
      if (!sensitive)
      {
        advice =
          "Unhealthy (55.5-150.4 µg/m³ PM2.5): You may begin to experience negative health effects.";
      }
      else
      {
        advice =
          "Unhealthy (55.5-150.4 \u00B5g/m\u00B3 PM2.5): You will highly experience detrimental health effects.";
      }
    }
    else if (pm25Level <= 250.4)
    {
      if (!sensitive)
      {
        advice =
          "Very Unhealthy (150.5-250.4 µg/m³ PM2.5): Health alert: everyone may experience more serious health effects, please stay indoor at all times.";
      }
      else
      {
        advice =
          "Very Unhealthy (150.5-250.4 \u00B5g/m\u00B3 PM2.5): Health alert: You will experiace severe health effects, stay indoors at all times.";
      }
    }
    else
    {
      advice =
        "Hazardous (250.5 µg/m³ PM2.5 and above): Health warnings of emergency conditions. The entire population is more likely to be affected.";
    }
    System.out.println(advice);
  }

  private void viewMedicalHistory(User currentUser)
  {
    System.out.println("Medical History for " + currentUser.getName() + " ("
      + currentUser.getPatientCode() + "):");
    List<String> history = currentUser.getMedicalHistory();
    if (history.isEmpty())
    {
      System.out.println("No medical history available.");
    }
    else
    {
      history.forEach(System.out::println);
    }
  }

  private void savePatientsToFile()
  {
    try (PrintWriter out = new PrintWriter("patients.txt"))
    {
      for (User user : patients)
      {
        out.printf("Code:%s\n", user.getPatientCode());
        out.printf("Name:%s\n", user.getName());
        out.printf("Age:%.2f\n", user.getAge());
        out.printf("Weight:%.2f\n", user.getWeight());
        out.printf("Height:%.2f\n", user.getHeight());
        out.printf("Gender:%s\n", user.getGender());
        out.printf("Smoker:%s\n", user.isSmoker());
        out.printf("Drinker:%s\n", user.isDrinker());

        for (String historyItem : user.getMedicalHistory())
        {
          out.printf("History:%s\n", historyItem);
        }

        out.println();
      }
    }
    catch (FileNotFoundException e)
    {
      System.out.println("Unable to save patients data: " + e.getMessage());
    }
  }

  private void loadPatientsFromFile()
  {
    try (Scanner fileScanner = new Scanner(new File("patients.txt")))
    {
      String name = "", gender = "", smoker = "", drinker = "", code = "";
      double age = 0, weight = 0, height = 0;
      List<String> history = new ArrayList<>();

      while (fileScanner.hasNextLine())
      {
        String line = fileScanner.nextLine().trim();
        if (line.isEmpty())
        {
          User user =
            User.loadUser(code, name, age, weight, height, gender, smoker, drinker);
          for (String historyItem : history)
          {
            user.addMedicalHistory(historyItem);
          }
          patients.add(user);
          name = gender = smoker = drinker = code = "";
          age = weight = 0;
          history.clear();
        }
        else
        {
          String[] parts = line.split(":", 2);
          if (parts.length == 2)
          {
            switch (parts[0])
            {
              case "Code":
                code = parts[1];
                break;
              case "Name":
                name = parts[1];
                break;
              case "Age":
                age = Double.parseDouble(parts[1]);
                break;
              case "Weight":
                weight = Double.parseDouble(parts[1]);
                break;
              case "Height":
                height = Double.parseDouble(parts[1]);
                break;  
              case "Gender":
                gender = parts[1];
                break;
              case "Smoker":
                smoker = parts[1];
                break;
              case "Drinker":
                drinker = parts[1];
                break;
              case "History":
                history.add(parts[1]);
                break;
            }
          }
        }
      }

      if (!code.isEmpty())
      {
        User user =
          User.loadUser(code, name, age, weight, height, gender, smoker, drinker);
        for (String historyItem : history)
        {
          user.addMedicalHistory(historyItem);
        }
        patients.add(user);
      }
    }
    catch (FileNotFoundException e)
    {
      System.out.println("No previous data found.");
    }
    catch (Exception e)
    {
      System.out.println("Error reading from file: " + e.getMessage());
    }
  }
}
