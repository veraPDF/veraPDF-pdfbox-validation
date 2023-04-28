veraPDF-pdfbox-validation
=========================
*PDF Box based PDF/A and PDF/UA validation, feature extraction and metadata repair developed for veraPDF*

[![Build Status](https://jenkins.openpreservation.org/job/veraPDF/job/1.24rc/job/pdfbox-validation-jakarta/badge/icon)](https://jenkins.openpreservation.org/job/veraPDF/job/1.24rc/job/pdfbox-validation-jakarta/ "OPF Jenkins")
[![Maven Central](https://img.shields.io/maven-central/v/org.verapdf/pdfbox-validation-model.svg)](https://repo1.maven.org/maven2/org/verapdf/pdfbox-validation-model/ "Maven central")
[![CodeCov Coverage](https://img.shields.io/codecov/c/github/veraPDF/veraPDF-pdfbox-validation.svg)](https://codecov.io/gh/veraPDF/veraPDF-pdfbox-validation/ "CodeCov coverage")
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/216fae32f94541e694228d96527aee5c)](https://app.codacy.com/gh/veraPDF/veraPDF-pdfbox-validation/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade "Codacy coverage")

[![GitHub issues](https://img.shields.io/github/issues/veraPDF/veraPDF-library.svg)](https://github.com/veraPDF/veraPDF-library/issues "Open issues on GitHub")
[![GitHub issues](https://img.shields.io/github/issues-closed/veraPDF/veraPDF-library.svg)](https://github.com/veraPDF/veraPDF-library/issues?q=is%3Aissue+is%3Aclosed "Closed issues on GitHub")
[![GitHub issues](https://img.shields.io/github/issues-pr/veraPDF/veraPDF-pdfbox-validation.svg)](https://github.com/veraPDF/veraPDF-pdfbox-validation/pulls "Open pull requests on GitHub")
[![GitHub issues](https://img.shields.io/github/issues-pr-closed/veraPDF/veraPDF-pdfbox-validation.svg)](https://github.com/veraPDF/veraPDF-pdfbox-validation/pulls?q=is%3Apr+is%3Aclosed "Closed pull requests on GitHub")

Licensing
---------
The veraPDF Parser is dual-licensed, see:

 - [GPLv3+](LICENSE.GPL "GNU General Public License, version 3")
 - [MPLv2+](LICENSE.MPL "Mozilla Public License, version 2.0")

Documentation
-------------
See the [veraPDF documentation site](https://docs.verapdf.org/).

Quick Start
-----------
### Pre-requisites

In order to build the parser you'll need:

 * Java 9 - 17, which can be downloaded [from Oracle](https://www.oracle.com/technetwork/java/javase/downloads/index.html), or for Linux users [OpenJDK](https://openjdk.java.net/install/index.html).
 * [Maven v3+](https://maven.apache.org/)

### Building the veraPDF PDF Box Validation Model

 1. Download the veraPDF-model repository, either: `git clone https://github.com/veraPDF/veraPDF-pdfbox-validation`
 or download the [latest tar archive](https://github.com/veraPDF/veraPDF-pdfbox-validation/archive/integration.tar.gz "veraPDF-pdfbox-validation latest GitHub tar archive") or [zip equivalent](https://github.com/veraPDF/veraPDF-pdfbox-validation/archive/integration.zip "veraPDF-pdfbox-validation latest GitHub zip archive") from GitHub.
 2. Move to the downloaded project directory, e.g. `cd veraPDF-parser`
 3. Build and install using Maven: `mvn clean install`
