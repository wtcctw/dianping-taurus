package com.cip.crane.restlet.resource;

import java.util.ArrayList;

import com.cip.crane.restlet.shared.AttemptDTO;
import org.restlet.resource.Get;

/**
 * 
 * IAttemptsResource
 * @author damon.zhu
 *
 */
public interface IAttemptsResource {

   @Get
   public ArrayList<AttemptDTO> retrieve();

}
