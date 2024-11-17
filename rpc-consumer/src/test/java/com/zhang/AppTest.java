package com.zhang;

import com.zhang.proxy.CglibTransactionProxy;
import com.zhang.proxy.UserDao;
import com.zhang.proxy.UserDaoImpl;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        UserDao userDao = new UserDaoImpl();
//        UserDao proxyInstance = (UserDao) new TransactionProxy(userDao).getProxyInstance();
        UserDao proxyInstance = (UserDao) new CglibTransactionProxy(userDao).getProxyInstance();
        proxyInstance.insert();
    }
}
