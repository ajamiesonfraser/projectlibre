# Libraries added by the UI/UX refresh

Bundled into `openproj-contrib.jar` by `openproj_contrib/build.xml`.

| Jar | Version | Why | License |
|---|---|---|---|
| `flatlaf/flatlaf-3.5.4.jar` | 3.5.4 | Modern Swing look and feel (default theme) | Apache License 2.0 |
| `jaxb/jaxb-api-2.3.1.jar` | 2.3.1 | JAXB API, removed from the JDK in Java 11 (used by MS Project XML import/export) | CDDL 1.1 / GPL 2.0 with Classpath Exception |
| `jaxb/jaxb-impl-2.3.9.jar` | 2.3.9 | JAXB runtime | Eclipse Distribution License 1.0 (BSD-3-Clause) |
| `jaxb/javax.activation-1.2.0.jar` | 1.2.0 | JavaBeans Activation, required by JAXB | CDDL 1.1 / GPL 2.0 with Classpath Exception |

All were downloaded unmodified from Maven Central:

    mvn dependency:copy -Dartifact=com.formdev:flatlaf:3.5.4 -DoutputDirectory=flatlaf
    mvn dependency:copy -Dartifact=javax.xml.bind:jaxb-api:2.3.1 -DoutputDirectory=jaxb
    mvn dependency:copy -Dartifact=com.sun.xml.bind:jaxb-impl:2.3.9 -DoutputDirectory=jaxb
    mvn dependency:copy -Dartifact=com.sun.activation:javax.activation:1.2.0 -DoutputDirectory=jaxb
