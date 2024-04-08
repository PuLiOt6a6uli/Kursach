package Application.Internal;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

public class Session extends Additional {
    // Поля сессии
    public boolean status = false;
    public User activeUser = null;

    // Запуск сценария
    public Session(LinkedList<User> userLinkedList, LinkedList<Book> bookLinkedList) {
        fillListOfBooks(bookLinkedList);
        fillListOfUsers(userLinkedList);
        enter(userLinkedList);
    }

    // Меню входа
    void logMenu() {
        System.out.print(
            """
            1 - Логин
            2 - Зарегистрироваться
            0 - Выйти
            """
        );
    }

    // Меню входа
    void enter(LinkedList<User> userLinkedList) {
        Scanner scan = new Scanner(System.in);
        boolean looper = false;

        do {
            logMenu();
            System.out.print("Ввод: ");
            String switcher = scan.nextLine();
            switch(switcher) {
                case "1" -> {
                    if(!status) {
                        login(userLinkedList);
                    } else {
                        looper = true;
                    }
                }
                case "2" -> {
                    if(!status) {
                        register(userLinkedList);
                    } else {
                        looper = true;
                    }
                }
                case "0" -> looper = false;
            }
        } while(looper);
    }

    // Вход по данным
    void login(LinkedList<User> userLinkedList) {
        do {
            Scanner scan = new Scanner(System.in);
            System.out.print("Введите логин: ");
            String login = scan.nextLine();
            System.out.print("Введите пароль: ");

            String passwd = scan.nextLine();
            for(User user : userLinkedList) {
                if (Objects.equals(user.login, login) && Objects.equals(decrypt(user.password, superSecretKey), passwd)) {
                    System.out.print("Вход выполнен успешно!\n");
                    activeUser = user;
                    status = true;
                    break;
                }
            }
            if (!status) {
                System.out.print("Неверный пароль или логин! Попробовать снова? (Y/N): ");
                String choice = validateYN(scan.nextLine());
                if(Objects.equals(choice.toLowerCase(), "n")) return;
            }
        }while (!status);
    }

    // Регистрация
    void register(LinkedList<User> userLinkedList) {
        Scanner scan = new Scanner(System.in);
        String login;
        boolean looper;
        do {
            looper = true;
            System.out.print("Введите логин: ");
            login = scan.nextLine();
            if(isUsernameReal(userLinkedList, login) || login.equals("none")) {
                looper = false;
                System.out.print("Пользователь с таким логином уже существует.");
            }
        } while(!looper);

        System.out.print("Введите пароль: ");
        // Шифрование пароля
        String passwd = encrypt(scan.nextLine(), superSecretKey);
        User user = new User();
        user.login = login;
        user.password = passwd;
        user.title = "reader";
        userLinkedList.add(user);
        activeUser = user;
        status = true;
        updateListOfUsers(userLinkedList);
        System.out.print("Учетная запись успешно создана!\n");
    }
}
