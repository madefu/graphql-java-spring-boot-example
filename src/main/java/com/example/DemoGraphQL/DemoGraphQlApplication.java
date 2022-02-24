package com.example.DemoGraphQL;

import com.example.DemoGraphQL.exception.GraphQLErrorAdapter;
import com.example.DemoGraphQL.model.Author;
import com.example.DemoGraphQL.model.Book;
import com.example.DemoGraphQL.model.TreeNode;
import com.example.DemoGraphQL.repository.AuthorRepository;
import com.example.DemoGraphQL.repository.BookRepository;
import com.example.DemoGraphQL.repository.TreeNodeRepository;
import com.example.DemoGraphQL.resolver.BookResolver;
import com.example.DemoGraphQL.resolver.Mutation;
import com.example.DemoGraphQL.resolver.Query;
import com.example.DemoGraphQL.resolver.TreeNodeResolver;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication

public class DemoGraphQlApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DemoGraphQlApplication.class, args);
	}

	@Bean
	public GraphQLErrorHandler errorHandler() {
		return new GraphQLErrorHandler() {
			@Override
			public List<GraphQLError> processErrors(List<GraphQLError> errors) {
				List<GraphQLError> clientErrors = errors.stream()
						.filter(this::isClientError)
						.collect(Collectors.toList());

				List<GraphQLError> serverErrors = errors.stream()
						.filter(e -> !isClientError(e))
						.map(GraphQLErrorAdapter::new)
						.collect(Collectors.toList());

				List<GraphQLError> e = new ArrayList<>();
				e.addAll(clientErrors);
				e.addAll(serverErrors);
				return e;
			}

			protected boolean isClientError(GraphQLError error) {
				return !(error instanceof ExceptionWhileDataFetching || error instanceof Throwable);
			}
		};
	}

	@Bean
	public BookResolver authorResolver(AuthorRepository authorRepository) {
		return new BookResolver(authorRepository);
	}

	@Bean
	public TreeNodeResolver treeNodeResolver(TreeNodeRepository treeNodeRepository){
		return new TreeNodeResolver(treeNodeRepository);
	}

	@Bean
	public Query query(AuthorRepository authorRepository, BookRepository bookRepository, TreeNodeRepository treeNodeRepository) {
		return new Query(authorRepository, bookRepository, treeNodeRepository);
	}

	@Bean
	public Mutation mutation(AuthorRepository authorRepository, BookRepository bookRepository,  TreeNodeRepository treeNodeRepo) {
		return new Mutation(authorRepository, bookRepository, treeNodeRepo);
	}

	@Bean
	public CommandLineRunner demo(AuthorRepository authorRepository, BookRepository bookRepository , TreeNodeRepository treeNodeRepo) {
		return (args) -> {
			Author author = new Author("Herbert", "Schildt");
			authorRepository.save(author);
			int i=0;
			while(i<200) {
				authorRepository.save(new Author("madefu", "1000" + i++));
			}

			bookRepository.save(new Book("Java: A Beginner's Guide, Sixth Edition", "0071809252", 728, author));

			treeNodeRepo.save(new TreeNode(null,null,"root",null));
			/**
			1
				1.1
				1.2
			 		1.2.1
			 		1.2.2
			 		1.2.3
			 	1.3
			 	1.4
			 	1.5
			 2
			 	2.1
			*/
			TreeNode tn1root = treeNodeRepo.save(new TreeNode(null,null,"1",null));
			treeNodeRepo.save(new TreeNode(null,tn1root.getId(),"1.1",null));
			TreeNode tn12 = treeNodeRepo.save(new TreeNode(null,tn1root.getId(),"1.2",null));
			treeNodeRepo.save(new TreeNode(null,tn12.getId(),"1.2.1",null));
			treeNodeRepo.save(new TreeNode(null,tn12.getId(),"1.2.2",null));
			treeNodeRepo.save(new TreeNode(null,tn12.getId(),"1.2.3",null));
			treeNodeRepo.save(new TreeNode(null,tn1root.getId(),"1.3",null));
			treeNodeRepo.save(new TreeNode(null,tn1root.getId(),"1.4",null));
			treeNodeRepo.save(new TreeNode(null,tn1root.getId(),"1.5",null));
			TreeNode tn2root = treeNodeRepo.save(new TreeNode(null,null,"2",null));
			treeNodeRepo.save(new TreeNode(null,tn2root.getId(),"2.1",null));

		};
	}
}
