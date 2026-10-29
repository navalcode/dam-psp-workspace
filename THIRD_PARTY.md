# Components and attribution

Original coursework, notes, history and existing notices remain. This review does not add a blanket license over historical code.

- Spring Boot 2.5.4/2.5.5, Thymeleaf, Hibernate, H2, Lombok and test libraries retain their declared versions and notices. No dependency upgrade or new runtime library was introduced.
- All four original Takari Maven Wrapper 0.5.6 JARs were compared with the official Maven Central artifact before running them. This was an audit-time comparison; the historical scripts do not enforce a modern runtime checksum gate. [Official Maven Wrapper documentation](https://maven.apache.org/wrapper/).
- Outfit is redistributed locally from the reviewed Google Fonts distribution under SIL OFL 1.1. The complete copyright/license [notice](demo/src/main/resources/static/fonts/OFL.txt) accompanies the font. [Upstream font directory](https://github.com/google/fonts/tree/main/ofl/outfit). No runtime font-provider call is made.
- New test data is fictional; model emails use example.invalid. Screenshots are actual local app pixels. Generated visual concepts stay internal and are not presented as runtime screenshots.

Formatting and research tooling are outside the source export. Existing repository history and unrelated files are preserved.
