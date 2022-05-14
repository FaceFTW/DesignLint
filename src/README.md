# Project Source Tree

The following details the general source tree hierarchy:

- `src/main` contains all the files that will be available in the final binary
  - `LinterMain.java` is a basic CLI Wrapper that interfaces with the main Linter architecture
  - `presentation` Package contains code that serves as the interface between analyzers and a wrapper. This is done in `PresentationLayer.java`
  - `domain` package contains code related to defining analyzer logic. All analyzers will extend the `DomainAnalyzer` class which contains abstract template methods to define specific analyzer functionality.
    - `domain.analyzer` package contains the concrete implementations of analyzers
  - `datasource` package contains code that provides an adapter from the ASM library to the analyzers. The `ASMParser` class serves as this adapter and is used by all analyzers
  - `example` package contains all the example classes that are used by unit/manual testing. No actual code that is executed by the DesignLint binary should be placed in this package.
- `src/test` contains all the relevant code for testing
