package Application.Internal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class ReaderOperations extends Additional {
    // Запуск сценария читателя
    public ReaderOperations(LinkedList<Book> bookLinkedList, Session session) {
        Scanner scan = new Scanner(System.in);
        boolean looper = true;
        do {
            readerMenu();
            System.out.print("Ввод: ");
            String switcher = scan.nextLine();
            switch(switcher) {
                case "1" -> showMyBooks(bookLinkedList, session);
                case "2" -> takeBook(bookLinkedList, session);
                case "3" -> putBook(bookLinkedList, session);
                case "0" -> looper = false;
                default -> throw new IllegalStateException("Unexpected value: " + switcher);
            }
        } while (looper);
    }

    // Меню действий читателя
    void readerMenu() {
        System.out.print(
            """
            1 - Мои книги
            2 - Взять книгу
            3 - Сдать книгу
            0 - Выйти
            """
        );
    }

    // Проверка на доступность книг
    boolean isAnyBooksAvailable(LinkedList<Book> bookLinkedList) {
        for (Book b : bookLinkedList) {
            if(!b.isPicked) return true;
        }
        return false;
    }

    // Функция взятия книги из фонда
    void takeBook(LinkedList<Book> bookLinkedList, Session session) {
        Scanner scan = new Scanner(System.in);
        ArrayList<Book> freeBooks = new ArrayList<>();

        if(isAnyBooksAvailable(bookLinkedList)) {
            System.out.print("Свободные книги: \n");

            for (Book b : bookLinkedList) {
                if (!b.isPicked) {
                    freeBooks.add(b);
                }
            }

            for (int i = 0; i < freeBooks.size(); i++) {
                System.out.printf("Номер: %d\n", i + 1);
                System.out.print(freeBooks.get(i) + "\n");
            }

            System.out.print("Введите номер книги, которую хотите взять.\nЕсли подходящих книг нет, введите 0\n");

            int choice = -1;
            do {
                System.out.print("Ввод: ");
                String temp = scan.nextLine();
                try {
                    choice = Integer.parseInt(temp);
                } catch (NumberFormatException e) {
                    System.out.print("Значение не является цифрой, попробуйте снова.\n");
                }
                if (choice < 0 || choice > freeBooks.size()) {
                    System.out.print("Такого варианта ответа не существует.\n");
                }
            } while (choice < 0 || choice > freeBooks.size());

            if (choice == 0) return;

            for (Book b : bookLinkedList) {
                if (b.equals(freeBooks.get(choice-1))) {
                    b.isPicked = true;
                    b.pickedBy = session.activeUser.login;
                    updateListOfBooks(bookLinkedList);
                    return;
                }
            }
        } else {
            System.out.print("На данный момент нет свободных книг.\n");
        }

    }

    // Функция возвращения книги в фонд
    void putBook(LinkedList<Book> bookLinkedList, Session session) {
        Scanner scan = new Scanner(System.in);
        ArrayList<Book> usedBooks = new ArrayList<>();

        for (Book b : bookLinkedList) {
            if (b.isPicked && b.pickedBy.equals(session.activeUser.login)) {
                usedBooks.add(b);
            }
        }

        if (!usedBooks.isEmpty()) {
            System.out.print("Ваши книги: \n");
            for (int i = 0; i < usedBooks.size(); i++) {
                System.out.printf("Номер: %d\n", i + 1);
                System.out.print(usedBooks.get(i) + "\n");
            }

            System.out.print("Введите номер книги, которую хотите сдать.\nЕсли подходящих книг нет, введите 0.\n");

            int choice = -1;
            do {
                System.out.print("Ввод: ");
                String temp = scan.nextLine();
                try {
                    choice = Integer.parseInt(temp);
                } catch (NumberFormatException e) {
                    System.out.print("Значение не является цифрой, попробуйте снова.\n");
                }
                if (choice < 0 || choice > usedBooks.size()) {
                    System.out.print("Такого варианта ответа не существует.\n");
                }
            } while (choice < 0 || choice > usedBooks.size());

            if (choice == 0) return;

            for (Book b : bookLinkedList) {
                if (b.equals(usedBooks.get(choice-1))) {
                    b.isPicked = false;
                    b.pickedBy = "none";
                    updateListOfBooks(bookLinkedList);
                    return;
                }
            }

        } else {
            System.out.print("У вас нет книг для сдачи.\n");
        }
    }

    // Вывод всех книг находящихся 'на руках' в пользователя сессии
    void showMyBooks(LinkedList<Book> bookLinkedList, Session session) {
        ArrayList<Book> bookArrayList = new ArrayList<>();
        for (Book b : bookLinkedList) {
            if (b.pickedBy.equals(session.activeUser.login)) {
                bookArrayList.add(b);
            }
        }
        if(bookArrayList.isEmpty()) {
            System.out.print("У вас нет книг.\n");
        } else {
            System.out.print("Ваши книги: \n");
            for (Book b : bookArrayList) System.out.print(b + "\n");
        }
    }
}
