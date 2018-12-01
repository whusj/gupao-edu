package com.gupaoedu.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/1</p>
 */
public interface IHelloService extends Remote{
    public String sayHello(String name) throws RemoteException;
}
