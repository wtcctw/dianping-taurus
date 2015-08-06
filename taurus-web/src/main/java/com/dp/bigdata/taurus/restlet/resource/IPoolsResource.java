package com.dp.bigdata.taurus.restlet.resource;

import java.util.ArrayList;

import com.dp.bigdata.taurus.restlet.shared.PoolDTO;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * 
 * IPoolsResource
 * @author damon.zhu
 *
 */
public interface IPoolsResource {

   @Get
   public ArrayList<PoolDTO> retrieve();
   
   @Post
   public void create(PoolDTO t);
}
