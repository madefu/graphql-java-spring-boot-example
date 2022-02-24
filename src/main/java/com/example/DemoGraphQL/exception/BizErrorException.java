package com.example.DemoGraphQL.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BizErrorException extends RuntimeException implements GraphQLError {

    private Map<String, Object> extensions = new HashMap<>();

    public BizErrorException(BizErrorEnum bizErrorEnum ) {
        super(bizErrorEnum.getMsg());
        extensions.put("code", bizErrorEnum.getCode()  );
        extensions.put("msg", bizErrorEnum.getMsg()  );
    }
    public BizErrorException(BizErrorEnum bizErrorEnum, String data) {
        super(bizErrorEnum.getMsg());
        extensions.put("code", bizErrorEnum.getCode()  );
        extensions.put("msg", bizErrorEnum.getMsg()  );
        extensions.put("data",data  );
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.DataFetchingException;
    }
}
