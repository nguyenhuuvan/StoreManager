package poly.project.storemanager.model;

public class Information {
    private final String title;
    private final String content;
    private final String detail;

    public Information(String title, String content, String detail) {
        this.title = title;
        this.content = content;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDetail() {
        return detail;
    }

}
