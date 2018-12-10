package com.gupaoedu.rmi;

import javax.sound.midi.Soundbank;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/1</p>
 */
public class Client {
    public static void main(String[] args) {
        try {
            IHelloService helloService = (IHelloService)Naming.lookup("rmi://127.0.0.1/Hello");// HelloServiceImpl实例(HelloServiceImpl_stub)
            // RegistryImpl_stub
            System.out.println(helloService.sayHello("Songjiang123"));
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
