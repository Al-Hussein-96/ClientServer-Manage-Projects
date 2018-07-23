package Different;

import java.io.Serializable;

public abstract class Changes implements Serializable {

    private final String object;

    protected Changes(String object) {
        this.object = object;
    }

    public String getObject() {
        return object;
    }

}
