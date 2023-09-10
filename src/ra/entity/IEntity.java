package ra.entity;

import java.util.List;
import java.util.Scanner;

public interface IEntity<E> {
    void input(Scanner input, List<E> bookList);

    void output(List<Category> categoryList);
}
