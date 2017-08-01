package org.isouth.router.iris;

import org.isouth.router.Router;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class IrisRouter<H> implements Router<H> {

    private String relativePath = "/";

    private List<Route<H>> allRoutes = new ArrayList<>();

    private List<Route<H>> routes = new ArrayList<>();

    @Override
    public Router app(String relativePath) {
        return null;
    }

    @Override
    public String route(String method, String path, H h) {
        if ("ANY".equals(method) || "ALL".equals(method)) {
            any(path, h);
            return null;
        }

        if (relativePath.charAt(relativePath.length() - 1) == '/') {
            if (path.charAt(0) == '/') {
                path = path.substring(1, path.length());
            }
        }

        String fullpath = relativePath + path;
        String subdomian = "";
        int idx = fullpath.indexOf("./");
        if (idx != -1) {
            subdomian = fullpath.substring(0, idx + 1);
            fullpath = fullpath.substring(idx + 1, fullpath.length());
        }

        Route<H> route = Route.create(method, subdomian, fullpath, h);
        allRoutes.add(route);
        routes.add(route);
        return route.name;
    }

    @Override
    public Router remove(String name) {
        return null;
    }

    @Override
    public Router build() {
        return null;
    }

    @Override
    public H match(String method, String path) {
        return null;
    }

    public static class Route<H> {
        private String name;
        private String method;
        private String subdomain;
        private Template tmpl;
        private String path;
        private String formatted;
        private H h;

        public static <H> Route<H> create(String method, String subdomain, String unparsedPath, H h) {
            Template tmpl = Macro.parse(unparsedPath);
            String path = compile(tmpl);
            path = cleanPath(path);
            String defaultName = method + subdomain + path;
            String formattedPath = formatPath(path);

            Route route = new Route();
            route.name = defaultName;
            route.method = method;
            route.subdomain = subdomain;
            route.tmpl = tmpl;
            route.path = path;
            route.h = h;
            route.formatted = formattedPath;
            return route;
        }

        public static String cleanPath(String path) {
            //todo clean path
            return null;
        }

        public static String formatPath(String path) {
            //todo format path
            return null;
        }

        public static String compile(Template tmpl) {
            //todo compile template
            return null;
        }

    }

    public static class Macro {
        public static Template parse(String src) {
            //todo parse template
            return null;
        }
    }

    public static class Template {
        //todo template
        private String src;
        private List<TemplateParam> params;
    }

    public static class TemplateParam {
        //todo template param
        private String src;
        private ParamType type;
        private String name;
        private int errCode;
        private Predicate<String> typeEvaluator;
        private List<Predicate<String>> funcs;
    }

    public List<ParamStatement> parse(String fullpath) {
        String[] parts = fullpath.split("/");
        Parser parser = new Parser();
        List<ParamStatement> statements = new ArrayList<>();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) {
                continue;
            }
            if (part.charAt(0) != '{' ||
                    part.charAt(part.length() - 1) != '}') {
                continue;
            }
            parser.reset(part);
            ParamStatement stmt = parser.parse();
            if (stmt.type == ParamType.PATH &&
                    i < parts.length - 1) {
                throw new IllegalArgumentException(
                        "param type 'path' should be lived only inside " +
                                "the last path segment, but was inside: " + part);
            }
            statements.add(stmt);
        }
        return statements;
    }

    public static enum ParamType {
        UNEXPECTED,
        STRING,
        INT,
        ALPHABETICAL,
        FILE,
        PATH
    }

    public static class ParamStatement {
        private ParamType type;
    }

    public static class Parser {
        private String src;

        public void reset(String src) {
            this.src = src;
        }

        public ParamStatement parse() {

            return null;
        }
    }

    public static class Token {
        public static final int EOF = 0;
        public static final int ILLEGAL = 1;
        public static final int LBRACE = 2;  // {
        public static final int RBRACE = 3; // }
        public static final int COLON = 4; // :
        public static final int LPAREN = 5; // (
        public static final int RPAREN = 6; // )
        public static final int COMMA = 7; //
        public static final int IDENT = 8; //
        public static final int ELSE = 9; // else
        public static final int INT = 10; // (

        private int type;
        private String literal;
        private int start;
        private int end;


    }

    public static class Lexer {
        private String input;
        private int pos;
        private int readPos;
        private char ch;

        public Lexer(String input) {
            this.input = input;
        }

        private void readChar() {
            if (this.readPos >= this.input.length()) {
                this.ch = 0;
            } else {
                this.ch = this.input.charAt(this.readPos);
            }
            this.pos = this.readPos;
            this.readPos++;
        }

        private int resolveTokenType(char ch) {
            switch (ch) {
                case '{':
                    return Token.LBRACE;
                case '}':
                    return Token.RBRACE;
                case ':':
                    return Token.COLON;
                case '(':
                    return Token.LPAREN;
                case ')':
                    return Token.RPAREN;
                case ',':
                    return Token.COMMA;
                case 0:
                    return Token.EOF;
                default:
                    return Token.IDENT;
            }
        }

        private Token NextToken() {
            this.skipWhitespace();
            int type = this.resolveTokenType(this.ch);
            Token token = new Token();
            token.type = type;
            switch (type) {
                case Token.EOF:
                    token.literal = "";
                case Token.IDENT:
                    if (isLetter(this.ch)) {
                        return newToken(type, this.readIdentifier());
                    }
                    if (isDigit(this.ch)) {
                        return newToken(Token.INT, this.readNumber());
                    }
                    throw new IllegalArgumentException("");
                default:
                    token = newToken(type, String.valueOf(this.ch));
            }
            this.readChar();
            return token;
        }

        private void skipWhitespace() {
            while (this.ch == ' ' || this.ch == '\t'
                    || this.ch == '\n' || this.ch == '\r') {
                this.readChar();
            }
        }

        public Token newToken(int type, String literal) {
            Token token = new Token();
            token.type = type;
            token.literal = literal;
            token.start = this.pos;
            token.end = this.pos;
            if (this.pos > 1 && literal.length() > 1) {
                token.end = this.pos - 1;
                token.start = token.end - literal.length() + 1;
            }
            return token;
        }

        private String readIdentifier() {
            int pos = this.pos;
            while (isLetter(this.ch)) {
                this.readChar();
            }
            return this.input.substring(pos, this.pos);
        }

        private boolean isLetter(char ch) {
            return ('a' <= ch && ch <= 'z') || (
                    'A' <= ch && ch <= 'Z') || ch == '_';
        }

        private String readNumber() {
            int pos = this.pos;
            while (isDigit(this.ch)) {
                this.readChar();
            }
            return this.input.substring(pos, this.pos);
        }

        private boolean isDigit(char ch) {
            return '0' <= ch && ch <= '9';
        }
    }

}
