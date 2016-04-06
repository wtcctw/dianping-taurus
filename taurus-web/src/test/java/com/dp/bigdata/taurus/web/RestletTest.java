package com.dp.bigdata.taurus.web;

import org.junit.Test;
import org.restlet.*;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;

/**
 * Created by chenchongze on 16/3/31.
 */
public class RestletTest {

    @Test
    public void testNotify() throws InterruptedException {
        TestRun testRun = new TestRun();
        testRun.start();
        Thread.sleep(3000);
        testRun.interrupt();
    }

    static class TestRun extends Thread {

        TestRun() {

        }

        public void run(long waitTimeout) throws InterruptedException {

        }

        @Override
        public void run() {
            System.out.println("111");
            try {
                Thread.sleep(60 * 6000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
                System.out.println("interrupt");
            }
        }
    }

    @Test
    public void testResource() {
        //RestletUtils.get("http://localhost:8080/ruok", null, 1000);
        String result = RestletUtils.get("http://localhost:8080/sleep", String.class, 15000);
        System.out.println(result);
    }

    public static void main(String[] args) {
        final ClientResource cr = new ClientResource("http://localhost:8080/ruok");
        try {
            /*Context context = new Context();
            context.getParameters().add("socketTimeout", "1000");
            Client client = new Client(context, Protocol.HTTP);*/

            String result = "response timeout";
            cr.setRetryOnError(false);
            cr.setOnResponse(new Uniform() {
                @Override
                public void handle(Request request, Response response) {
                    String ret = cr.toObject(response.getEntity(), String.class);
                    System.out.println(ret);
                    /*try {
                        System.out.println(response.getEntity().getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            });
            cr.get();
            //cr.getOnResponse();

            Thread.sleep(3 * 1000);

            System.out.println(result);

        } catch (ResourceException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            cr.release();
        }
    }
}
