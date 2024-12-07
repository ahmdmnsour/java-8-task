package org.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.*;
import java.util.stream.Stream;

public class StreamsExample {

    public static void main(final String[] args) {

        List<Author> authors = Library.getAuthors();
        
        banner("Authors information");
        // SOLVED With functional interfaces declared
        Consumer<Author> authorPrintConsumer = new Consumer<Author>() {
            @Override
            public void accept(Author author) {
                System.out.println(author);
            }
        };
        authors
            .stream()
            .forEach(authorPrintConsumer);

        // SOLVED With functional interfaces used directly
        authors
            .stream()
            .forEach(System.out::println);

        banner("Active authors");
        // DONE With functional interfaces declared
        Predicate<Author> activeAuthorsPredicate = new Predicate<Author>() {
            @Override
            public boolean test(Author author) {
                return author.active;
            }
        };
        authors.stream().filter(activeAuthorsPredicate).forEach(System.out::println);

        banner("Active authors - lambda");
        // DONE With functional interfaces used directly
        authors.stream().filter(author -> author.active).forEach(System.out::println);

        banner("Active books for all authors");
        // DONE With functional interfaces declared
        Predicate<Book> publishedBooksPredicate = new Predicate<Book>() {
            @Override
            public boolean test(Book book) {
                return book.published;
            }
        };
        authors.forEach(author -> {
            author.books
                    .stream()
                    .filter(publishedBooksPredicate)
                    .forEach(System.out::println);
        });

        banner("Active books for all authors - lambda");
        // DONE With functional interfaces used directly
        authors.forEach(author -> {
            author.books.stream().filter(book -> book.published).forEach(System.out::println);
        });

        banner("Average price for all books in the library");
        // DONE With functional interfaces declared
        Function<Author, Stream<Book>> booksStream = new Function<Author, Stream<Book>>() {
            @Override
            public Stream<Book> apply(Author author) {
                return author.books.stream();
            }
        };
        ToDoubleFunction<Book> priceStream = new ToDoubleFunction<Book>() {
            @Override
            public double applyAsDouble(Book book) {
                return book.price;
            }
        };
        authors
                .stream()
                .flatMap(booksStream)
                .mapToDouble(priceStream)
                .average()
                .ifPresent(System.out::println);

        banner("Average price for all books in the library - lambda");
        // DONE With functional interfaces used directly
        authors
                .stream()
                .flatMap(author -> author.books.stream())
                .mapToDouble(book -> book.price)
                .average()
                .ifPresent(System.out::println);

        banner("Active authors that have at least one published book");
        // DONE With functional interfaces declared
        Predicate<Author> hasPublishedBooksPredicate = new Predicate<Author>() {
            @Override
            public boolean test(Author author) {
                return author.books.stream().anyMatch(publishedBooksPredicate);
            }
        };
        authors
                .stream()
                .filter(activeAuthorsPredicate)
                .filter(hasPublishedBooksPredicate)
                .forEach(System.out::println);

        banner("Active authors that have at least one published book - lambda");
        // DONE With functional interfaces used directly
        authors
                .stream()
                .filter(author -> author.active)
                .filter(author ->
                        author.books
                            .stream()
                            .anyMatch(book -> book.published))
                .forEach(System.out::println);

    }

    private static void banner(final String m) {
        System.out.println("#### " + m + " ####");
    }
}


class Library {
    public static List<Author> getAuthors() {
        return Arrays.asList(
            new Author("Author A", true, Arrays.asList(
                new Book("A1", 100, true),
                new Book("A2", 200, true),
                new Book("A3", 220, true))),
            new Author("Author B", true, Arrays.asList(
                new Book("B1", 80, true),
                new Book("B2", 80, false),
                new Book("B3", 190, true),
                new Book("B4", 210, true))),
            new Author("Author C", true, Arrays.asList(
                new Book("C1", 110, true),
                new Book("C2", 120, false),
                new Book("C3", 130, true))),
            new Author("Author D", false, Arrays.asList(
                new Book("D1", 200, true),
                new Book("D2", 300, false))),
            new Author("Author X", true, Collections.emptyList()));
    }
}

class Author {
    String name;
    boolean active;
    List<Book> books;

    Author(String name, boolean active, List<Book> books) {
        this.name = name;
        this.active = active;
        this.books = books;
    }

    @Override
    public String toString() {
        return name + "\t| " + (active ? "Active" : "Inactive");
    }
}

class Book {
    String name;
    int price;
    boolean published;

    Book(String name, int price, boolean published) {
        this.name = name;
        this.price = price;
        this.published = published;
    }

    @Override
    public String toString() {
        return name + "\t| " + "\t| $" + price + "\t| " + (published ? "Published" : "Unpublished");
    }
}
