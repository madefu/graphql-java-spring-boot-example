package com.example.DemoGraphQL.resolver;

import com.example.DemoGraphQL.model.Author;
import com.example.DemoGraphQL.model.Book;
import com.example.DemoGraphQL.model.TreeNode;
import com.example.DemoGraphQL.repository.AuthorRepository;
import com.example.DemoGraphQL.repository.BookRepository;
import com.example.DemoGraphQL.repository.TreeNodeRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.relay.*;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Query implements GraphQLQueryResolver {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private TreeNodeRepository treeNodeRepository;
    public Query(AuthorRepository authorRepository, BookRepository bookRepository,TreeNodeRepository treeNodeRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.treeNodeRepository = treeNodeRepository;
    }

    public Iterable<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public long countBooks() {
        return bookRepository.count();
    }

    public Iterable<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    public long countAuthors() {
        return authorRepository.count();
    }

    public Iterable<TreeNode> findChildren(Long parentId){
        return treeNodeRepository.findByParentId(parentId);

    }

    public Iterable<TreeNode> findChildrenPageable(Long parentId,int page,int size){
        PageRequest pageRequest =PageRequest.of(page,size);
        return treeNodeRepository.findByParentId(parentId,pageRequest);
    }

    @Deprecated
    public Connection<Author> findAuthorsPage(int first, @Nullable String cursor, DataFetchingEnvironment env){
        //查找全体
        List<Author> authorList = authorRepository.findAll();
        List<Author> authors = StreamSupport.stream(authorList.spliterator(),false).collect(Collectors.toList());
        return new SimpleListConnection<Author>(authors).get(env);
    }

    public Connection<Author> findAuthorsPage2(int first, @Nullable String cursor, DataFetchingEnvironment env){
        //查找全体
        List<Author> authorList = authorRepository.findAll();
        List<Author> authors = StreamSupport.stream(authorList.spliterator(),false).collect(Collectors.toList());
        return new SimpleListConnection<Author>(authors).get(env);
    }
}
