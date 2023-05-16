package org.example.register.resource;

import org.example.register.request.RegisterRequest;
import org.example.register.response.RegisterResponse;
import org.example.register.service.RegisterService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/api")
public class RegisterResource {

    private RegisterService registerService = new RegisterService();

    @POST
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(RegisterRequest request) {
        registerService.postUser(request);
        RegisterResponse response = new RegisterResponse();
        response.setSuccess(true);
        response.setMessage("User created successfully");
        return Response.ok(response).build();
    }

    @PUT
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, RegisterRequest request) {
        // Check if user exists
        RegisterResponse response = new RegisterResponse();
        if (registerService.getUserById(id) == null) {
            response.setSuccess(false);
            response.setMessage("User not found");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }
        // Update user
        registerService.updateUser(id, request);
        response.setSuccess(true);
        response.setMessage("User updated successfully");
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Long id) {
        // Check if user exists
        RegisterResponse response = new RegisterResponse();
        if (registerService.getUserById(id) == null) {
            response.setSuccess(false);
            response.setMessage("User not found");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }
        // Delete user
        registerService.deleteUser(id);
        response.setSuccess(true);
        response.setMessage("User deleted successfully");
        return Response.ok(response).build();
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        return Response.ok(registerService.getAllUsers()).build();
    }

    @GET
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id) {
        RegisterResponse response = new RegisterResponse();
        RegisterRequest userRequest = registerService.getUserById(id);
        if (userRequest == null) {
            response.setSuccess(false);
            response.setMessage("User not found");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }
        return Response.ok(userRequest).build();
    }

}
