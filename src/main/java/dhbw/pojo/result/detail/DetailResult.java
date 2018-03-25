package dhbw.pojo.result.detail;



public class DetailResult {
    private String title;
    private String info;
    private String additionalInfo;
    private String href;

    public DetailResult(String title, String info, String href, String additionalInfo) {
        this.title = title;
        this.info = info;
        this.href =href;
        this.additionalInfo=additionalInfo;
    }

    public DetailResult() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}

