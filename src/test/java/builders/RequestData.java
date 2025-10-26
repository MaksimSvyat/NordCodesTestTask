package builders;

public class RequestData {
    private final String token;
    private final String action;
    private final boolean withApiKey;

    public RequestData(String token, String action, boolean withApiKey) {
        this.token = token;
        this.action = action;
        this.withApiKey = withApiKey;
    }

    public String getToken() {
        return token;
    }

    public String getAction() {
        return action;
    }

    public boolean isWithApiKey() {
        return withApiKey;
    }

    @Override
    public String toString() {
        return String.format("RequestData{token='%s', action='%s', withApiKey=%s}",
                maskToken(token), action, withApiKey);
    }

    private String maskToken(String token) {
        if (token == null || token.length() <= 8) return "***";
        return token.substring(0, 4) + "***" + token.substring(token.length() - 4);
    }
}
