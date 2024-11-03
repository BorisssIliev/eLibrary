package com.example.eLibrary.runner;

import com.example.eLibrary.entity.book.Book;
import com.example.eLibrary.entity.book.BookImage;
import com.example.eLibrary.service.book.BookImageService;
import com.example.eLibrary.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoadBookRunner implements CommandLineRunner {

    private final BookService bookService;
    private final BookImageService bookImageService;

    @Override
    public void run(String... args) throws Exception {
        loadBooks();
    }

    private void loadBooks() {
        List<Book> books = new ArrayList<>();

        books.add(createBook("1984", "Джордж Оруел", "Антиутопия", "9780141036144", LocalDate.of(1949, 6, 8), "1984.jpg"));
        books.add(createBook("Престъпление и наказание", "Фьодор Достоевски", "Класическа литература", "9780140449136", LocalDate.of(1866, 1, 1), "prestuplenie.jpg"));
        books.add(createBook("Малкият принц", "Антоан дьо Сент-Екзюпери", "Философска приказка", "9780156012195", LocalDate.of(1943, 4, 6), "lepetitprince.jpg"));
        books.add(createBook("Гордост и предразсъдъци", "Джейн Остин", "Романтика, Класическа литература", "9780141199078", LocalDate.of(1813, 1, 28), "prideandprejudice.jpg"));
        books.add(createBook("Властелинът на пръстените: Задругата на пръстена", "Дж. Р. Р. Толкин", "Фентъзи", "9780261102354", LocalDate.of(1954, 7, 29), "fellowship.jpg"));
        books.add(createBook("Хари Потър и философският камък", "Дж. К. Роулинг", "Фентъзи", "9780747532743", LocalDate.of(1997, 6, 26), "harrypotter.jpg"));
        books.add(createBook("Да убиеш присмехулник", "Харпър Ли", "Драма, Социална тематика", "9780061120084", LocalDate.of(1960, 7, 11), "tokillamockingbird.jpg"));
        books.add(createBook("Пътеводител на галактическия стопаджия", "Дъглас Адамс", "Научна фантастика, Хумор", "9780345391803", LocalDate.of(1979, 10, 12), "hitchhiker.jpg"));
        books.add(createBook("Граф Монте Кристо", "Александър Дюма", "Приключенски роман", "9780140449266", LocalDate.of(1844, 8, 28), "montecristo.jpg"));
        books.add(createBook("Игра на тронове", "Джордж Р. Р. Мартин", "Фентъзи", "9780553103540", LocalDate.of(1996, 8, 6), "gameofthrones.jpg"));
        books.add(createBook("Шифърът на Леонардо", "Дан Браун", "Трилър, Мистерия", "9780307474278", LocalDate.of(2003, 3, 18), "davincicode.jpg"));

        for (Book book : books) {
            if (!bookService.bookExistsByIsbn(book.getIsbn())) {
                bookService.saveBook(book);
            }
        }
    }

    private Book createBook(String title, String author, String genre, String isbn, LocalDate publicationDate, String imageName) {
        BookImage bookImage = BookImage.builder()
                .imageName(imageName)
                .value(loadImage(imageName))
                .build();

        bookImageService.saveBookImage(bookImage);

        return Book.builder()
                .title(title)
                .author(author)
                .genre(genre)
                .isbn(isbn)
                .publicationDate(publicationDate)
                .summary("Резюме на книгата " + title)
                .bookImages(List.of(bookImage))
                .build();
    }

    private byte[] loadImage(String imageName) {
        try {
            // Зареждане на изображението от ресурсната папка
            return this.getClass().getClassLoader().getResourceAsStream("images/" + imageName).readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Неуспешно зареждане на изображението: " + imageName, e);
        }
    }
}
