package com.example.DemoGraphQL.resolver;

import com.example.DemoGraphQL.exception.BizErrorEnum;
import com.example.DemoGraphQL.exception.BizErrorException;
import com.example.DemoGraphQL.exception.BookNotFoundException;
import com.example.DemoGraphQL.model.Author;
import com.example.DemoGraphQL.model.Book;
import com.example.DemoGraphQL.model.TreeNode;
import com.example.DemoGraphQL.repository.AuthorRepository;
import com.example.DemoGraphQL.repository.BookRepository;
import com.example.DemoGraphQL.repository.TreeNodeRepository;
import graphql.GraphqlErrorException;
import graphql.kickstart.tools.GraphQLMutationResolver;

import java.util.Optional;

public class Mutation implements GraphQLMutationResolver {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private TreeNodeRepository treeNodeRepository;

    public Mutation(AuthorRepository authorRepository, BookRepository bookRepository,TreeNodeRepository treeNodeRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.treeNodeRepository = treeNodeRepository;
    }

    public Author newAuthor(String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);

        authorRepository.save(author);

        return author;
    }

    public Book newBook(String title, String isbn, Integer pageCount, Long authorId) {
        Book book = new Book();
        book.setAuthor(new Author(authorId));
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setPageCount(pageCount != null ? pageCount : 0);

        bookRepository.save(book);

        return book;
    }

    public boolean deleteBook(Long id) {
        bookRepository.deleteById(id);
        return true;
    }

    public Book updateBookPageCount(Integer pageCount, Long id) {
        Optional<Book> opt = bookRepository.findById(id);
        if (opt.isPresent()) {
            Book book = opt.get();
            book.setPageCount(pageCount);
            bookRepository.save(book);
            return book;
        }
        throw new BookNotFoundException("The book to be updated was found", id);
    }

    public TreeNode newTreeNode(Long parentId, String value)throws BizErrorException ,Exception{
        if(parentId==null){
            //creat root
            TreeNode treeNode  = treeNodeRepository.save(new TreeNode(null,null,value,null));
            return treeNode;
        }
        Optional<TreeNode> opt = treeNodeRepository.findById(parentId);
        if (opt.isPresent()){
            TreeNode treeNode  = treeNodeRepository.save(new TreeNode(null,parentId,value,null));
            return treeNode;
        }else{
            throw new BizErrorException(BizErrorEnum.NotFoundObject);
        }
    }
}
