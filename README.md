# ChatGPT API used with Java & Spring

## How to get it running
* Clone this GIT project.
* Make sure it is a Maven project and Maven is executed to load dependencies.
* Create an Account at https://openai.com & log in
* Create API key at https://beta.openai.com/account/api-keys
* Store the key in application.properties file in cloned project.
* Start it as Spring Boot application.
* For chatting with ChatGPT: http://localhost:8080/
* For drawing images with DALL-E: http://localhost:8080/image

## Setup proxy
If you need a proxy to communicate with Internet (ChatGPT API is in Internet), adapt ChatGptController.java like this:
* Replace: private HttpClient client = HttpClient.newHttpClient();
* with: private HttpClient client = HttpClient.newBuilder().proxy(ProxySelector.of(InetSocketAddress.createUnresolved("proxy.host.com", 8080))).build();

## More information
* OpenAI API documentation: https://beta.openai.com/docs/api-reference/completions/create
* Visit Elmar Brauch's German blog to read in general about Spring & Java:
https://agile-coding.blogspot.com
