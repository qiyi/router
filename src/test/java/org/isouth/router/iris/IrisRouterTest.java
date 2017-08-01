package org.isouth.router.iris;

import org.isouth.router.Router;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IrisRouterTest {

    private IrisRouter<Object> irisRouter = new IrisRouter<>();

    @Test
    public void testGetSubsomain() {
        Object obj = new Object();
        irisRouter.get("subdomain./get", obj);
        Object handler = irisRouter.match(Router.GET_METHOD, "subdomain.isouth.org/get");
        assertEquals(obj, handler);
    }

}
