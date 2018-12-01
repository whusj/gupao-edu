package serial;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/1</p>
 */
public interface ISerializer {
    <T> byte[] serializer(T obj);

    <T> T deSerializer(byte[] data, Class<T> clazz);
}
