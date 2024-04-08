package Application.Internal;

import java.util.LinkedList;
import java.util.Scanner;

public class AdministrationOperations extends Additional {
    // Сценарий работы администратора
    public AdministrationOperations(LinkedList<User> userLinkedList) {
        Scanner scan = new Scanner(System.in);
        boolean looper = true;
        do {
            adminMenu();
            System.out.print("Ввод: ");
            String switcher = scan.nextLine();
            switch(switcher) {
                case "1" -> addNewUser(userLinkedList);
                case "2" -> removeUser(userLinkedList);
                case "3" -> changeUserStats(userLinkedList);
                case "0" -> looper = false;
                default -> System.out.print("Значение не существует.");
            }
        } while (looper);
    }

    // Меню администратора
    void adminMenu() {
        System.out.print(
            """
            1 - Добавить пользователя
            2 - Удалить пользователя
            3 - Изменить пользователя
            0 - Выйти
            """
        );
    }

    // Добавление нового пользователя
    void addNewUser(LinkedList<User> userLinkedList) {
        Scanner scan = new Scanner(System.in);
        User u = new User();
        boolean looper;
        do {
            System.out.print("Введите имя нового пользователя: ");
            u.login = scan.nextLine();
            if(isUsernameReal(userLinkedList, u.login) || u.login.equals("none")) {
                looper = false;
                System.out.print("Пользователь с таким логином уже существует.\n");
            } else {
                looper = true;
            }
        } while(!looper);

        System.out.print("Введите пароль пользователя: ");
        u.password = encrypt(scan.nextLine(), superSecretKey);

        do {
            System.out.print("Введите должность: ");
            u.title = scan.nextLine();
            if(!(u.title.equals("admin") || u.title.equals("reader") || u.title.equals("biblic"))) {
                looper = false;
                System.out.print("Данная должность еще не существует.\n");
            } else {
                looper = true;
            }
        } while(!looper);

        userLinkedList.add(u);
        updateListOfUsers(userLinkedList);
    }

    // Удаление существующего пользователя
    void removeUser(LinkedList<User> userLinkedList) {
        Scanner scan = new Scanner(System.in);
        String login;
        boolean looper = true;

        do {
            System.out.print("Введите имя пользователя для удаления: ");
            login = scan.nextLine();
            if(!isUsernameReal(userLinkedList, login)) {
                looper = false;
                System.out.print("Пользователь с таким логином не существует.\n");
            }
        } while(!looper);

        for (int i = 0; i < userLinkedList.size(); i++) {
            if (userLinkedList.get(i).login.equals(login)) {
                userLinkedList.remove(i);
                System.out.print("Пользователь успешно удален.\n");
                updateListOfUsers(userLinkedList);
                return;
            }
        }
    }

    // Меню изменения пользовательских данных
    void changeUserMenu() {
        System.out.print(
            """
            1 - Изменить логин
            2 - Изменить пароль
            3 - Изменить должность
            0 - Выйти
            """
        );
    }

    // Функция изменения пользовательских данных
    void changeUserStats(LinkedList<User> userLinkedList) {
        Scanner scan = new Scanner(System.in);
        int choice = 0;
        String login;
        boolean looper = true;

        do {
            System.out.print("Введите имя пользователя для изменения: ");
            login = scan.nextLine();
            if(!isUsernameReal(userLinkedList, login)) {
                looper = false;
                System.out.print("Пользователь с таким логином не существует.\n");
            }
        } while(!looper);

        for (int i = 0; i < userLinkedList.size(); i ++ ) {
            if (login.equals(userLinkedList.get(i).login)) {
                choice = i;
            }
        }

        do {
            changeUserMenu();
            System.out.print("Ввод: ");
            String switcher = scan.nextLine();
            switch(switcher) {
                case "1" -> {
                    do {
                        System.out.print("Введите новое имя пользователя: ");
                        String newLogin = scan.nextLine();
                        if(isUsernameReal(userLinkedList, newLogin) || newLogin.equals("none")) {
                            looper = false;
                            System.out.print("Пользователь с таким логином уже существует.\n");
                        }
                        userLinkedList.get(choice).login = newLogin;
                    } while(!looper);
                    updateListOfUsers(userLinkedList);
                }
                case "2" -> {
                    System.out.print("Введите новый пароль: ");
                    userLinkedList.get(choice).password = encrypt(scan.nextLine(), superSecretKey);
                    updateListOfUsers(userLinkedList);
                }
                case "3" -> {
                    String newTitle;
                    do {
                        System.out.print("Введите новую должность: ");
                        newTitle = scan.nextLine();
                        if(!(newTitle.equals("admin") || newTitle.equals("reader") || newTitle.equals("biblic"))) {
                            looper = false;
                            System.out.print("Данная должность еще не существует.\n");
                        }
                    } while(!looper);
                    userLinkedList.get(choice).title = newTitle;
                    updateListOfUsers(userLinkedList);
                }
                case "0" -> looper = false;
            }
        }while(looper);
    }
}
