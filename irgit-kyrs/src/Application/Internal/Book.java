package Application.Internal;

// Структура книги
public class Book {
    // Поля структуры
    String name;
    String author;
    String storageCipher;
    String ISBN;
    boolean isPicked;
    String pickedBy;

    // Перегрузка стандартной функции для структуры Book
    @Override
    public String toString() {
        return String.format(
            """
            Название: %s
            Автор(ы): %s
            Складской шифр: %s
            ISBN: %s
            Статус: %s
            """,
            name,
            author,
            storageCipher,
            ISBN,
            isPicked ? String.format("В пользовании у %s", pickedBy) : "На складе"
        );
    }
}

