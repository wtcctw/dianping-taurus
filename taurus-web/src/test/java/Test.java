import com.dp.bigdata.taurus.web.servlet.AttemptProxyServlet;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by kirinli on 14-9-23.
 */
public class Test {
    public static final  String url = "http://10.1.120.206:8080/agentrest.do?action=getlog&date=2014-09-22&attemptId=attempt_201408191754_0059_0031_0001&flag=NORMAL&query_type=log";

    public static String readContentFromGet() throws IOException {
        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
        String getURL = url;
        URL getUrl = new URL(getURL);
        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) getUrl
                .openConnection();
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
        // 服务器
        connection.connect();
        // 取得输入流，并使用Reader读取
        StringWriter writer = new StringWriter();
        InputStream inputStream =  connection.getInputStream();
        IOUtils.copy(inputStream, writer, "UTF-8");
        String theString = writer.toString();


        // 断开连接
        connection.disconnect();
        return theString;
    }
    public static void main(String[] args) {
        String ret="";
        long start = System.currentTimeMillis();
        try {
            ret = readContentFromGet();
            System.out.println(ret);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end1 = System.currentTimeMillis();

        System.out.println("############TIME1:"+(end1 - start) );

    }
}
