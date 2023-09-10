package ra.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;


public class Category implements IEntity<Category>, Serializable {
    int id;
    String name;
    boolean status;

    /**
     * Text color
     */
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public Category() {
    }

    public Category(int id, String name, boolean status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public void input(Scanner input, List<Category> categoryList) {
        this.id = validateCategoryId(input, categoryList);
        this.name = validateCategoryName(input, categoryList);
        this.status = inputBoolean(input);
    }

    /**
     * Validate category ID
     *
     * @param input:        declare input function to input data from keyboard
     * @param categoryList: pass into method an array object of category
     * @return : retrieve an integer value of inputted ID that is already validated
     */
    public static Integer validateCategoryId(Scanner input, List<Category> categoryList) {
        do {
            try {
                System.out.println("Please input the category ID:");
                String idStr = input.nextLine();
                if (!idStr.trim().equals("")) {
                    if (Integer.parseInt(idStr) <= 0) {
                        System.out.println(ANSI_RED +
                                "The category ID should be an integer which is greater than 0" + ANSI_RESET);
                    } else {
                        if (categoryList.size() == 0) {
                            return Integer.parseInt(idStr);
                        } else {
                            boolean checkId = false;
                            for (Category category : categoryList) {
                                if (category.getId() == Integer.parseInt(idStr.trim())) {
                                    System.out.println(ANSI_RED +
                                            "The inputted ID is already exist. Please try another!" + ANSI_RESET);
                                    checkId = true;
                                }
                            }
                            if (!checkId) {
                                return Integer.parseInt(idStr);
                            }
                        }
                    }
                } else {
                    System.out.println(ANSI_RED +
                            "The inputted data ID of category should not be a blank!" + ANSI_RESET);
                }
            } catch (NumberFormatException ex1) {
                System.out.println(ANSI_RED +
                        "The inputted data format is not integer format. Please try again!" + ANSI_RESET);
            } catch (Exception ex) {
                System.out.println(ANSI_RED + "Err appears while inputting category ID" + ANSI_RESET);
            }
        } while (true);
    }

    /**
     * Validate category name
     *
     * @param input:        pass through method Scanner input
     * @param categoryList: pass through an object-array list
     * @return : return a string value of validated name inputted from keyboard
     */
    public static String validateCategoryName(Scanner input, List<Category> categoryList) {
        do {
            try {
                System.out.println("Please input the category name:");
                String name = input.nextLine();
                if (name.trim().equals("")) {
                    System.out.println(ANSI_RED + "Category name should not be a blank!" + ANSI_RESET);
                    continue;
                }
                if (name.length() < 6 || name.length() > 30) {
                    System.out.println(ANSI_RED +
                            "Category name should contain 6-30 characters!" + ANSI_RESET);
                    continue;
                }
                boolean checkName = false;
                for (Category category : categoryList) {
                    if (category.getName().trim().equals(name.trim())) {
                        checkName = true;
                        break;
                    }
                }
                if (!checkName) {
                    return name;
                } else {
                    System.out.println(ANSI_RED +
                            "The inputted category name is already exist. Please try another!" + ANSI_RESET);
                }
            } catch (Exception ex) {
                System.out.println(ANSI_RED + "Err appears while inputting category name" + ANSI_RESET);
            }
        } while (true);
    }

    /**
     * Validate category status
     *
     * @param input: pass through Scanner input for inputting value from keyboard
     * @return : return the boolean value of status
     */
    public static boolean inputBoolean(Scanner input) {
        do {
            System.out.println("Please input status of category:");
            String status = input.nextLine();
            if (status.equalsIgnoreCase("true") || status.equalsIgnoreCase("false")) {
                return Boolean.parseBoolean(status);
            } else {
                System.out.println(ANSI_RED + "The status of category should be 'true' or 'false'" + ANSI_RESET);
            }
        } while (true);
    }

    /**
     * Display data to console
     */
    @Override
    public void output(List<Category> categoryList) {
        String statusCategory = this.status ? "Active" : "Inactive";
        System.out.printf("| %-11d | %-20s | %-8s |\n", this.id, this.name, statusCategory);
    }
}
