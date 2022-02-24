package com.example.DemoGraphQL.model;

import lombok.*;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
public class TreeNode {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="parent_id")
    private Long parentId;

    @Column(name = "value", nullable = false)
    private String value;

    @OneToMany
    private List<TreeNode> children;

    public TreeNode() {

    }


}