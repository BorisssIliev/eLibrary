package com.example.eLibrary.runner;

import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.BookImage;
import com.example.eLibrary.repository.book.BookImageRepository;
import com.example.eLibrary.repository.book.BookRepository;
import com.example.eLibrary.service.book.BookImageService;
import com.example.eLibrary.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoadBookRunner implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final BookImageRepository bookImageRepository;
    private final BookImageService bookImageService;

    @Override
    public void run(String... args) throws Exception {
        loadBooks();
        setUpBookImages();
    }

    private void loadBooks() {
        List<Book> allBooks = bookRepository.findAll();

        Book book1 = Book.builder().title("1984").author("Джордж Оруел").isbn(UUID.randomUUID().toString()).genre("Дистопия").publicationDate(LocalDate.of(1949, 6, 8)).summary("Роман за дистопично бъдеще, където тоталитарната държава контролира всяка част от живота на хората.").build();
        if (!isBookExists(book1, allBooks)) {
            bookRepository.save(book1);
        }

        Book book2 = Book.builder().title("Престъпление и наказание").author("Фьодор Достоевски").isbn(UUID.randomUUID().toString()).genre("Роман").publicationDate(LocalDate.of(1866, 1, 1)).summary("Историята на беден студент, който извършва убийство и се бори с моралните последствия от деянието си.").build();
        if (!isBookExists(book2, allBooks)) {
            bookRepository.save(book2);
        }

        Book book3 = Book.builder().title("Малкият принц").author("Антоан дьо Сент-Екзюпери").isbn(UUID.randomUUID().toString()).genre("Приказка").publicationDate(LocalDate.of(1943, 4, 6)).summary("Приказка за малкия принц, който пътува през различни планети и научава важни уроци за живота.").build();
        if (!isBookExists(book3, allBooks)) {
            bookRepository.save(book3);
        }

        Book book4 = Book.builder().title("Гордост и предразсъдъци").author("Джейн Остин").isbn(UUID.randomUUID().toString()).genre("Роман").publicationDate(LocalDate.of(1813, 1, 28)).summary("Роман за любовта и социалните предразсъдъци в английското общество през 19 век.").build();
        if (!isBookExists(book4, allBooks)) {
            bookRepository.save(book4);
        }

        Book book5 = Book.builder().title("Властелинът на пръстените: Задругата на пръстена").author("Дж. Р. Р. Толкин").isbn(UUID.randomUUID().toString()).genre("Фентъзи").publicationDate(LocalDate.of(1954, 7, 29)).summary("Първата част от епичното приключение на Фродо и неговите приятели в Средната земя.").build();
        if (!isBookExists(book5, allBooks)) {
            bookRepository.save(book5);
        }

        Book book6 = Book.builder().title("Хари Потър и философският камък").author("Дж. К. Роулинг").isbn(UUID.randomUUID().toString()).genre("Фентъзи").publicationDate(LocalDate.of(1997, 6, 26)).summary("Приключенията на младия магьосник Хари Потър, който открива своята съдба в училището за магия Хогуортс.").build();
        if (!isBookExists(book6, allBooks)) {
            bookRepository.save(book6);
        }

        Book book7 = Book.builder().title("Да убиеш присмехулник").author("Харпър Ли").isbn(UUID.randomUUID().toString()).genre("Роман").publicationDate(LocalDate.of(1960, 7, 11)).summary("История за расизма и несправедливостта в южните щати на САЩ, разказана през очите на младо момиче.").build();
        if (!isBookExists(book7, allBooks)) {
            bookRepository.save(book7);
        }

        Book book8 = Book.builder().title("Пътеводител на галактическия стопаджия").author("Дъглас Адамс").isbn(UUID.randomUUID().toString()).genre("Научна фантастика").publicationDate(LocalDate.of(1979, 10, 12)).summary("Забавна научнофантастична история за приключенията на Артър Дент в космоса.").build();
        if (!isBookExists(book8, allBooks)) {
            bookRepository.save(book8);
        }

        Book book9 = Book.builder().title("Граф Монте Кристо").author("Александър Дюма").isbn(UUID.randomUUID().toString()).genre("Приключенски").publicationDate(LocalDate.of(1844, 1, 1)).summary("Историята на Едмон Дантес и неговото отмъщение срещу тези, които са го предали.").build();
        if (!isBookExists(book9, allBooks)) {
            bookRepository.save(book9);
        }

        Book book10 = Book.builder().title("Игра на тронове").author("Джордж Р. Р. Мартин").isbn(UUID.randomUUID().toString()).genre("Фентъзи").publicationDate(LocalDate.of(1996, 8, 6)).summary("Първата книга от поредицата 'Песен за огън и лед', разказваща за борбата за власт в Седемте кралства.").build();
        if (!isBookExists(book10, allBooks)) {
            bookRepository.save(book10);
        }

        Book book11 = Book.builder().title("Шифърът на Леонардо").author("Дан Браун").isbn(UUID.randomUUID().toString()).genre("Трилър").publicationDate(LocalDate.of(2003, 3, 18)).summary("Трилър за мистериозни символи и тайни общества, които водят до вълнуващо приключение.").build();
        if (!isBookExists(book11, allBooks)) {
            bookRepository.save(book11);
        }
    }

    private boolean isBookExists(Book book, List<Book> books) {
        for (Book currentBook : books) {
            if (currentBook.getTitle().equalsIgnoreCase(book.getTitle())
                    && currentBook.getAuthor().equalsIgnoreCase(book.getAuthor())) {
                return true;
            }
        }
        return false;
    }

    private void setUpBookImages() {
        List<Book> books = bookRepository.findAll();
        String[] imageFiles = {
                "1984.png", "prestyplenie.jpg", "malkiqt.jpg", "gordost.jpg",
                "prustena.jpg", "hari.jpg", "da-ubiesh-prismehulnik.jpg",
                "galakti4eskiq.jpg", "graf-monte-kristo.jpg", "igra-na-tronove.jpg",
                "shifura.jpg"
        };

        for (int i = 0; i < books.size() && i < imageFiles.length; i++) {
            Book book = books.get(i);
            String imageFileName = imageFiles[i];

            // Зареждаме изображение за книгата
            try {
                BookImage bookImage = loadImageForBook(imageFileName);
                bookImage.setBook(book);
                bookImageRepository.save(bookImage);
                System.out.println("Книга със снимка: " + book.getTitle() + " - " + bookImage.getImageName());
            } catch (IOException e) {
                System.err.println("Грешка при зареждането на изображението за книга: " + book.getTitle() + " - " + e.getMessage());
            }
        }
    }

    private BookImage loadImageForBook(String imageFileName) throws IOException {
        // Проверка дали съществува изображение със същата стойност
        byte[] imageValue = Files.readAllBytes(Path.of("src/main/resources/static/images/" + imageFileName));
        Optional<BookImage> existingBookImage = bookImageRepository.findByValue(imageValue);

        if (existingBookImage.isPresent()) {
            // Ако изображението съществува, използваме съществуващото изображение
            return existingBookImage.get();
        } else {
            // Създаване и запазване на ново изображение
            String uniqueImageName = imageFileName + "_" + UUID.randomUUID();
            BookImage bookImage = BookImage.builder()
                    .value(imageValue)
                    .imageName(uniqueImageName)
                    .build();
            bookImageRepository.save(bookImage);
            return bookImage;
        }
    }
}

