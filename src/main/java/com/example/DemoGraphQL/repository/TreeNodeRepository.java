package com.example.DemoGraphQL.repository;

import com.example.DemoGraphQL.model.TreeNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TreeNodeRepository extends JpaRepository<TreeNode,Long> {

    public List<TreeNode> findByParentId(Long parentId);

    public Page<TreeNode> findByParentId(Long parentId, Pageable page);


    @Query(value = "Select * from TreeNode t where t.parentId = ?1 and t.id>?3 order by t.id limit ?2", nativeQuery = true)
    List<TreeNode> findChildren(Long parentId, int size, Long lastId);
}
