If you need to check in a jar which is not in maven central, you will need to install the jar before adding it to the project pom.xml

Prerequisites:  Maven is installed and on path
                Ant is installed and on path

You may need to copy jar files from static-jar\ant-deps to %MAVEN_HOME%\lib

1.  Create a POM file for your jar based on the edu.mit.jwi_2.3.0.xml pom file
    -- edit the section marked as appropriate --
2.  Edit the project pom.xml and add your dependency using these same values
3.  ant install-static  TODO:  this should be done automatically when running compile
4.  mvn compile