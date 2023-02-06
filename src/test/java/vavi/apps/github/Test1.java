/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.github;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.ClientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import vavi.util.Debug;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


@EnabledIf("localPropertiesExists")
@PropsEntity(url = "file:local.properties")
class Test1 {

    static boolean localPropertiesExists() {
        return Files.exists(Paths.get("local.properties"));
    }

    @Property(name = "github.token")
    String token;

    @BeforeEach
    void setup() throws Exception {
        if (localPropertiesExists()) {
            PropsEntity.Util.bind(this);
        }
    }

    Gson gson = new GsonBuilder().create();

    @Test
    @DisplayName("low api")
    void test1() throws Exception {
Debug.println("token: " + token);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.github.com/");

        String zen = target.path("zen").request()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .get(String.class);
Debug.println("zen: " + zen);

        String json = target.path("user").request()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .get(String.class);
Debug.println("user: " + json);
        User user = gson.fromJson(json, User.class);
Debug.println("user: " + user);
    }

    @Test
    @DisplayName("use library")
    void test2() throws Exception {
        Github github = Github.newInstance();
        User user = github.login();
Debug.println("user: " + user.name);
        String zen = github.getZen();
Debug.println("zen: " + zen);

        Package[] packages = github.getPackages("maven");
Arrays.stream(packages).forEach(p -> {
 System.err.println(p.name);
 Version[] versions = github.getPackageVersions(p);
 Arrays.stream(versions).forEach(v -> {
  System.err.println("\t" + v.name);
 });
});
    }

    @Test
    @DisplayName("test remove packages version")
    void test3() throws Exception {
        Github github = Github.newInstance();
        User user = github.login();
Debug.println("user: " + user.name);

        String filter = "org.rococoa.";
        String version = "0.8.3-SNAPSHOT";

        Package[] packages = github.getPackages("maven");
        Arrays.stream(packages).filter(p -> p.name.startsWith(filter)).forEach(p -> {
System.err.println(p.name);
            Version[] versions = github.getPackageVersions(p);
            Arrays.stream(versions).filter(v -> v.name.equals(version)).forEach(v -> {
System.err.println("\t" + v.name);
            });
        });
    }

    @Test
    @Disabled
    @DisplayName("how to get response code?")
    void test4() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.github.com/");
        String json = target.path("zen").request()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .get(String.class);
Debug.println(json);
    }

}

/* */
