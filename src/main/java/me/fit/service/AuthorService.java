package me.fit.service;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import me.fit.enums.AuthorStatus;
import me.fit.exception.AuthorException;
import me.fit.model.Author;
import me.fit.model.Book;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Dependent
public class AuthorService {

    @Inject
    private EntityManager em;

    @Transactional
    public Set<Author> getAllAuthors() {
        return em.createNamedQuery(Author.GET_ALL_AUTHORS, Author.class)
                .getResultStream()
                .peek(this::loadAuthorBooks)
                .collect(Collectors.toSet());
    }

    @Transactional
    public Set<Author> getAuthorsByName(String name) {
        return em.createNamedQuery(Author.GET_AUTHOR_BY_NAME, Author.class)
                .setParameter("name", "%" + name + "%")
                .getResultStream()
                .peek(this::loadAuthorBooks)
                .collect(Collectors.toSet());
    }

    @Transactional
    public Author createAuthor(Author author) throws AuthorException {
        validateAuthorDoesNotExist(author);

        return em.merge(author);
    }

    @Transactional
    public Author getAuthorById(Long id) {
        return loadAuthorBooks(
                em.createNamedQuery(Author.GET_AUTHOR_BY_ID, Author.class)
                        .setParameter("id", id)
                        .getSingleResult());
    }

    @Transactional
    public void deleteAuthor(Long id) {
        Author author = em.find(Author.class, id);
        if (author != null) {
            em.remove(author);
        }
    }

    @Transactional
    public Author updateAuthor(Author author) throws AuthorException {
        validateAuthorDoesNotExist(author);
        return em.merge(author);
    }

    private Author loadAuthorBooks(Author author) {
        Set<Book> books = new HashSet<>(em.createNamedQuery(Book.GET_ALL_FOR_AUTHOR, Book.class)
                .setParameter("authorId", author.getId())
                .getResultList());
        author.setBooks(books);
        return author;
    }

    @Transactional
    private boolean authorExists(Author author) {
        System.out.println(author.getId());
        return em.createNamedQuery(Author.CHECK_AUTHOR_EXISTS, Long.class)
                .setParameter("name", author.getName())
                .setParameter("id", author.getId() != null ? author.getId() : -1L)
                .getSingleResult() > 0;
    }

    public void validateAuthorDoesNotExist(Author author) throws AuthorException {
        if (authorExists(author))
            throw new AuthorException(AuthorStatus.EXISTS.getLabel());
    }
}
