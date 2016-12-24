package org.jakev.serviceutils;

import android.os.IBinder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* Wrapper for connecting to a system service. */
public class SystemServiceConnector {

    private String SERVICE_MANAGER_CLASS = "android.os.ServiceManager";
    private String serviceName;
    private String serviceClassStubName;
    private String serviceClassStubProxyName;
    private Object AIDLClass;
    private Class stubProxyClass;

    /**
     * Class constructor specifying a service name and corresponding Java class name.
     */
    public SystemServiceConnector(String serviceName, String serviceClassName)
            throws ConnectorException {

        this.serviceName = serviceName;
        this.serviceClassStubName = serviceClassName + "$Stub";
        this.serviceClassStubProxyName = this.serviceClassStubName + "$Proxy";

        try {
            /* Load our IBinder */
            Class ServiceManager = Class.forName(SERVICE_MANAGER_CLASS);
            Method getService = ServiceManager.getMethod("getService", String.class);
            IBinder serviceIBinder = (IBinder)getService.invoke(null, this.serviceName);

            /* Get handle to AIDL Stub class */
            Class ServiceAIDLStub = Class.forName(serviceClassStubName);
            Method asInterface = ServiceAIDLStub.getMethod("asInterface", IBinder.class);

            /* Get class handle to StubProxy and save it */
            this.stubProxyClass = Class.forName(serviceClassStubProxyName);

            /* Return an object of type AIDLClass */
            this.AIDLClass = asInterface.invoke(null, serviceIBinder);

        } catch (ClassNotFoundException e) {
            throw new ConnectorException(e);
        } catch (NoSuchMethodException e) {
            throw new ConnectorException(e);
        } catch (InvocationTargetException e) {
            throw new ConnectorException(e);
        } catch (IllegalAccessException e) {
            throw new ConnectorException(e);
        }
    }

    /**
     * Attempt to call a method of a IBinder Service and return its response.
     * This method can be used if you need to pass arguments to the method.
     * <p>
     * This method was raise exceptions if the method is not found, the
     * argument types do not match, or if there is a permission issue.
     *
     * @param  methodName  The name of the method to invoke
     * @param  args  An array of arguments to pass to method
     * @return      The response from invoking the method (returned as Object)
     * @throws ConnectorException  If an error occurs trying to call method
     */
     public Object callMethod(String methodName, Object... args)
            throws ConnectorException {

        Class<?>[] classList;
        Object[] objectList;

        /* No arguments means we just pass nulls */
        if (args == null) {
            classList = null;
            objectList = null;

        /* Otherwise, build Class<?>[] */
        } else {
            classList = new Class<?>[args.length];
            objectList = new Object[args.length];
            int i = 0;
            for (Object o : args) {
                classList[i] = o.getClass();
                objectList[i] = o;
                i++;
            }
        }

        /* Call the method */
        try {
            Method method = this.stubProxyClass.getMethod(methodName, classList);
            return method.invoke(this.AIDLClass, objectList);

        } catch (NoSuchMethodException e) {
            throw new ConnectorException(e);
        } catch (InvocationTargetException e) {
            throw new ConnectorException(e);
        } catch (IllegalAccessException e) {
            throw new ConnectorException(e);
        }
    }

    /**
     * Attempt to call a method of a IBinder Service and return its response.
     * This method does not attempt to pass any arguments to the called method.
     * <p>
     * This method was raise exceptions if the method is not found, the
     * argument types do not match, or if there is a permission issue.
     *
     * @param  methodName  The name of the method to invoke
     * @return      The response from invoking the method (returned as Object)
     * @see #callMethod(String, Object...)
     * @throws ConnectorException  If an error occurs trying to call method
     */
    public Object callMethod(String methodName)
            throws ConnectorException {

        return callMethod(methodName, null);
    }

    /**
     * Determine if the connected System Service has the supplied method name.
     *
     * @param  methodName  The name of the method to search for
     * @return      Whether or not the method exists
     */
    public Boolean hasMethod(String methodName) {


        for (Method method : this.stubProxyClass.getMethods()) {
            if (method.getName().equals(methodName)) {
                return true;
            }
        }

        /* Not found */
        return false;
    }
}