package course.coursera.dailyselfie;

/**
 * Created by Guillermo Celarie on 31/05/2015.
 */
public class Selfie {
    private long id;
    private String uri;
    private String date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return uri;
    }
}
