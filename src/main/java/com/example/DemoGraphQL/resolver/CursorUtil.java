package com.example.DemoGraphQL.resolver;

import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.Edge;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class CursorUtil {
    public static ConnectionCursor encode(Long id) {
        return new DefaultConnectionCursor(
                Base64.getEncoder().encodeToString(id.toString().getBytes(StandardCharsets.UTF_8))
        );
    }

    public static Long decode(String cursor) {
        return Long.parseLong(new String(Base64.getDecoder().decode(cursor)));
    }

    public static <T> ConnectionCursor getStartCursorFrom(List<Edge<T>> edges) {
        return edges.isEmpty() ? null : edges.get(0).getCursor();
    }

    public static <T> ConnectionCursor getEndCursorFrom(List<Edge<T>> edges) {
        return edges.isEmpty() ? null : edges.get(edges.size() - 1).getCursor();
    }
}
