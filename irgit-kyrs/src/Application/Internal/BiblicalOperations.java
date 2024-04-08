package Application.Internal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Scanner;

public class BiblicalOperations extends Additional {
    // Запуск сценария библиотекаря
    public BiblicalOperations(LinkedList<User> userLinkedList, LinkedList<Book> bookLinkedList) {
        Scanner scan = new Scanner(System.in);
        boolean looper = true;
        do {
            biblicalMenu();
            System.out.print("Ввод: ");
            String switcher = scan.nextLine();
            switch(switcher) {
                case "1" -> makeBookReport(bookLinkedList, 1);
                case "2" -> makeBookReport(bookLinkedList, 2);
                case "3" -> makeBookReport(bookLinkedList, 3);
                case "4" -> showUsersWithBooks(userLinkedList, bookLinkedList);
                case "5" -> addNewBook(bookLinkedList, userLinkedList);
                case "6" -> deleteBook(bookLinkedList);
                case "7" -> changeBook(bookLinkedList, userLinkedList);
                case "0" -> looper = false;
                default -> throw new IllegalStateException("Unexpected value: " + switcher);
            }
        } while (looper);
    }

    // Меню библиотекаря
    void biblicalMenu() {
        System.out.print(
            """
            1 - Создать отчет по всем книгам
            2 - Создать отчет по книгам у читателей
            3 - Создать отчет по книгам со склада
            4 - Просмотреть читателей с книгами
            5 - Добавить новую книгу
            6 - Удалить существующую книгу
            7 - Изменить статус книги
            0 - Выйти
            """
        );
    }

    // Меню изменений характеристик пользователя
    void changeMenu() {
        System.out.print(
            """
            1 - Изменить название
            2 - Изменить автора
            3 - Изменить складской номер
            4 - Изменить ISBN
            5 - Изменить статус
            0 - Выйти
            """
        );
    }

    // Изменение полей книги
    void changeBook(LinkedList<Book> bookLinkedList, LinkedList<User> userLinkedList) {
        Scanner scan = new Scanner(System.in);
        boolean looper = true;

        for (int i = 0; i < bookLinkedList.size(); i++) {
            System.out.printf("Номер: %d\n", i+1);
            System.out.printf("%s\n", bookLinkedList.get(i));
        }

        System.out.print("Введите номер книги, которую хотите изменить.\nЕсли подходящих книг нет, введите 0\n");

        int choice = -1;
        do {
            System.out.print("Ввод: ");
            String temp = scan.nextLine();
            try {
                choice = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                System.out.print("Значение не является цифрой, попробуйте снова.\n");
            }
            if (choice < 0 || choice > bookLinkedList.size()) {
                System.out.print("Такого варианта ответа не существует.\n");
            }
        } while (choice < 0 || choice > bookLinkedList.size());

        if (choice == 0) return;

        do {
            changeMenu();
            System.out.print("Ввод: ");
            String switcher = scan.nextLine();
            switch (switcher) {
                case "1" -> {
                    System.out.print("Введите новое значение: ");
                    bookLinkedList.get(choice-1).name = scan.nextLine();
                }
                case "2" -> {
                    System.out.print("Введите новое значение: ");
                    bookLinkedList.get(choice-1).author = scan.nextLine();
                }
                case "3" -> {
                    System.out.print("Введите введите новое значение: ");
                    bookLinkedList.get(choice-1).storageCipher = scan.nextLine();
                }
                case "4" -> {
                    System.out.print("Введите новое значение: ");
                    bookLinkedList.get(choice-1).ISBN = scan.nextLine();
                }
                case "5" -> {
                    System.out.print("Имя нового владельца книги, или введите 'none' если книга на складе.\n");
                    boolean subLooper = true;
                    do {
                        System.out.print("Введите у кого находится книга: ");
                        bookLinkedList.get(choice-1).pickedBy = scan.nextLine();
                        if (isUsernameReal(userLinkedList, bookLinkedList.get(choice-1).pickedBy) || bookLinkedList.get(choice-1).pickedBy.equals("none")) {
                            subLooper = false;
                        }
                    } while(subLooper);
                    bookLinkedList.get(choice-1).isPicked = bookLinkedList.get(choice-1).pickedBy.equals("none");

                }
                case "0" -> looper = false;
                default -> System.out.print("Данного значения нет.");
            }
        } while(looper);
        updateListOfBooks(bookLinkedList);
    }

