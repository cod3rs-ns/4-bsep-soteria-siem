package bsep.sw.hateoas;

public class ErrorResponse {

    private final StandardResponseMeta meta = new StandardResponseMeta();
    private final String code;
    private final String title;
    private final String detail;

    public ErrorResponse(String code, String title, String detail) {
        this.code = code;
        this.title = title;
        this.detail = detail;
    }

    public StandardResponseMeta getMeta() {
        return meta;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }
}
