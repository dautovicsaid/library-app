package me.fit.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(name = Author.GET_ALL_AUTHORS, query = "SELECT a FROM Author a"),
        @NamedQuery(name = Author.GET_AUTHOR_BY_NAME, query = "SELECT a FROM Author a WHERE a.name LIKE :name"),
        @NamedQuery(name = Author.GET_AUTHOR_BY_ID, query = "SELECT a FROM Author a WHERE a.id = :id"),
        @NamedQuery(name = Author.CHECK_AUTHOR_EXISTS, query = "SELECT COUNT(a) FROM Author a WHERE a.name = :name AND a.id <> :id")
})
public class Author {

    public static final String GET_ALL_AUTHORS = "getAllAuthors";
    public static final String GET_AUTHOR_BY_NAME = "getAuthorByName";
    public static final String GET_AUTHOR_BY_ID = "getAuthorById";
    public static final String CHECK_AUTHOR_EXISTS = "checkAuthorExists";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_seq")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Book> books;

    public Author() {
        super();
    }

    public Author(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Author(String name) {
        super();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Author other = (Author) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }
}
