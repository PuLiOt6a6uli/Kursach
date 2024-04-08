package Application.Internal;

// Структура пользователя
public class User {
    // Поля структуры
    public String login;
    String password;
    public String title;

    // Перегрузка стандартной функции для структуры User
    @Override
    public String toString() {
        return String.format(
            """
            Логин: %s
            Статус: %s
            """,
            login,
            title
        );
    }
}
