package com.example.DemoGraphQL.resolver;


import com.example.DemoGraphQL.model.TreeNode;
import com.example.DemoGraphQL.repository.TreeNodeRepository;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.relay.*;

import java.util.List;
import java.util.stream.Collectors;

public class TreeNodeResolver implements GraphQLResolver<TreeNode> {
    private TreeNodeRepository treeNodeRepository;
    public TreeNodeResolver(TreeNodeRepository treeNodeRepository){
        this.treeNodeRepository=treeNodeRepository;
    }

    public Connection<TreeNode> getFindChildrenCursor(Long parentId,int size,Long cursor){
        // 从after中解析出最后一条数据的ID
        Long lastId = cursor;
//        if (after != null) {
//            lastId = CursorUtil.decode(after);
//        }

        // 这一条相当于 select * from artist where id > $lastId limit ($first + 1)
        // 比 first 多取一条数据，用于判断是否还有下一页
        List<TreeNode> artists = treeNodeRepository.findChildren(parentId,size, lastId);

        // 是否还有下一页
        boolean hasNextPage = artists.size() > size;

        // 如果有下一页，则删除最后那一条冗余的数据
        if (hasNextPage) {
            artists.remove(artists.size() - 1);
        }

        // 把数据塞到Edges里面
        List<Edge<TreeNode>> edges = artists.stream()
                .map(artist -> new DefaultEdge<>(artist, CursorUtil.encode(artist.getId()))).collect(Collectors.toList());

        // 分页信息
        PageInfo pageInfo =  new DefaultPageInfo(
                CursorUtil.getStartCursorFrom(edges),
                CursorUtil.getEndCursorFrom(edges),
                false,
                hasNextPage
        );

        // 返回 Connection
        return new DefaultConnection<>(edges, pageInfo);
    }


}
