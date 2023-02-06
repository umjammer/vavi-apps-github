[![Release](https://jitpack.io/v/umjammer/vavi-apps-github.svg)](https://jitpack.io/#umjammer/vavi-apps-github)
[![Java CI](https://github.com/umjammer/vavi-apps-github/actions/workflows/maven.yml/badge.svg)](https://github.com/umjammer/vavi-apps-github/actions/workflows/maven.yml)
[![CodeQL](https://github.com/umjammer/vavi-apps-github/actions/workflows/codeql.yml/badge.svg)](https://github.com/umjammer/vavi-apps-github/actions/workflows/codeql-analysis.yml)
![Java](https://img.shields.io/badge/Java-8-b07219)

# vavi-apps-github

how do u remove projects that has the same version?

<img width="400" src="https://user-images.githubusercontent.com/493908/216862475-440e2b11-680a-457b-9eed-741091387a0d.png">

## Goal

```java
 github.packageList("jsapi").filter(Packages::versionFilter("6.3.0")).foreach(Packages::delele);
```

## Install

## References

 * https://github.com/cli/cli
 * https://github.com/hub4j/github-api
   * ~~https://github-api.kohsuke.org/~~ ... idn why i couldn't create subclass of GHObject 
 * https://github.com/spotify/github-java-client

## TODO
