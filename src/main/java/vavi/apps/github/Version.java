/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.github;

/**
 * Version.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2023-02-06 nsano initial version <br>
 */
public class Version {

    int id;
    String name;
    String url;
    String package_html_url;
    String created_at;
    String updated_at;
    String html_url;
    static class Metadata {
        String package_type;
        static class Container {
            String[] tags;
        }
    }
}
