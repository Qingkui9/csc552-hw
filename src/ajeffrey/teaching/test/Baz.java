package ajeffrey.teaching.test;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.Serializable;

public interface Baz extends Remote, Serializable {

    public void go () throws RemoteException;

}
