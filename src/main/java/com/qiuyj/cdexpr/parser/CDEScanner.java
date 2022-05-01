package com.qiuyj.cdexpr.parser;

import com.qiuyj.cdexpr.utils.InternalCharStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 默认的词法解析器，会根据输入的字符流，解析成对应的token流
 * @author qiuyj
 * @since 2022-04-17
 */
public class CDEScanner implements Lexer {

    private final CDETokenizer tokenizer;

    /**
     * 当前解析的token
     */
    private Token token;

    /**
     * 保存之前已经解析过的token
     */
    private final List<Token> tokenStream = new ArrayList<>();

    public CDEScanner(String source) {
        tokenizer = new CDETokenizer(InternalCharStream.wrap(source));
    }

    @Override
    public void nextToken() {
        Token curToken = tokenizer.processOne();
        if (Objects.nonNull(token)) {
            tokenStream.add(token);
        }
        token = curToken;
    }

    @Override
    public Token token() {
        return token;
    }

    @Override
    public Token prevToken() {
        return tokenStream.isEmpty()
                ? null
                : tokenStream.get(tokenStream.size() - 1);
    }

    @Override
    public List<Token> nextAllTokens() {
        if (tokenizer.source().remaining()) {
            do {
                nextToken();
            }
            while (Objects.nonNull(token()));
        }
        return Collections.unmodifiableList(tokenStream);
    }

    @Override
    public void consumeTokens(Consumer<Token> tokenConsumer) {
        if (tokenizer.source().remaining()) {
            while (true) {
                nextToken();
                Token token = token();
                if (Objects.isNull(token)) {
                    break;
                }
                tokenConsumer.accept(token);
            }
        }
    }

}
