package serial;

import com.alibaba.fastjson.JSON;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/1</p>
 */
public class FastjsonSerializer implements ISerializer {

    public <T> byte[] serializer(T obj) {
        return JSON.toJSONString(obj).getBytes();
    }

    public <T> T deSerializer(byte[] data, Class<T> clazz) {
        return (T)JSON.parseObject(data,clazz);
    }
}
