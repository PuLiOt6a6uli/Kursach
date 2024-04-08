package Application.Internal;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Additional {

    // Ключи шифрования и алгоритм шифрования
    final String superSecretKey = "f3c6f3b515af37c929c1946700c27dcb264e9d83ceb13be1c7d76f1c9c62694b";
    private SecretKeySpec secretKey;
    private final String ALGORITHM = "AES";

    // Функция зашифровывания
    public String encrypt(String strToEncrypt, String secret) {
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.printf("Ошибка шифрования: %s", e);
        }
        return null;
    }

    // Функция расшифровывания
    public String decrypt(String strToDecrypt, String secret) {
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.printf("Ошибка шифрования: %s", e);
        }
        return null;
    }

    // Подготовка секретного ключа
    public void prepareSecreteKey(String myKey) {
        MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Заполнение списка книг из файла
    void fillListOfBooks(LinkedList<Book> bookLinkedList) {
        File file = new File("public/books.txt");
        try (Scanner fl = new Scanner(file)) {
            while (fl.hasNextLine()) {
                Book tempBook = new Book();
                tempBook.name = fl.nextLine();
                tempBook.author = fl.nextLine();
                tempBook.storageCipher = fl.nextLine();
                tempBook.ISBN = fl.nextLine();
                tempBook.isPicked = fl.nextLine().equals("true");
                tempBook.pickedBy = fl.nextLine();
                fl.nextLine();
                bookLinkedList.add(tempBook);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Обновление хранилища списка книг
    void updateListOfBooks(LinkedList<Book> bookLinkedList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("public/books.txt"))) {
            for (Book b : bookLinkedList) {
                writer.write(b.name);
                writer.newLine();
                writer.write(b.author);
                writer.newLine();
                writer.write(b.storageCipher);
                writer.newLine();
                writer.write(b.ISBN);
                writer.newLine();
                writer.write(b.isPicked ? "true" : "false");
                writer.newLine();
                writer.write(b.pickedBy);
                writer.newLine();
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Заполнение списка пользователей из файла
    void fillListOfUsers(LinkedList<User> userLinkedList) {
        File file = new File("public/users.txt");
        try (Scanner fl = new Scanner(file)) {
            while (fl.hasNextLine()) {
                User tempUser = new User();
                tempUser.login = fl.nextLine();
                tempUser.password = fl.nextLine();
                tempUser.title = fl.nextLine();
                fl.nextLine();
                userLinkedList.add(tempUser);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Обновление хранилища списка пользователей
    void updateListOfUsers(LinkedList<User> userLinkedList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("public/users.txt"))) {
            for (User user : userLinkedList) {
                for (String s : Arrays.asList(user.login, user.password, user.title)) {
                    writer.write(s);
                    writer.newLine();
                }
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Проверка существует ли такое имя пользователя
    boolean isUsernameReal(LinkedList<User> listOfUsers, String login) {
        for (User user : listOfUsers) {
            if (Objects.equals(user.login, login)) return true;
        }
        return false;
    }

    // Проверка на совпадение y/n
    String validateYN(String str) {
        Scanner scan = new Scanner(System.in);
        while(!str.equalsIgnoreCase("n") && !str.equalsIgnoreCase("y")) {
            System.out.print("Введенное значение некорректно! Попробуйте снова.\nВвести: ");
            str = scan.nextLine();
        }
        return str;
    }
}