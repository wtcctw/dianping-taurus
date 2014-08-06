package com.dp.bigdata.taurus.web.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dp.bigdata.taurus.restlet.resource.IAttemptsResource;
import com.dp.bigdata.taurus.restlet.shared.AttemptDTO;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.dp.bigdata.taurus.restlet.resource.IAttemptResource;

/**
 * AttemptProxyServlet
 * 
 * @author damon.zhu
 */
public class AttemptProxyServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = -2924647981910768516L;

    private static final String KILL = "kill";
    private static final String LOG = "view-log";
    private static final String RUNLOG = "runlog";
    private static final String ERRORLOG = "runerrorlog";

    private static int indexOfLog=0;
    private static int indexOfErrorLog=0;

    private String RESTLET_URL_BASE;
    private String ERROR_PAGE;
    private String AGENT_PORT;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = getServletContext();
        RESTLET_URL_BASE = context.getInitParameter("RESTLET_SERVER");
        ERROR_PAGE = context.getInitParameter("ERROR_PAGE");
        AGENT_PORT = context.getInitParameter("AGENT_SERVER_PORT");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String attemptID = request.getParameter("id");
        String action = request.getParameter("action") == null ? "" : request.getParameter("action").toLowerCase();
        String status = request.getParameter("status");

        ClientResource attemptCr = new ClientResource(RESTLET_URL_BASE + "attempt/" + attemptID);
        IAttemptResource attemptResource = attemptCr.wrap(IAttemptResource.class);

        if (action.equals(KILL)) {
            attemptResource.kill();
            response.setStatus(attemptCr.getStatus().getCode());
        } /*else if (action.equals(LOG)) {
            response.setContentType("text/html;charset=utf-8");
            try {
                Representation rep = attemptCr.get(MediaType.TEXT_HTML);
                if (attemptCr.getStatus().getCode() == 200) {
                    OutputStream output = response.getOutputStream();
                    rep.write(output);
                    output.close();
                } else {
                    //getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
                }
            } catch (Exception e) {
                getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
            }
        }*/ else if (action.equals(LOG)) {
            response.setContentType("text/html;charset=utf-8");
            try {
                    String host = "";
                    Date endTime = null;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                    ClientResource attemptLogCr = new ClientResource(RESTLET_URL_BASE + "attempt");

                    IAttemptsResource attemptLogResource = attemptLogCr.wrap(IAttemptsResource.class);
                    List<AttemptDTO> attemptList = attemptLogResource.retrieve();

                    for (AttemptDTO dto : attemptList) {
                        if (dto.getAttemptID().equals(attemptID)) {
                            host = dto.getExecHost();
                            endTime = dto.getEndTime();
                            break;
                        }
                    }

                    String date;
                    ClientResource getLogCr = null;

                    if (endTime == null) {
                        date = format.format(new Date());
                    } else {
                        date = format.format(endTime);
                    }

                    if (host.isEmpty()) {
                        getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
                    } else {
                        String url = "http://" + host +":"+ AGENT_PORT + "/api/getlog/" + date + ":" + attemptID + ":" + status;
                        getLogCr = new ClientResource(url);

                    }

                    Representation repLog = getLogCr.get(MediaType.TEXT_HTML);
                    OutputStream output = response.getOutputStream();
                    repLog.write(output);
                    output.close();
            } catch (Exception e) {
                getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
            }

        }else if (action.equals(ERRORLOG)){
            try {
                String host = "";
                Date endTime = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                ClientResource attemptLogCr = new ClientResource(RESTLET_URL_BASE + "attempt");

                IAttemptsResource attemptLogResource = attemptLogCr.wrap(IAttemptsResource.class);
                List<AttemptDTO> attemptList = attemptLogResource.retrieve();

                for (AttemptDTO dto : attemptList) {
                    if (dto.getAttemptID().equals(attemptID)) {
                        host = dto.getExecHost();
                        endTime = dto.getEndTime();
                        break;
                    }
                }

                String date;
                ClientResource getLogCr = null;

                if (endTime == null) {
                    date = format.format(new Date());
                } else {
                    date = format.format(endTime);
                }

                if (host.isEmpty()) {

                    OutputStream output = response.getOutputStream();

                    output.close();
                } else {
                    String url = "http://" + host +":"+ AGENT_PORT + "/api/geterrorlog/" + date + ":" + attemptID + ":" + status;

                    getLogCr = new ClientResource(url);

                }

                Representation repLog = getLogCr.get(MediaType.TEXT_HTML);
               /* String retunStr = repLog.getText();
                String logStr = retunStr.substring(indexOfLog+1,retunStr.length());
                indexOfLog =  retunStr.length();*/


                OutputStream output = response.getOutputStream();
                repLog.write(output);
                output.close();
            } catch (Exception e) {
                OutputStream output = response.getOutputStream();
                output.close();
            }
        }else if(action.equals(RUNLOG)){
            try {
            String host = "";
            Date endTime = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            ClientResource attemptLogCr = new ClientResource(RESTLET_URL_BASE + "attempt");

            IAttemptsResource attemptLogResource = attemptLogCr.wrap(IAttemptsResource.class);
            List<AttemptDTO> attemptList = attemptLogResource.retrieve();

            for (AttemptDTO dto : attemptList) {
                if (dto.getAttemptID().equals(attemptID)) {
                    host = dto.getExecHost();
                    endTime = dto.getEndTime();
                    break;
                }
            }

            String date;
            ClientResource getLogCr = null;

            if (endTime == null) {
                date = format.format(new Date());
            } else {
                date = format.format(endTime);
            }

            if (host.isEmpty()) {

                OutputStream output = response.getOutputStream();

                output.close();
            } else {
                String url = "http://" + host +":"+ AGENT_PORT + "/api/getlog/" + date + ":" + attemptID + ":" + status;

                getLogCr = new ClientResource(url);

            }

            Representation repLog = getLogCr.get(MediaType.TEXT_HTML);
            //String retunStr = repLog.getText();
          //  String logStr = retunStr.substring(indexOfErrorLog+1,retunStr.length());
            //indexOfErrorLog =  repLog.getText().length();
            OutputStream output = response.getOutputStream();
            repLog.write(output);
            output.close();
        } catch (Exception e) {
            OutputStream output = response.getOutputStream();
            output.close();
        }
        }
    }
}
