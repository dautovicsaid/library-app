package me.fit.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.fit.exception.AuthorException;
import me.fit.model.Author;
import me.fit.service.AuthorService;

@Path("/api/author")
public class AuthorRest {

    @Inject
    private AuthorService authorService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response getAllAuthors() {
        return Response.ok().entity(authorService.getAllAuthors()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAuthorByName")
    public Response getAuthorByName(@QueryParam(value = "name") String name) {
        return Response.ok().entity(authorService.getAuthorsByName(name)).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getAuthorById(@PathParam(value = "id") Long id) {
        return Response.ok().entity(authorService.getAuthorById(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/create")
    public Response createAuthor(Author author) throws AuthorException {
        return Response.ok().entity(authorService.createAuthor(author)).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/delete")
    public Response deleteAuthor(@PathParam(value = "id") Long id) {
        try {
            Author author = authorService.getAuthorById(id);
            authorService.deleteAuthor(id);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/update")
    public Response updateAuthor(@PathParam(value = "id") Long id, Author author) throws AuthorException {
        author.setId(id);
        return Response.ok().entity(authorService.updateAuthor(author)).build();
    }
}
