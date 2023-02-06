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

    /** */
    private String version = "2022-11-28";

    /** */
    public User login() throws IOException {
        Client client = ClientBuilder.newClient();
        target = client.target("https://api.github.com/");

        String json = target.path("user").request()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", version)
                .get(String.class);
        return gson.fromJson(json, User.class);
    }

    /** */
    public String getZen() {
        return target.path("zen").request()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", version)
                .get(String.class);
    }

    /** */
    public Package[] getPackages(String packageType) {
        String json = target.path("user/packages")
                .queryParam("package_type", packageType)
                .request()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", version)
                .get(String.class);
//Debug.println(json);
        return gson.fromJson(json, Package[].class);
    }

    /** */
    public Version[] getPackageVersions(Package p) {
        String json = target.path(String.format("user/packages/%s/%s/versions", p.package_type, p.name))
                .queryParam("package_type", p.created_at)
                .request()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", version)
                .get(String.class);
//Debug.println(json);
        return gson.fromJson(json, Version[].class);
    }

    /** */
    public void deletePackage(Package p, int versionId) {
        ClientResponse response = target.path(String.format("user/packages/%s/%s/versions/%s", p.package_type, p.name, versionId))
                .queryParam("package_type", p.created_at)
                .request()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", version)
                .delete(ClientResponse.class);
Debug.println(response.getStatus());
    }
}