    // Вывод всех пользователей с книгами
    void showUsersWithBooks(LinkedList<User> userLinkedList, LinkedList<Book> bookLinkedList) {
        for (User u : userLinkedList) {
            int counter = 0;
            for (Book b : bookLinkedList) {
                if(u.login.equals(b.pickedBy)) counter++;
            }
            if (counter != 0) {
                System.out.printf("Книг у пользователя %s: %d\n", u.login, counter );
            }
        }
    }

    // Удаление книги
    void deleteBook(LinkedList<Book> bookLinkedList) {
        Scanner scan = new Scanner(System.in);

        for (int i = 0; i < bookLinkedList.size(); i++) {
            System.out.printf("Номер: %d\n", i+1);
            System.out.printf("%s\n", bookLinkedList.get(i));
        }
        System.out.print("Введите номер книги, которую хотите удалить.\nЕсли подходящих книг нет, введите 0\n");

        int choice = -1;
        do {
            System.out.print("Ввод: ");
            String temp = scan.nextLine();
            try {
                choice = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                System.out.print("Значение не является цифрой, попробуйте снова.\n");
            }
            if (choice < 0 || choice > bookLinkedList.size()) {
                System.out.print("Такого варианта ответа не существует.\n");
            }
        } while (choice < 0 || choice > bookLinkedList.size());

        if (choice == 0) return;

        bookLinkedList.remove(choice - 1);
        System.out.print("Книга удалена!\n");
    }

    // Добавление новой книги
    void addNewBook(LinkedList<Book> bookLinkedList, LinkedList<User> userLinkedList) {
        Scanner scan = new Scanner(System.in);
        Book nB = new Book();

        System.out.print("Введите название книги: ");
        nB.name = scan.nextLine();
        System.out.print("Введите авторов книги: ");
        nB.author = scan.nextLine();
        System.out.print("Введите складской шифр: ");
        nB.storageCipher = scan.nextLine();
        System.out.print("Введите ISBN: ");
        nB.ISBN = scan.nextLine();

        boolean looper = true;
        do {
            System.out.print("Введите у кого находится книга: ");
            nB.pickedBy = scan.nextLine();
            if (isUsernameReal(userLinkedList, nB.pickedBy) || nB.pickedBy.equals("none")) {
                looper = false;
            }
        } while(looper);
        nB.isPicked = !nB.pickedBy.equals("none");
        bookLinkedList.add(nB);
        updateListOfBooks(bookLinkedList);
    }

    // Создание отчета по всем книгам
    void makeBookReport(LinkedList<Book> bookLinkedList, int reportType) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        LocalDate now = LocalDate.now();
        try {
            File file = new File(String.format("public/reports/book-report-%s-type-%d", dtf.format(now), reportType));
            boolean makeFile = file.createNewFile();
            if (!makeFile) {
                System.out.print("Файл уже существует...\n");
            } else {
                System.out.print("Создаем файл...\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (reportType) {
            case 1 -> {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("public/reports/book-report-%s-type-%d", dtf.format(now), reportType)))) {
                    System.out.print("Записываем данные в файл...\n");
                    for (Book b : bookLinkedList) {
                        writer.write(b.toString());
                        writer.newLine();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case 2 -> {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("public/reports/book-report-%s-type-%d", dtf.format(now), reportType)))) {
                    System.out.print("Записываем данные в файл...\n");
                    for (Book b : bookLinkedList) {
                        if (b.isPicked) {
                            writer.write(b.toString());
                            writer.newLine();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case 3 -> {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("public/reports/book-report-%s-type-%d", dtf.format(now), reportType)))) {
                    System.out.print("Записываем данные в файл...\n");
                    for (Book b : bookLinkedList) {
                        if (!b.isPicked) {
                            writer.write(b.toString());
                            writer.newLine();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}