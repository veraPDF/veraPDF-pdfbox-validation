veraPDF-pdfbox-validation
=========================
*PDF Box based PDF/A validation, feature extraction and metdata repair developed for veraPDF*

[![Build Status](https://travis-ci.org/veraPDF/veraPDF-pdfbox-validation.svg?branch=integration)](https://travis-ci.org/veraPDF/pdfbox-validation "Travis-CI")
[![Build Status](http://jenkins.openpreservation.org/buildStatus/icon?job=veraPDF-pdfbox-validation)](http://jenkins.openpreservation.org/job/veraPDF-pdfbox-validation/ "OPF Jenkins Release")
[![Build Status](http://jenkins.openpreservation.org/buildStatus/icon?job=veraPDF-pdfbox-validation-dev)](http://jenkins.openpreservation.org/job/veraPDF-pdfbox-validation-dev/ "OPF Jenkins Development")
[![Maven Central](https://img.shields.io/maven-central/v/org.verapdf/verapdf-pdfbox-validation.svg)](http://repo1.maven.org/maven2/org/verapdf/verapdf-pdfbox-validation/ "Maven central")
[![CodeCov Coverage](https://img.shields.io/codecov/c/github/veraPDF/veraPDF-pdfbox-validation.svg)](https://codecov.io/gh/veraPDF/veraPDF-pdfbox-validation/ "CodeCov coverage")
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/9fddd0a05e5b4c4ea57240c2eee55c16)](https://www.codacy.com/app/carlwilson/veraPDF-pdfbox-validation?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=veraPDF/veraPDF-pdfbox-validation&amp;utm_campaign=Badge_Grade "Codacy grade")
Licensing
---------
The veraPDF Parser is dual-licensed, see:

 - [GPLv3+](LICENSE.GPL "GNU General Public License, version 3")
 - [MPLv2+](LICENSE.MPL "Mozilla Public License, version 2.0")

Documentation
-------------
See the [veraPDF documentation site](http://docs.verapdf.org/).

Quick Start
-----------
### Pre-requisites

In order to build the parser you'll need:

 * Java 7, which can be downloaded [from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html), or for Linux users [OpenJDK](http://openjdk.java.net/install/index.html).
 * [Maven v3+](https://maven.apache.org/)

### Building the veraPDF Parser

 1. Download the veraPDF-model repository, either: `git clone https://github.com/veraPDF/veraPDF-parser`
 or download the [latest tar archive](https://github.com/veraPDF/veraPDF-parser/archive/integration.tar.gz "veraPDF-parser latest GitHub tar archive") or [zip equivalent](https://github.com/veraPDF/veraPDF-parser/archive/integration.zip "veraPDF-parser latest GitHub zip archive") from GitHub.
 2. Move to the downloaded project directory, e.g. `cd veraPDF-parser`
 3. Build and install using Maven: `mvn clean install`
