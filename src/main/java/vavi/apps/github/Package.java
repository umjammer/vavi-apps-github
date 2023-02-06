/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.github;

import java.util.Date;


/**
 * GHPackages.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2023-02-05 nsano initial version <br>
 */
public class Package {

    String id;
    public String name;
    String package_type;
//    User owner;
    int version_count;
    String visibility;
    Date created_at;
    Date updated_at;
    String html_url;
}
