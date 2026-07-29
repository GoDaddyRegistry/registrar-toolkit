# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and test commands

```bash
# Build (requires JDK 8)
./gradlew build

# Unit tests only (fast; excludes PerfTest and SessionTest)
./gradlew test

# Integration tests (depends on unit tests)
./gradlew integrationTest

# Component tests (depends on integration tests; requires JDK 8+)
./gradlew componentTest

# Run a single test class
./gradlew test --tests "godaddy.registry.jtoolkit2.EPPDateFormatterTest"

# Checkstyle (runs automatically as part of build)
./gradlew checkstyleMain

# Full coverage report (merges unit + integration + component)
./gradlew jacocoTestReport
```

## Pre-commit setup (required for contributors)

```bash
brew install pre-commit              # macOS
pre-commit install --install-hooks   # in repo root
```

Pre-commit enforces: no commits to `main`/`master`, trailing whitespace, end-of-file newline, merge conflict markers, large files, JSON validity.

## Architecture

This is a single-module Gradle library (`godaddy.registry:gdrjtk`) that implements an EPP (Extensible Provisioning Protocol) client for GoDaddy Registry. It has no runtime dependencies beyond `commons-codec` and `httpclient`.

### Package structure

| Package | Purpose |
|---|---|
| `godaddy.registry.jtoolkit2.session` | TCP/TLS connection management, `SessionManager`, `SessionPool`, `Transaction` |
| `godaddy.registry.jtoolkit2.se` | EPP service element mappings — every EPP command and response is a class here |
| `godaddy.registry.jtoolkit2.se.<extension>` | GoDaddy Registry proprietary EPP extensions (e.g. `tmch`, `idn`, `variant`, `fee`, `launch`, `secdns`) |
| `godaddy.registry.jtoolkit2.xml` | XML marshalling/unmarshalling layer; `XMLDocument`, `XMLBuilder`, `XmlOutputConfig` |
| `godaddy.registry.jtoolkit2.tmdb` | TMCH (Trademark Clearing House) HTTP client |

### Key design patterns

**Command/Response model** — Every EPP operation is represented as a `Command` subclass paired with a `Response` subclass. Extensions implement `CommandExtension` and are appended to commands via `command.appendExtension(extension)`. Response extensions are registered on the response object before execution via `response.registerExtension(ext)`.

**Session management** — `SessionManagerFactory.newInstance(properties)` returns a thread-safe `SessionManager` backed by a `SessionPool`. Callers invoke `manager.execute(new Transaction(command, response))`. The manager handles connection lifecycle, EPP greeting, and login transparently.

**XML output** — By default, output is prefixless (default namespace on every element). `XmlOutputConfig` controls namespace prefixing; set `xml.output.namespace.prefixes=true` in `toolkit.properties` for registries that require prefixed QNAMEs. The `CBRC-9047` change added `prefixAllExcept(Set<String>)` for selective prefix exclusion.

**Test suites** — Three separate Gradle source sets: `test` (unit), `integrationTest`, `componentTest`. They run in dependency order. Component tests use `ari-mock-http-server` (a local JAR in `lib/`). PerfTest and SessionTest are excluded from the default `test` task.

### Releasing

Versions are set in `gradle.properties` (`toolkitVersion`). The release workflow (`release.yml`) triggers on GitHub release publish and calls `./gradlew publish` to push to GitHub Packages. The `readme.md` must have version numbers matching `toolkitVersion` or the `checkVersionNumbersInReadme` task will fail.
