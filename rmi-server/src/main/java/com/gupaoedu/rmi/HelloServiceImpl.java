package com.gupaoedu.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/1</p>
 */
public class HelloServiceImpl extends UnicastRemoteObject implements IHelloService {
    /**
     * Creates and exports a new UnicastRemoteObject object using an
     * anonymous port.
     * <p>
     * <p>The object is exported with a server socket
     * created using the {@link RMISocketFactory} class.
     *
     * @throws RemoteException if failed to export object
     * @since JDK1.1
     */
    protected HelloServiceImpl() throws RemoteException {
        super();
    }

    public String sayHello(String name) throws RemoteException {
        return "hello, " + name;
    }
}
