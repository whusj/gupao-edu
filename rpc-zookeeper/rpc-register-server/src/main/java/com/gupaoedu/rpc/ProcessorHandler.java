package com.gupaoedu.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/6</p>
 */
public class ProcessorHandler implements Runnable {

    private Socket socket;
    Map<String, Object> handlerMap;

    public ProcessorHandler(Socket socket, Map<String, Object> handlerMap) {
        this.socket = socket;
        this.handlerMap = handlerMap;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        //处理请求
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;
        try {
            //获取客户端的输入流
            inputStream = new ObjectInputStream(socket.getInputStream());
            //反序列化远程传输的对象RcpRequest
            RpcRequest request = (RpcRequest)inputStream.readObject();
            Object result = invoke(request);//通过反射去调用本地的方法

            //通过输出流将结果输出给客户端
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(result);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Object invoke(RpcRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //以下均为反射操作，目的是通过反射调用服务
        Object[] args = request.getParameters();
        Class<?>[] types = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }
        String serviceName = request.getClassName();
        String version = request.getVersion();
        if(version != null && !"".equals(version)){
            serviceName = serviceName + "-" + version;
        }

        //从handlerMap中，根据客户端请求的地址，去拿响应的服务，通过反射发起调用
        Object service = handlerMap.get(serviceName);
        Method method = service.getClass().getMethod(request.getMethodName(),types);
        return method.invoke(service,args);
    }
}
