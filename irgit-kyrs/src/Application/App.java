package Application;

import Application.Internal.*;

import java.util.LinkedList;

public class App {
    public void run() {
        // Объявление списков пользователей и книг
        LinkedList<Book> bookLinkedList = new LinkedList<>();
        LinkedList<User> userLinkedList = new LinkedList<>();
        // Запуск сессии
        Session session = new Session(userLinkedList, bookLinkedList);
        // Выбор сценария исходя из должности пользователя в сессии
        if (session.status) {
            switch (session.activeUser.title) {
                case "reader" -> new ReaderOperations(bookLinkedList, session);
                case "admin" -> new AdministrationOperations(userLinkedList);
                case "biblic" -> new BiblicalOperations(userLinkedList, bookLinkedList);
            }
        }
    }
}
