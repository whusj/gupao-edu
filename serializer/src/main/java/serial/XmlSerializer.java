package serial;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/1</p>
 */
public class XmlSerializer implements ISerializer {

    XStream xStream = new XStream(new DomDriver());

    public <T> byte[] serializer(T obj) {
        return xStream.toXML(obj).getBytes();
    }

    public <T> T deSerializer(byte[] data, Class<T> clazz) {
        return (T)xStream.fromXML(new String(data));
    }
}
