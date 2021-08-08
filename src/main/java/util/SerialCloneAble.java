package util;

import java.io.Serializable;

public abstract class SerialCloneAble implements Cloneable, Serializable {

    private static final long serialVersionUID = 5794148504376232369L;

    @Override
    public Object clone() {
        try {
            return SerialCloneUtils.deepClone(this);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
