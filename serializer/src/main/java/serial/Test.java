package serial;


/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/1</p>
 */

public class Test {
    public static void main(String[] args) {
        User user = new User();
        user.setId(1);
        user.setName("宋江");
        user.setAge(35);

        /*ISerializer iSerializer = new FileSerializer();
        byte[] bytes = iSerializer.serializer(user);
        User user1 = iSerializer.deSerializer(bytes,User.class);
        System.out.println(user1.toString());*/

        /*ISerializer iSerializer = new XmlSerializer();
        byte[] bytes = iSerializer.serializer(user);
        System.out.println(new String(bytes));
        User user1 = iSerializer.deSerializer(bytes,User.class);
        System.out.println(user1.toString());*/

        ISerializer iSerializer = new FastjsonSerializer();
        byte[] bytes = iSerializer.serializer(user);
        System.out.println(new String(bytes));
        User user1 = iSerializer.deSerializer(bytes,User.class);
        System.out.println(user1.toString());

    }
}
