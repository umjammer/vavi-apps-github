/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.github;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import vavi.util.Debug;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;


@EnabledIf("localPropertiesExists")
@PropsEntity(url = "file:local.properties")
class TestCase {

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
    @DisplayName("raw api")
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
        client.close();
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
    @DisplayName("how to get response code")
    void test4() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.github.com/");
        Response response = target.path("zen").request()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .get();
        int statusCode = response.getStatus();
Debug.println("statusCode: " + statusCode);
        String json = response.readEntity(String.class);
Debug.println(json);
        client.close();
    }

    static class Comparison {
        static class File {
            String filename;
            String patch;
            @Override public String toString() {
                return String.format("diff --git a/%1$s b/%1$s\n--- a/%1$s\n+++ b/%1$s\n", filename) +
                        (patch != null ? patch.replace("\\n", "\n") : "");
            }
        }
        File[] files;
    }

    @Test
    @DisplayName("raw api: compare")
    void test5() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.github.com/");
        String path = "/repos/FabricMC/fabric/compare/1.19_experimental...1.19.3";
        String json = target.path(path).request()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .get(String.class);

        Comparison comparison = gson.fromJson(json, Comparison.class);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        Arrays.stream(comparison.files).forEach(pw::println);
        Path diff = Paths.get("tmp/diff.patch");
        if (!Files.exists(diff.getParent()))
            Files.createDirectories(diff.getParent());
        Files.write(diff, sw.toString().getBytes());
        client.close();
    }

    static class Release {
        int id;
        String tag_name;
        String name;
        String body;
        @Override public String toString() {
            return "Release{" +
                    "id=" + id +
                    ", tag_name='" + tag_name + '\'' +
                    ", name='" + name + '\'' +
                    ", body='" + body.replaceAll("\r\n", "\\\\r\\\\n") + '\'' +
                    '}';
        }
    }

    static class ReleaseAsset {
        int id;
        String name;
        String download_count;
        @Override public String toString() {
            return "ReleaseAsset{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", download_count=" + download_count +
                    '}';
        }
    }

    @Test
    @DisplayName("release assets downloads")
    void test6() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.github.com/");

        String owner = "umjammer";
//        String repo = "vavi-apps-comicviewer";
        String repo = "JustMap";

        String path = String.format("/repos/%s/%s/releases", owner, repo);
        String json = target.path(path).request()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .get(String.class);
        Release[] releases = gson.fromJson(json, Release[].class);
Debug.println(json);
Arrays.stream(releases).forEach(System.err::println);

        int releaseId = releases[0].id; // ordered by date desc.
        path = String.format("/repos/%s/%s/releases/%d/assets", owner, repo, releaseId);
        json = target.path(path).request()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .get(String.class);
        ReleaseAsset[] releaseAssets = gson.fromJson(json, ReleaseAsset[].class);
Arrays.stream(releaseAssets).forEach(System.err::println);
        client.close();
    }

    @Test
    @DisplayName("delete tag")
    void test71() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.github.com/");

        String owner = "umjammer";
        String repo = "vavi-sound-mdsound";
        String ref = "TAG073_";

Debug.println("deleting: " + ref);
        String path = String.format("/repos/%s/%s/git/refs/tags/%s", owner, repo, ref);
        Response response = target.path(path).request()
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .delete();
        int statusCode = response.getStatus();
Debug.println("statusCode: " + statusCode);
        client.close();
    }

    @Test
    @DisplayName("delete tag bulk")
    void test72() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.github.com/");

        String owner = "umjammer";
        String repo = "vavi-sound-mucom88";
        String ref = "TAG%03d";

        for (int i = 1; i <= 183; i++) {
            Debug.println("deleting: " + ref.formatted(i));
            String path = String.format("/repos/%s/%s/git/refs/tags/%s", owner, repo, ref.formatted(i));
            Response response = target.path(path).request()
                    .header("Accept", "application/vnd.github+json")
                    .header("Authorization", "Bearer " + token)
                    .header("X-GitHub-Api-Version", "2022-11-28")
                    .delete();
            int statusCode = response.getStatus();
Debug.println("statusCode: " + statusCode);
            Thread.sleep(3000);
        }
        client.close();
    }
}
