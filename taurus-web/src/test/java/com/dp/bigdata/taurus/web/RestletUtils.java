package com.dp.bigdata.taurus.web;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Uniform;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenchongze on 16/3/31.
 */
public class RestletUtils {

    public static <T> T get(ClientResource cr, Class<T> resultClass, long waitTimeout) {
        try {
            InnerClassUniform uniform = new InnerClassUniform();
            cr.setOnResponse(uniform);
            cr.get();
            return resultClass!=null ? cr.toObject(uniform.getResult(waitTimeout, TimeUnit.MILLISECONDS), resultClass) : null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            cr.release();
        }
    }

    public static <T> T get(String uri, Class<T> resultClass, long waitTimeout) {
        return get(new ClientResource(uri), resultClass, waitTimeout);
    }

    public static <T> T get(String uri, long waitTimeout) {
        return get(new ClientResource(uri), null, waitTimeout);
    }

    public static <T> T get(ClientResource cr, long waitTimeout) {
        return get(cr, null, waitTimeout);
    }

    public static <T> T post(ClientResource cr, Representation entity, Class<T> resultClass, long waitTimeout) {
        try {
            InnerClassUniform uniform = new InnerClassUniform();
            cr.setOnResponse(uniform);
            cr.post(entity);
            return resultClass!=null ? cr.toObject(uniform.getResult(waitTimeout, TimeUnit.MILLISECONDS), resultClass) : null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            cr.release();
        }
    }

    public static <T> T post(String uri, Representation entity, Class<T> resultClass, long waitTimeout) {
        return post(new ClientResource(uri), entity, resultClass, waitTimeout);
    }

    public static <T> T post(String uri, Representation entity, long waitTimeout) {
        return post(new ClientResource(uri), entity, null, waitTimeout);
    }

    public static <T> T post(ClientResource cr, Representation entity, long waitTimeout) {
        return post(cr, entity, null, waitTimeout);
    }

    private static class InnerClassUniform implements Uniform {

        private Representation result;
        CountDownLatch latch = new CountDownLatch(1);

        @Override
        public void handle(Request request, Response response) {
            result = response.getEntity();
            latch.countDown();
        }

        public Representation getResult(long timeout, TimeUnit unit) throws InterruptedException {
            latch.await(timeout, unit);
            return result;
        }
    }
}
