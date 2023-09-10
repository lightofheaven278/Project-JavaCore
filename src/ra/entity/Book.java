package ra.entity;

import java.io.Serializable;
import java.time.Year;
import java.util.List;
import java.util.Scanner;

public class Book implements IEntity<Book>, Serializable {
    String id;
    String title;
    String author;
    String publisher;
    int year;
    String description;
    int categoryId;

    /**
     * Text color
     */
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public Book() {
    }

    public Book(String id, String title, String author, String publisher, int year, String description, int categoryId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.description = description;
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getYear() {
        return year;
    }

    public String getDescription() {
        return description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void input(Scanner input, List<Book> bookList) {
        this.id = validateBookId(input, bookList);
        this.title = validateBookTitle(input, bookList);
        this.author = validateBookAuthor(input);
        this.publisher = validatePublisher(input);
        this.year = validateYear(input);
        this.description = validateDescription(input);
    }

    /**
     * @param input:    pass Scanner through method for inputting data from keyboard
     * @param bookList: pass an array-object through method for checking duplicate book ID
     * @return : return the validated value of ID
     */
    public static String validateBookId(Scanner input, List<Book> bookList) {
        do {
            System.out.println("Please input book ID:");
            String id = input.nextLine();
            if (!id.trim().equals("") && id.startsWith("B") && id.length() == 4) {
                if (bookList.size() == 0) {
                    return id;
                } else {
                    if (!checkDuplicateBookID(bookList, id)) {
                        return id;
                    } else {
                        System.out.println(ANSI_RED +
                                "The inputted ID is already exist. Please try another!" + ANSI_RESET);
                    }
                }
            } else {
                System.out.println(ANSI_RED + "The ID should not a blank and follow this format: B***" + ANSI_RESET);
            }
        } while (true);
    }

    /**
     * @param input:    pass Scanner through method for inputting data from keyboard
     * @param bookList: pass an array-object through method for checking duplicate book title
     * @return : return the validated value of book title
     */
    public static String validateBookTitle(Scanner input, List<Book> bookList) {
        do {
            System.out.println("Please input book title:");
            String title = input.nextLine();
            if (!title.trim().equals("") && title.length() > 6 && title.length() < 50) {
                if (!checkDuplicateBookTitle(bookList, title)) {
                    return title;
                } else {
                    System.out.println(ANSI_RED +
                            "The inputted title is already exist. Please try another!" + ANSI_RESET);
                }
            } else {
                System.out.println(ANSI_RED +
                        "The book title should not be a blank and contain 6-50 characters!" + ANSI_RESET);
            }
        } while (true);
    }

    /**
     * @param input: pass Scanner through method for inputting data from keyboard
     * @return : return the validated value of author
     */
    public static String validateBookAuthor(Scanner input) {
        do {
            System.out.println("Please input author of book:");
            String author = input.nextLine();
            if (!author.trim().equals("")) {
                return author;
            } else {
                System.out.println(ANSI_RED + "The author name should not be a blank!" + ANSI_RESET);
            }
        } while (true);
    }

    /**
     * @param input: pass Scanner through method for inputting data from keyboard
     * @return : return the validated value of publisher
     */
    public static String validatePublisher(Scanner input) {
        do {
            System.out.println("Please input the publisher name:");
            String publisher = input.nextLine();
            if (!publisher.trim().equals("")) {
                return publisher;
            } else {
                System.out.println(ANSI_RED + "The publisher name should not be a blank!" + ANSI_RESET);
            }
        } while (true);
    }

    /**
     * @param input: pass Scanner through method for inputting data from keyboard
     * @return : return the validated value of year
     */
    public static Integer validateYear(Scanner input) {
        do {
            try {
                System.out.println("Please input published year:");
                int year = Integer.parseInt(input.nextLine());
                int actualYear = Year.now().getValue();
                if (year >= 1970 && year <= actualYear) {
                    return year;
                } else {
                    System.out.println(ANSI_RED + "Published Year should be from 1970 to now!" + ANSI_RESET);
                }
            } catch (NumberFormatException ex1) {
                System.out.println(ANSI_RED + "The inputted year is not an integer format" + ANSI_RESET);
            } catch (Exception ex) {
                System.out.println(ANSI_RED + "Err appears while inputting published year!" + ANSI_RESET);
            }
        } while (true);
    }

    /**
     * @param input: pass Scanner through method for inputting data from keyboard
     * @return : return the validated value of description
     */
    public static String validateDescription(Scanner input) {
        do {
            System.out.println("Please input description of book:");
            String description = input.nextLine();
            if (!description.trim().equals("")) {
                return description;
            } else {
                System.out.println(ANSI_RED+"The description of book should not be a blank"+ ANSI_RESET);
            }
        } while (true);
    }

    /**
     * @param bookList: pass an array-object through method for checking duplicate book title
     * @param arr:      pass an array to check if that arr is same with ID of book in bookList
     * @return : return a boolean value of method that will be used as condition to
     * check duplicate ID while adding or updating
     */
    public static boolean checkDuplicateBookID(List<Book> bookList, String arr) {
        boolean checkDuplicateID = false;
        for (Book book : bookList) {
            if (book.getId().equals(arr)) {
                checkDuplicateID = true;
                break;
            }
        }
        return checkDuplicateID;
    }

    public static boolean checkDuplicateBookTitle(List<Book> bookList, String arr) {
        boolean checkDuplicateTitle = false;
        for (Book book : bookList) {
            if (book.getTitle().equals(arr)) {
                checkDuplicateTitle = true;
                break;
            }
        }
        return checkDuplicateTitle;
    }

    /**
     * @param categoryList: pass list of categories through method to get category name representing category of book
     */
    @Override
    public void output(List<Category> categoryList) {
        String categoryName = null;
        for (Category category : categoryList) {
            if (category.getId() == this.categoryId) {
                categoryName = category.getName();
            }
        }
        System.out.printf("| " + "%-15s" +
                        " | " + "%-20s" +
                        " | " + "%-20s" +
                        " | " + "%-20s" +
                        " | " + "%-20d" +
                        " | " + "%-20s" +
                        " | " + "%-20s" +
                        " | " + "\n", this.id, this.title, this.author,
                this.publisher, this.year, this.description, categoryName);
    }
}
