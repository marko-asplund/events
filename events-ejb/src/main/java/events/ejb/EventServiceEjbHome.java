package events.ejb;

public interface EventServiceEjbHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/EventServiceEjb";
   public static final String JNDI_NAME="EventServiceEjb";

   public events.ejb.EventServiceEjb create()
      throws javax.ejb.CreateException,java.rmi.RemoteException;

}
