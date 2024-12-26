package org.example.exam2.cucumber;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.glue", value = "org.example.exam2.cucumber")
public class RunCucumberTicketCal {
}