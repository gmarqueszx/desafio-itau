package space.gmarqueszx.desafio_itau.handler;

import lombok.Getter;

@Getter
public enum ProblemType {
    INVALID_TRANSACTION_EXCEPTION("/invalid-transaction", "Invalid transaction"),
    BAD_REQUEST("/bad-request", "Bad request");

    private String title;
    private String uri;

    ProblemType(String path, String title) {
        this.uri = "https://gmarqueszx.space" + path;
        this.title = title;
    }
}
