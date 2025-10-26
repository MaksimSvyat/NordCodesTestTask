package builders;

import helpers.TokenHelper;

public class RequestDataBuilder {
    private String token;
    private String action;
    private boolean withApiKey = true;

    private RequestDataBuilder() {
    }

    public static RequestDataBuilder create() {
        return new RequestDataBuilder();
    }

    public RequestDataBuilder withValidToken() {
        this.token = TokenHelper.generateValidToken();
        return this;
    }

    public RequestDataBuilder withInvalidToken() {
        this.token = TokenHelper.generateInvalidToken();
        return this;
    }

    public RequestDataBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    public RequestDataBuilder withLoginAction() {
        this.action = "LOGIN";
        return this;
    }

    public RequestDataBuilder withActionAction() {
        this.action = "ACTION";
        return this;
    }

    public RequestDataBuilder withLogoutAction() {
        this.action = "LOGOUT";
        return this;
    }

    public RequestDataBuilder withAction(String action) {
        this.action = action;
        return this;
    }

    public RequestDataBuilder withoutApiKey() {
        this.withApiKey = false;
        return this;
    }

    public RequestData build() {
        return new RequestData(token, action, withApiKey);
    }
}
