package com.example.eLibrary.runner;

import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.BookImage;
import com.example.eLibrary.service.book.BookImageService;
import com.example.eLibrary.service.book.BookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoadBookRunner implements CommandLineRunner {

    private final BookService bookService;
    private final BookImageService bookImageService;

    @Override
    public void run(String... args) throws Exception {
        loadBooks();
        setUpBookImages();
    }

    private void loadBooks() {
        List<Book> allBooks = bookService.getAllBooks();

        String[] titles = {
                "1984", "Престъпление и наказание", "Малкият принц", "Гордост и предразсъдъци",
                "Властелинът на пръстените: Задругата на пръстена", "Хари Потър и философският камък",
                "Да убиеш присмехулник", "Пътеводител на галактическия стопаджия", "Граф Монте Кристо",
                "Игра на тронове", "Шифърът на Леонардо"
        };

        String[] authors = {
                "Джордж Оруел", "Фьодор Достоевски", "Антоан дьо Сент-Екзюпери", "Джейн Остин",
                "Дж. Р. Р. Толкин", "Дж. К. Роулинг", "Харпър Ли", "Дъглас Адамс", "Александър Дюма",
                "Джордж Р. Р. Мартин", "Дан Браун"
        };

        String[] genres = {
                "Дистопия", "Роман", "Приказка", "Роман", "Фентъзи", "Фентъзи", "Роман", "Научна фантастика",
                "Приключенски", "Фентъзи", "Трилър"
        };

        LocalDate[] dates = {
                LocalDate.of(1949, 6, 8),
                LocalDate.of(1866, 1, 1),
                LocalDate.of(1943, 4, 6),
                LocalDate.of(1813, 1, 28),
                LocalDate.of(1954, 7, 29),
                LocalDate.of(1997, 6, 26),
                LocalDate.of(1960, 7, 11),
                LocalDate.of(1979, 10, 12),
                LocalDate.of(1844, 1, 1),
                LocalDate.of(1996, 8, 6),
                LocalDate.of(2003, 3, 18)
        };

        String[] summaries = {
                "Роман за дистопично бъдеще...",
                "История за морални дилеми.",
                "Приказка за малкия принц...",
                "Любов и социални предразсъдъци.",
                "Епично приключение в Средната земя.",
                "Хари открива магическия свят.",
                "История за расизма в южните щати.",
                "Забавна история в космоса.",
                "Отмъщението на Едмон Дантес.",
                "Борба за власт в Седемте кралства.",
                "Мистерия с тайни общества."
        };

        for (int i = 0; i < titles.length; i++) {
            Book book = Book.builder()
                    .title(titles[i])
                    .author(authors[i])
                    .isbn(UUID.randomUUID().toString())
                    .genre(genres[i])
                    .publicationDate(dates[i])
                    .summary(summaries[i])
                    .build();

            if (!bookService.existsByTitleAndAuthor(book.getTitle(), book.getAuthor())) {
                bookService.saveBook(book);
            }
        }
    }

    @Transactional
    private void setUpBookImages() {
        List<Book> books = bookService.getAllBooks();
        String[] imageFiles = {
                "1984.png", "prestyplenie.jpg", "malkiqt.jpg", "gordost.jpg",
                "prustena.jpg", "hari.jpg", "da-ubiesh-prismehulnik.jpg",
                "galakti4eskiq.jpg", "graf-monte-kristo.jpg", "igra-na-tronove.jpg",
                "shifura.jpg"
        };

        Path uploadDir = Path.of("uploads");
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
        } catch (IOException e) {
            System.err.println("❌ Неуспешно създаване на uploads папка: " + e.getMessage());
        }

        for (int i = 0; i < books.size() && i < imageFiles.length; i++) {
            Book book = books.get(i);

            // ✅ Пропускаме ако книгата вече има снимка
            if (book.getBookImages() != null && !book.getBookImages().isEmpty()) {
                System.out.println("⚠️ Книгата \"" + book.getTitle() + "\" вече има снимка. Пропускаме.");
                continue;
            }

            String originalFileName = imageFiles[i];
            String newFileName = UUID.randomUUID() + "_" + originalFileName;

            try {
                Path sourcePath = Path.of("src/main/resources/static/images", originalFileName);
                Path targetPath = Path.of("uploads", newFileName);
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

                BookImage bookImage = BookImage.builder()
                        .imageName(newFileName)
                        .book(book)
                        .build();

                bookImageService.saveBookImage(bookImage);
                System.out.println("📚 Снимка добавена: " + book.getTitle());
            } catch (IOException e) {
                System.err.println("❌ Грешка при зареждане на снимка за " + book.getTitle() + ": " + e.getMessage());
            }
        }
    }
}
