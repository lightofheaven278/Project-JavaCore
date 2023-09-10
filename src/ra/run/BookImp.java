package ra.run;

import ra.entity.Book;
import ra.entity.Category;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class BookImp {
    /**
     * Text color
     */
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    /**
     * Bold format
     */
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE

    /**
     * Global variable
     */
    static List<Category> categoryList = new ArrayList<>();
    static List<Book> bookList = new ArrayList<>();
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        do {
            try {
                readCategoryListFromFile();
                readBookListFromFile();
                System.out.println("-----------Library Management System-----------");
                System.out.println("1. Category Management System");
                System.out.println("2. Book Management System");
                System.out.println("3. Exit");
                System.out.println("Please input the number above representing the function you wanna choose:");
                int choice = Integer.parseInt(input.nextLine());
                switch (choice) {
                    case 1 -> BookImp.categoryManagement();
                    case 2 -> BookImp.bookManagement();
                    case 3 -> System.exit(0);
                    default -> System.err.println("The inputted choice is out of scope!");
                }
            } catch (NumberFormatException ex1) {
                System.err.println("The inputted choice is not an integer format, " +
                        "and should not a blank. Please try again!");
            } catch (Exception ex) {
                System.err.println("Err appears while inputting choice!");
            }
        } while (true);
    }

    /**
     * Category management
     */
    public static void categoryManagement() {
        boolean checkOutCategory = true;
        do {
            readCategoryListFromFile();
            System.out.println("------------Category Management System------------");
            System.out.println("1. Add new category");
            System.out.println("2. Display category info");
            System.out.println("3. Show statistics of the number of books in each category");
            System.out.println("4. Update info of category");
            System.out.println("5. Delete category");
            System.out.println("6. Exit");
            System.out.println("Please input the number above representing the function you wanna choose:");
            // validate choice's inputted format by try-catch
            try {
                int choice = Integer.parseInt(input.nextLine());
                switch (choice) {
                    // Add a new category
                    case 1 -> {
                        addNewCategory();
                        writeCategoryListToFile();
                    }
                    // Read data from file, then sort by name and display category
                    case 2 -> {
                        readCategoryListFromFile();
                        displayCategoryInfoAfterSortByName();
                    }
                    // Show statistics info
                    case 3 -> showStatisticsInfo();
                    // Update category info
                    case 4 -> {
                        updateInfoOfCategory();
                        writeCategoryListToFile();
                    }
                    // Delete category info
                    case 5 -> {
                        deleteCategory();
                        writeCategoryListToFile();
                    }
                    // Exit category management system
                    case 6 -> {
                        writeCategoryListToFile();
                        checkOutCategory = false;
                    }
                    default -> System.err.println("The inputted choice is out of scope!");
                }
            } catch (NumberFormatException ex1) {
                System.err.println("The inputted choice is not an integer format");
            } catch (Exception ex) {
                System.err.println("Err appears while inputting choice");
            }
        } while (checkOutCategory);
    }

    /**
     * Delete Category
     */
    public static void deleteCategory() {
        displayCategoryInfoAfterSortByName();
        System.out.println("Please input the ID of category you wanna delete:");
        try {
            int categoryIdDelete = Integer.parseInt(input.nextLine());
            boolean checkIdDeleteCategory = false;
            for (Book book : bookList) {
                if (book.getCategoryId() == categoryIdDelete) {
                    System.err.println("You cannot delete the category which has already had book!.");
                    checkIdDeleteCategory = true;
                }
            }
            if (!checkIdDeleteCategory) {
                boolean removed = categoryList.removeIf(category -> category.getId() == categoryIdDelete);
                if (removed) {
                    System.out.println(PURPLE_BOLD + "Category with ID " +
                            categoryIdDelete + " has been removed." + ANSI_RESET);
                } else {
                    System.out.println(ANSI_RED + "The inputted ID is not exist!" + ANSI_RESET);
                }
            }
        } catch (NumberFormatException ex1) {
            System.out.println(ANSI_RED + "The inputted data is not an integer format. Please try again!" + ANSI_RESET);
        } catch (Exception ex) {
            System.out.println(ANSI_RED + "Some errs occur while input" + ANSI_RESET);
        }
    }

    /**
     * Update info of category
     */
    public static void updateInfoOfCategory() {
        do {
            displayCategoryInfoAfterSortByName();
            try {
                String categoryIdStr = checkCategoryIDUpdate();
                int categoryIdUpdate = Integer.parseInt(categoryIdStr);
                int indexCategoryUpdate = BookImp.getIndexCategoryUpdate(categoryIdUpdate);
                if (indexCategoryUpdate == -1) {
                    System.err.println("The inputted ID is not exist!");
                } else {
                    categoryList.get(indexCategoryUpdate).setName(categoryNameUpdate(indexCategoryUpdate));
                    categoryList.get(indexCategoryUpdate).setStatus(Category.inputBoolean(input));
                    System.out.println("Category with ID " + categoryIdUpdate + " was updated!");
                }
                break;
            } catch (NumberFormatException ex1) {
                System.err.println("The inputted ID is not an integer format!");
            } catch (Exception ex) {
                System.err.println("There are some errs occur while inputting category ID for update!");
            }
        } while (true);
    }

    /**
     * @return : return validated category ID for updating category info
     */
    public static String checkCategoryIDUpdate() {
        System.out.println("Please input the category ID you wanna update:");
        do {
            String categoryIdStr = input.nextLine();
            if (categoryIdStr.trim().equals("")) {
                System.out.println(ANSI_RED + "The ID should not be a blank. Please try again!" + ANSI_RESET);
            } else {
                return categoryIdStr;
            }
        } while (true);
    }

    /**
     * @param index: pass through method an integer to get name to check if inputted nam is duplicated with other name
     *               in category list
     * @return : return the validated name for updating category info
     */
    public static String categoryNameUpdate(int index) {
        do {
            try {
                System.out.println("Please input the category name:");
                String name = input.nextLine();
                if (!name.trim().equals("")) {
                    if (name.length() > 6 && name.length() < 30) {
                        boolean checkName = false;
                        for (int i = 0; i < categoryList.size(); i++) {
                            if (i != index) {
                                if (categoryList.get(i).getName().trim().equals(name.trim())) {
                                    checkName = true;
                                    break;
                                }
                            }
                        }
                        if (!checkName) {
                            return name;
                        } else {
                            System.out.println(ANSI_RED +
                                    "The inputted category name is already exist. Please try another!" + ANSI_RESET);
                        }
                    } else {
                        System.out.println(ANSI_RED +
                                "Category name should not be a blank and contain 6-30 characters!" + ANSI_RESET);
                    }
                } else {
                    System.out.println(ANSI_RED + "Category name should not be a blank!" + ANSI_RESET);
                }
            } catch (Exception ex) {
                System.out.println(ANSI_RED + "Err appears while inputting category name" + ANSI_RESET);
            }
        } while (true);
    }

    /**
     * Show statistics info
     */
    public static void showStatisticsInfo() {
        if (categoryList.size() == 0) {
            System.err.println("There is no category!");
        } else {
            BookImp.statisticsCategory();
        }
    }

    /**
     * Display category info after sorting by name
     */
    public static void displayCategoryInfoAfterSortByName() {
        categoryList.sort(Comparator.comparing(Category::getName));
        System.out.print("-------" + ANSI_YELLOW + "CATEGORIES LIST" + ANSI_RESET +
                " (" + ANSI_GREEN + "After sorting(A-Z)" + ANSI_RESET + ")-------\n");
        System.out.printf("| " + PURPLE_BOLD + "%-11s" + ANSI_RESET + " | "
                + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | " + PURPLE_BOLD
                + "%-8s" + ANSI_RESET + " |\n", "CATEGORY ID", "CATEGORY NAME", "STATUS");
        for (Category category : categoryList) {
            System.out.print("-------------------------------------------------\n");
            category.output(categoryList);
        }
        System.out.print("-------------------------------------------------\n");
        System.out.println();
    }

    /**
     * Add new category
     */
    public static void addNewCategory() {
        System.out.println("Please input number of categories you wanna add:");
        try {
            do {
                int numberOfCategory = Integer.parseInt(input.nextLine());
                if (numberOfCategory > 0) {
                    for (int i = 0; i < numberOfCategory; i++) {
                        System.out.println("----------" + ANSI_GREEN + "New Category number " + (i + 1)
                                + ANSI_RESET + "----------");
                        Category newCategory = new Category();
                        newCategory.input(input, categoryList);
                        categoryList.add(newCategory);
                        System.out.println(PURPLE_BOLD + "The new category has already added!" + ANSI_RESET);
                    }
                    break;
                } else {
                    System.out.println(ANSI_RED +
                            "The inputted number should be an integer and greater than 0!" + ANSI_RESET);
                }
            } while (true);

        } catch (NumberFormatException ex1) {
            System.out.println(ANSI_RED + "The inputted number is not an integer format!" + ANSI_RESET);
        } catch (Exception ex) {
            System.out.println(ANSI_RED +
                    "Some errs occur while input the number representing number of categories!" + ANSI_RESET);
        }
    }

    /**
     * Get index for updating category
     *
     * @param categoryIdUpdate:pass an integer value to match with available ID in category list
     * @return : return value of index for updating category info
     */
    public static int getIndexCategoryUpdate(int categoryIdUpdate) {
        int indexUpdate = 0;
        boolean IdMatch = false;
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId() == categoryIdUpdate) {
                indexUpdate = i;
                IdMatch = true;
            }
        }
        if (IdMatch) {
            return indexUpdate;
        } else {
            indexUpdate = -1;
        }
        return indexUpdate;
    }

    /**
     * Total up number of books in each category of category list
     */
    public static void statisticsCategory() {
        List<Integer> categoryIdList = new ArrayList<>();
        List<String> categoryNameList = new ArrayList<>();
        List<Integer> statisticsList = new ArrayList<>();
        // add category ID to categoryIdList
        for (Category category : categoryList) {
            categoryIdList.add(category.getId());
        }
        // add category name to categoryNameList
        for (Integer id : categoryIdList) {
            for (Category category : categoryList) {
                if (id == category.getId()) {
                    categoryNameList.add(category.getName());
                }
            }
        }
        // add number of books of each category to statisticsList
        for (Integer id : categoryIdList) {
            int countDuplicate = 0;
            for (Book book : bookList) {
                if (id == book.getCategoryId()) {
                    countDuplicate++;
                }
            }
            statisticsList.add(countDuplicate);
        }
        // Show statistics
        System.out.println();
        System.out.println("---------------------------" + PURPLE_BOLD + "Statistics Info Of Categories" +
                ANSI_RESET + "---------------------------");
        for (int i = 0; i < categoryIdList.size(); i++) {
            System.out.printf("There is(are)" + ANSI_RED + " %d" + ANSI_RESET + " book(s) in category " +
                            "which has ID " + ANSI_GREEN + "%d" + ANSI_RESET + " and is named " +
                            ANSI_YELLOW + "%s\n" + ANSI_RESET,
                    statisticsList.get(i), categoryIdList.get(i), categoryNameList.get(i));
        }
        System.out.println();
    }

    /**
     * Write category info to file categories.txt
     */
    public static void writeCategoryListToFile() {
        File categoryFile = new File("categories.txt");
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(categoryFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(categoryList);
            oos.flush();
        } catch (FileNotFoundException ex1) {
            System.err.println("Cannot file the file!");
        } catch (IOException ex2) {
            System.err.println("Err appears while writing data!");
        } catch (Exception ex) {
            System.err.println("There are some errs occur while writing data!");
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException ex1) {
                System.err.println("Err appears while closing stream!");
            } catch (Exception ex) {
                System.err.println("There are some errs occur while closing stream!");
            }
        }
    }

    /**
     * Read category from file categories.txt
     */
    public static void readCategoryListFromFile() {
        File newCategoryFile = new File("categories.txt");
        if (newCategoryFile.exists()) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                fis = new FileInputStream(newCategoryFile);
                ois = new ObjectInputStream(fis);
                categoryList = (List<Category>) ois.readObject();
            } catch (FileNotFoundException ex1) {
                System.err.println("Cannot find the file!");
            } catch (IOException ex2) {
                System.err.println("Err appears while reading stream!");
            } catch (Exception ex) {
                System.err.println("There are some errs occur while reading stream!");
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (FileNotFoundException ex1) {
                    System.err.println("Cannot file the file");
                } catch (IOException ex2) {
                    System.err.println("Err appears while closing stream!");
                } catch (Exception ex) {
                    System.err.println("There are some errs occur while closing stream!");
                }
            }
        }
    }


    /**
     * Book Management
     */
    public static void bookManagement() {
        boolean checkOutBook = true;
        do {
            readBookListFromFile();
            System.out.println("--------------Book Management System-------------");
            System.out.println("1. Add a new book");
            System.out.println("2. Update book info");
            System.out.println("3. Delete book info");
            System.out.println("4. Search book");
            System.out.println("5. Display book by category");
            System.out.println("6. Exit");
            try {
                System.out.println("Please input the number above representing the function you wanna choose:");
                int choice = Integer.parseInt(input.nextLine());
                switch (choice) {
                    // Read data from file, then add new book and write book list to file
                    case 1 -> {
                        readCategoryListFromFile();
                        addNewBook();
                        writeBookListToFile();
                    }
                    // Update book info and then write updated info to file
                    case 2 -> {
                        updateBookInfo();
                        writeBookListToFile();
                    }
                    // Delete book info and then write book list to file
                    case 3 -> {
                        deleteBookInfo();
                        writeBookListToFile();
                    }
                    // Read book info from file and search book in book list by book name
                    case 4 -> {
                        readBookListFromFile();
                        searchBookInfoByBookName();
                    }
                    // Read book info from file and then display book info by category
                    case 5 -> {
                        readBookListFromFile();
                        displayBookInfoByCategory();
                    }
                    // Exit book management system
                    case 6 -> {
                        writeBookListToFile();
                        checkOutBook = false;
                    }
                    default -> System.err.println("The inputted choice is out of scope!");
                }
            } catch (NumberFormatException ex1) {
                System.err.println("The inputted choice is not integer format!");
            } catch (Exception ex) {
                System.err.println("There are some errs occur while inputting choice!");
            }
        } while (checkOutBook);
    }

    /**
     * Search book info by book name
     */
    public static void searchBookInfoByBookName() {
        if (bookList.size() == 0) {
            System.err.println("There is no book to search!");
        } else {
            System.out.println("Please input keyword of book(title, author, publisher) you wanna search:");
            String searchKeyword = input.nextLine();
            boolean checkSearchName = false;
            System.out.print("---------------------------------------------------------------------------------" +
                    "----------------------------------------------------------------------------\n");
            System.out.printf("| " + PURPLE_BOLD + "%-15s" + ANSI_RESET + " | "
                            + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                            + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                            + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                            + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                            + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                            + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                            + "\n", "BOOK ID", "TITLE", "AUTHOR", "PUBLISHER",
                    "PUBLISHED YEAR", "DESCRIPTION", "CATEGORY NAME");
            for (Book book : bookList) {
                if (book.getTitle().trim().toLowerCase().contains(searchKeyword.toLowerCase())
                        || book.getAuthor().trim().toLowerCase().contains(searchKeyword.trim().toLowerCase())
                        || book.getPublisher().trim().toLowerCase().contains(searchKeyword.trim().toLowerCase())) {
                    System.out.print("---------------------------------------------------------------------------------" +
                            "----------------------------------------------------------------------------\n");
                    book.output(categoryList);
                    checkSearchName = true;
                }
            }
            System.out.print("---------------------------------------------------------------------------------" +
                    "----------------------------------------------------------------------------\n");
            if (!checkSearchName) {
                System.out.println(ANSI_RED + "There is no book contain keyword you inputted!" + ANSI_RESET);
            }
            System.out.println();
        }
    }

    /**
     * Delete book info
     */
    public static void deleteBookInfo() {
        displayBookList();
        System.out.println("Please input ID of book you wanna delete info:");
        String bookIdDelete = input.nextLine();
        if (bookList.size() == 0) {
            System.err.println("There is no book to delete!");
        } else {
            if (!bookIdDelete.startsWith("B") || bookIdDelete.length() != 4) {
                System.err.println("Book ID should not be a blank and follow this format: B***");
            } else {
                boolean removed = bookList.removeIf(book -> book.getId().equals(bookIdDelete));
                if (removed) {
                    System.out.println(PURPLE_BOLD + "The book with ID " +
                            bookIdDelete + " has been deleted!" + ANSI_RESET);
                } else {
                    System.err.println("The inputted ID is not exist!");
                }
            }
        }
    }

    /**
     * Update book info
     */
    public static void updateBookInfo() {
        displayBookList();
        System.out.println("Please input ID of book you wanna update info:");
        do {
            String bookIdUpdate = input.nextLine();
            if (bookList.size() == 0) {
                System.err.println("There is no book to update!");
                break;
            } else {
                if (!bookIdUpdate.startsWith("B") || bookIdUpdate.length() != 4) {
                    System.err.println("Book ID should follow this format: B***");
                } else {
                    int indexUpdateBook = BookImp.getIndexUpdateBook(bookIdUpdate);
                    bookList.get(indexUpdateBook).setTitle(bookTitleUpdate(indexUpdateBook));
                    bookList.get(indexUpdateBook).setAuthor(Book.validateBookAuthor(input));
                    bookList.get(indexUpdateBook).setPublisher(Book.validatePublisher(input));
                    bookList.get(indexUpdateBook).setYear(Book.validateYear(input));
                    bookList.get(indexUpdateBook).setDescription(Book.validateDescription(input));
                    generateCategoryName();
                    do {
                        try {
                            System.out.println("Please input the number representing the category name:");
                            int updateCategoryNum = Integer.parseInt(input.nextLine());
                            bookList.get(indexUpdateBook).setCategoryId(categoryList.get(updateCategoryNum - 1).
                                    getId());
                            break;
                        } catch (NumberFormatException ex1) {
                            System.out.println(ANSI_RED + "The inputted number is not an integer format" + ANSI_RESET);
                        } catch (Exception ex) {
                            System.out.println(ANSI_RED + "Some errs occur while updating category of book");
                        }
                    } while (true);
                    System.out.println(PURPLE_BOLD + "The book has been updated!" + ANSI_RESET);
                    break;
                }
            }
        } while (true);
    }

    /**
     * Generate list of category name to choose while adding new book or updating book info
     */
    public static void generateCategoryName() {
        System.out.println("-----------" + PURPLE_BOLD + "Category Name" + ANSI_RESET + "----------");
        if (categoryList.size() == 0) {
            System.err.println("Please input data for category!");
        } else {
            for (int i = 0; i < categoryList.size(); i++) {
                System.out.println((i + 1) + ". " + categoryList.get(i).getName());
            }
        }
    }

    /**
     * Check book title for updating
     *
     * @param index: pass through method an integer for checking if inputted title is duplicated with
     *               other titles of other books
     * @return : return the validated title for updating
     */
    public static String bookTitleUpdate(int index) {
        do {
            System.out.println("Please input book title:");
            String title = input.nextLine();
            boolean checkDuplicateTitle = false;
            if (!title.trim().equals("") && title.length() > 6 && title.length() < 50) {
                for (int i = 0; i < bookList.size(); i++) {
                    if (i != index) {
                        if (bookList.get(i).getTitle().trim().equals(title.trim())) {
                            checkDuplicateTitle = true;
                            break;
                        }
                    }
                }
                if (!checkDuplicateTitle) {
                    return title;
                } else {
                    System.out.println(ANSI_RED +
                            "The inputted title is already exist. Please try another!" + ANSI_RESET);
                }
            } else {
                System.out.println(ANSI_RED +
                        "The book title should not be a blank contain 6-50 characters!" + ANSI_RESET);
            }
        } while (true);
    }

    /**
     * Display list of books
     */
    public static void displayBookList() {
        System.out.print("---------------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------\n");
        System.out.printf("| " + PURPLE_BOLD + "%-15s" + ANSI_RESET + " | "
                        + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                        + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                        + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                        + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                        + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                        + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                        + "\n", "BOOK ID", "TITLE", "AUTHOR", "PUBLISHER",
                "PUBLISHED YEAR", "DESCRIPTION", "CATEGORY NAME");
        for (Book book : bookList) {
            System.out.print("---------------------------------------------------------------------------------" +
                    "----------------------------------------------------------------------------\n");
            book.output(categoryList);
        }
        System.out.print("---------------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------\n");
        System.out.println();
    }

    /**
     * Add new book to book list
     */
    public static void addNewBook() {
        generateCategoryName();
        do {
            try {
                System.out.println("Please input the number representing the category name:");
                int choiceNum = Integer.parseInt(input.nextLine());
                do {
                    try {
                        System.out.println("Please input the number of books you wanna add info:");
                        int numberOfBook = Integer.parseInt(input.nextLine());
                        if (numberOfBook > 0) {
                            for (int i = 0; i < numberOfBook; i++) {
                                System.out.println("----------" + ANSI_GREEN + "New Book Number " + (i + 1)
                                        + ANSI_RESET + "----------");
                                Book newBook = new Book();
                                newBook.input(input, bookList);
                                newBook.setCategoryId(categoryList.get(choiceNum - 1).getId());
                                bookList.add(newBook);
                                System.out.println(PURPLE_BOLD + "The new book has already added!" + ANSI_RESET);
                            }
                            break;
                        } else {
                            System.out.println(ANSI_RED +
                                    "The inputted number should be an integer and greater than 0!" + ANSI_RESET);
                        }
                    } catch (NumberFormatException ex1) {
                        System.out.println(ANSI_RED + "The inputted data is not an integer format!" + ANSI_RESET);
                    } catch (Exception ex) {
                        System.out.println(ANSI_RED +
                                "There are some errs occur while inputting choice number!" + ANSI_RESET);
                    }
                } while (true);
                break;
            } catch (NumberFormatException ex1) {
                System.out.println(ANSI_RED + "The inputted data is not an integer format!" + ANSI_RESET);
            } catch (Exception ex) {
                System.out.println(ANSI_RED + "There are some errs occur while inputting choice number!" + ANSI_RESET);
            }
        } while (true);
    }

    /**
     * Display book info by category
     */
    public static void displayBookInfoByCategory() {
        int countCategory = 1;
        for (Category category : categoryList) {
            System.out.println();
            System.out.println(countCategory + ". " + ANSI_YELLOW + category.getName() + ANSI_RESET);
            countCategory++;
            System.out.print("---------------------------------------------------------------------------------" +
                    "-----------------------------------------------------------------------------\n");
            System.out.printf("| " + PURPLE_BOLD + "%-15s" + ANSI_RESET + " | "
                            + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                            + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                            + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                            + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                            + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                            + PURPLE_BOLD + "%-20s" + ANSI_RESET + " | "
                            + "\n", "BOOK ID", "TITLE", "AUTHOR", "PUBLISHER",
                    "PUBLISHED YEAR", "DESCRIPTION", "CATEGORY NAME");
            for (Book book : bookList) {
                if (category.getId() == book.getCategoryId()) {
                    System.out.print("---------------------------------------------------------------------------------"
                            + "-----------------------------------------------------------------------------\n");
                    book.output(categoryList);
                }
            }
            System.out.print("---------------------------------------------------------------------------------" +
                    "-----------------------------------------------------------------------------\n");
        }
        System.out.println();
    }

    /**
     * Get index of book need to update in book list
     *
     * @param bookIdUpdate: pass through method a book ID that would be inputted previously to match with ID available
     *                      in book list
     * @return : return the value of index for updating book info
     */
    public static int getIndexUpdateBook(String bookIdUpdate) {
        int indexUpdate = 0;
        boolean checkBookId = false;
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getId().equals(bookIdUpdate)) {
                indexUpdate = i;
                checkBookId = true;
            }
        }
        if (!checkBookId) {
            indexUpdate = -1;
            System.err.println("The inputted ID is not exist!");
        }
        return indexUpdate;
    }

    /**
     * Write book info to file books.txt
     */
    public static void writeBookListToFile() {
        File newBookFile = new File("books.txt");
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(newBookFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(bookList);
            oos.flush();
        } catch (FileNotFoundException ex1) {
            System.err.println("Cannot find the file!");
        } catch (IOException ex2) {
            System.err.println("Err appears while writing stream!");
        } catch (Exception ex) {
            System.err.println("There are some errs occur while writing stream!");
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException ex1) {
                System.err.println("Err appears while closing stream!");
            } catch (Exception ex) {
                System.err.println("There are some errs occur while closing stream");
            }
        }
    }

    /**
     * Read book info from file books.txt
     */
    public static void readBookListFromFile() {
        File newBookRead = new File("books.txt");
        if (newBookRead.exists()) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                fis = new FileInputStream(newBookRead);
                ois = new ObjectInputStream(fis);
                bookList = (List<Book>) ois.readObject();
            } catch (FileNotFoundException ex1) {
                System.err.println("Cannot find the file!");
            } catch (IOException ex2) {
                System.err.println("Err appears while reading stream!");
            } catch (Exception ex) {
                System.err.println("There are some errs occur while reading stream!");
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException ex1) {
                    System.err.println("Err appears while closing stream!");
                } catch (Exception ex) {
                    System.err.println("There are some errs occur while closing stream!");
                }
            }
        }
    }
}
