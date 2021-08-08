package util;

import java.io.*;

public abstract class SerialCloneUtils {

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deepClone(T t) throws CloneNotSupportedException {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(bout)) {
                out.writeObject(t);
            }
            try (InputStream bin = new ByteArrayInputStream(bout.toByteArray())) {
                ObjectInputStream in = new ObjectInputStream(bin);
                return (T) (in.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            CloneNotSupportedException cloneNotSupportedException = new CloneNotSupportedException();
            e.initCause(cloneNotSupportedException);
            throw cloneNotSupportedException;
        }
    }
}
