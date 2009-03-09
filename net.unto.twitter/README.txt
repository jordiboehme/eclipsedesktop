=Java Twitter=

_A Java wrapper around the Twitter API_

Author: `DeWitt Clinton <dewitt@unto.net>`

==Introduction==

This library provides a pure Java interface for the Twitter API.

Twitter (http://twitter.com) provides a service that allows people to
connect via the web, IM, and SMS.  Twitter exposes a web services API 
(http://twitter.com/help/api) and this library is intended to make
it even easier for java programmers to use. 

==Using==

Download the latest source or binrary version of java-twitter from:

  http://code.google.com/p/java-twitter/downloads/list
  
===Runtime Dependencies===

The java-twitter library requires that the following runtime dependencies are installed in the CLASSPATH:

  * [http://commons.apache.org/lang/ Apache Commons Lang]
  * [http://jakarta.apache.org/httpcomponents/httpclient-3.x/ Apache Jakarta Commons HttpClient]
  * [http://joda-time.sourceforge.net/ Joda Time]
  * [http://json-lib.sourceforge.net/ Json-lib]

===Examples===

Connect to Twitter and print out the latest public messages:

{{{
  import net.unto.twitter.Api;
  import net.unto.twitter.Status;
   
  Api api = new Api();
  for (Status status : api.getPublicTimeline()) {
    System.out.println(String.format("%s wrote '%s'", status.getUser().getName(), status.getText()));
  }
}}}

Send a Twitter message:

{{{
  import net.unto.twitter.Api;
  
  Api api = new Api("username", "password");
  api.updateStatus("This is a test message.");
}}}


==Building==

*Using Maven*

Install Java 1.5 or greater:

  http://java.sun.com/j2se/1.5.0/
  
Install Maven:

  http://maven.apache.org/

Maven will automatically fetch the runtime and test dependencies:

Run the following:

{{{
  $ mvn package 
}}}

The JAR file will be generated as:

  target/twitter-0.0.1.jar

==Javadocs==

Generate the javadocs for the project with:

{{{
  $ mvn javadoc:javadoc
}}}

The generated javadocs can be found at:

  target/site/apidocs/index.html

==More Information==

Please visit http://groups.google.com/group/java-twitter for more discussion.

==License==

{{{
  Copyright 2007 DeWitt Clinton All Rights Reserved.
  
  Licensed under the Apache License, Version 2.0 (the 'License');
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
      http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an 'AS IS' BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
}}}