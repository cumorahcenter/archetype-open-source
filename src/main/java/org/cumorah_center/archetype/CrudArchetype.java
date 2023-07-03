package org.cumorah_center.archetype;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * Created by Gabriel Aguilera on 03-05-2022
 */
public abstract class CrudArchetype<T extends PanacheEntityBase, S> {

    private static final Logger LOGGER = LogManager.getLogger(CrudArchetype.class.getName());

    /**
     * Method for persist entity in database
     * @param t
     * @return Response
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(T t) {
        ThreadContext.put("id", UUID.randomUUID().toString());
        LOGGER.log(Level.INFO, "Persist entity: {}", t);
        if (t != null) {
            t.persist();
            if (t.isPersistent()) {
                LOGGER.log(Level.INFO, "Entity status persist : {}", t.isPersistent());
                ThreadContext.clearAll();
                return Response.ok(t).build();
            } else {
                LOGGER.log(Level.WARN, "Entity not persist");
                ThreadContext.clearAll();
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
        LOGGER.log(Level.ERROR, "Entity not exist");
        ThreadContext.clearAll();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * Method for search entity by uuid
     * @param s
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response read(S s) {
        ThreadContext.put("id", UUID.randomUUID().toString());
        LOGGER.log(Level.INFO, "Obtain entity by uuid: {}", s);
        if (s != null) {
            T t = T.findById(s);
            if (t != null) {
                LOGGER.log(Level.DEBUG, "Entity found: {}", t);
                //log.info("Entity found: {}", t);
                ThreadContext.clearAll();
                return Response.ok(t).build();
            } else {
                ThreadContext.clearAll();
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
        ThreadContext.clearAll();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * Method for update entity by uuid
     * @param t
     * @param uuid
     * @return
     */
    @Path("/{uuid}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(T t, @PathParam("uuid") S uuid) {
        ThreadContext.put("id", UUID.randomUUID().toString());
        LOGGER.log(Level.INFO, "Update entity: {} by id: {}", t, uuid);
        if (t != null) {
            t.persist();
            ThreadContext.clearAll();
            return Response.ok(t).build();
        } else {
            ThreadContext.clearAll();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Method for delete logical or physical entity
     * @param t
     * @param uuid
     * @return
     */
    @Path("/{uuid}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response delete(T t, @PathParam("uuid") S uuid) {
        ThreadContext.put("id", UUID.randomUUID().toString());
        LOGGER.log(Level.INFO, "Update entity: {} by id: {}", t, uuid);
        if (t != null) {
            t.delete();
            ThreadContext.clearAll();
            return Response.ok(t).build();
        } else {
            ThreadContext.clearAll();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


}


