/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.github;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.ClientResponse;
import vavi.util.Debug;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


/**
 * Github.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2023-02-06 nsano initial version <br>
 */
@PropsEntity(url = "file:local.properties")
public class Github {

    @Property(name = "github.token")
    private transient String token;

    @Property(name = "github.version")
    private String version = "2022-11-28";

    /** */
    private static Github instance = new Github();

    /** */
    public static Github newInstance() {
        return instance;
    }

    /** */
    private Github() {
        try {
            PropsEntity.Util.bind(this);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /** */
    private Gson gson = new GsonBuilder().create();

    /** */
    private WebTarget target;

    /**
     * TODO how to instantiate MultivaluedMap?
     * @return the builder filled w/ authorization headers
     */
    private Invocation.Builder headersFilledBuilder(Invocation.Builder builder) {
        return builder.header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", version);
    }

    /** Logins the github. */
    public User login() throws IOException {
        Client client = ClientBuilder.newClient(); // DON'T CLOSE
        target = client.target("https://api.github.com/");

        String json = headersFilledBuilder(target.path("user").request())
                .get(String.class);
        return gson.fromJson(json, User.class);
    }

    /** Gets a zen. */
    public String getZen() {
        return headersFilledBuilder(target.path("zen").request())
                .get(String.class);
    }

    /** Gets a packages. */
    public Package[] getPackages(String packageType) {
        String json = headersFilledBuilder(target.path("user/packages")
                .queryParam("package_type", packageType)
                .request())
                .get(String.class);
//Debug.println(json);
        return gson.fromJson(json, Package[].class);
    }

    /** Gets a package version. */
    public Version[] getPackageVersions(Package p) {
        String path = String.format("user/packages/%s/%s/versions", p.package_type, p.name);
        String json = headersFilledBuilder(target.path(path)
                .queryParam("package_type", p.created_at)
                .request())
                .get(String.class);
//Debug.println(json);
        return gson.fromJson(json, Version[].class);
    }

    /** Deletes a package. */
    public void deletePackage(Package p, int versionId) {
        String path = String.format("user/packages/%s/%s/versions/%s", p.package_type, p.name, versionId);
        ClientResponse response = headersFilledBuilder(target.path(path)
                .queryParam("package_type", p.created_at)
                .request())
                .delete(ClientResponse.class);
Debug.println(response.getStatus());
    }
}
