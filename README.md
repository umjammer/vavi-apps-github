[![Release](https://jitpack.io/v/umjammer/vavi-apps-github.svg)](https://jitpack.io/#umjammer/vavi-apps-github)
[![Java CI](https://github.com/umjammer/vavi-apps-github/actions/workflows/maven.yml/badge.svg)](https://github.com/umjammer/vavi-apps-github/actions/workflows/maven.yml)
[![CodeQL](https://github.com/umjammer/vavi-apps-github/actions/workflows/codeql.yml/badge.svg)](https://github.com/umjammer/vavi-apps-github/actions/workflows/codeql.yml)
![Java](https://img.shields.io/badge/Java-17-b07219)

# vavi-apps-github

how do u remove packages that has the same version?

<img alt="ghp" width="320" src="https://user-images.githubusercontent.com/493908/216862475-440e2b11-680a-457b-9eed-741091387a0d.png">

## Goal

```java
 github.packageList("jsapi").filter(Packages::versionFilter("6.3.0")).foreach(Packages::delele);
```

## Install

 * [maven](https://jitpack.io/#umjammer/vavi-apps-github)

## References

 * https://docs.github.com/ja/rest
 * https://github.com/cli/cli
 * https://github.com/hub4j/github-api
   * ~~https://github-api.kohsuke.org/~~ ... idn why i couldn't create subclass of GHObject 
 * https://github.com/spotify/github-java-client

## TODO

 * ~~delete packages which has save version.~~
 * ~~list diffs between versions or tags as unified diff~~
 * ~~show release assets download count~~
