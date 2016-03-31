package com.dp.bigdata.taurus.web;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Uniform;
import org.restlet.resource.ClientResource;

/**
 * Created by chenchongze on 16/3/31.
 */
public class RestletUtils {

    public static <T> T get(String uri, Class<T> resultClass, long waitTimeout) {
        final ClientResource cr = new ClientResource(uri);
        try {

            CustomClassUniform<String> stringCustomClassUniform = new CustomClassUniform<String>(cr, String.class);

            return null;
        } catch (Throwable t) {
            throw new RuntimeException("restlet call failed", t);
        } finally {
            cr.release();
        }
    }

    static class CustomClassUniform<T> implements Uniform {

        private T result;

        private final Class<T> resultClass;

        private final ClientResource cr;

        CustomClassUniform(ClientResource cr, Class<T> resultClass) {
            this.cr = cr;
            this.resultClass = resultClass;
        }

        @Override
        public void handle(Request request, Response response) {
            result = cr.toObject(response.getEntity(), resultClass);
        }

        public T getResult() {
            return result;
        }
    }
}
