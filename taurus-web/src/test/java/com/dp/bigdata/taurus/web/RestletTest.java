package com.dp.bigdata.taurus.web;

import org.restlet.*;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;

/**
 * Created by chenchongze on 16/3/31.
 */
public class RestletTest {

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
