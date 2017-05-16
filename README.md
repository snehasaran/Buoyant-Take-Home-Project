# Buoyant Take Home Project 

RESTful API to provide information about the health of the demo and an endpoint that shifts traffic between two redis clusters

### Prerequisites

1. Java 8 , JRE 1.8.0_131
2. Working Docker environment
3. Set environment variables in your bash profile as follows: 
```	

# Setting path for DOCKER IP
export DOCKER_IP=localhost

# Setting path fo GO
export PATH=$PATH:/usr/local/go/bin

export NAMERCTL_BASE_URL=http://$DOCKER_IP:4180

export GOPATH=/Users/Sneha/go
export PATH=$GOPATH/bin:$PATH

# Setting JAVA_HOME
export JAVA_HOME='/Library/Java/JavaVirtualMachines/jdk1.8.0_131/Contents/Home/jre'
export JAVA_HOME=$(/usr/libexec/java_home)
```	

### Installing

1. Eclipse IDE , version 4.6+ AND Spring Tool Suite(STS). STS can be downloaded from Eclipse Marketplace.
2. Build system used : Maven
3. Language used : Java

## Deployment

* JRE : 1.8, Java 8
* IDE used to write and test code: Eclipse Kepler
* Browser used to test the API: Chrome Version 58
* Tool used for interacting with APIs: Postman

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 

### Docker
* cd to the folder where you've the demo linkerd+docker application saved.
```	
	$ cd <linkerd_demo_location>
```		
* start linkerd-viz on port 3000 on your Docker host
```	
	$ docker-compose build && docker-compose up -d
```		
* open grafana/dashboard using : 
```	
	$ open http://$DOCKER_IP:3000 # on OS X	
```		

  

### Project/Application: 

In a new tab/terminal window, do the following: 

1. cd to the location where you've linkerd_demo_location saved and then do  a 
```	
	git pull 
```		
2. cd to the  sub-folder:
```	
	$ cd demo
```	
3. To add or update all the necessary Maven Wrapper files to your project execute the following command:
```	
	mvn -N io.takari:maven:wrapper
```	

4. compile and run your application	:

```	
	$ ./mvnw spring-boot:run
```	

5. 	If you see the 'Started Application' message in the terminal window, that means application has started successfully.

## GET /health: Health Check

1. Goto the browser and hit the url : 
```	
	$ localhost:8080/health
```		
2. If docker is not started, the API is designed to show the health status as 'down'.
3. When docker is running, the API will hit 4 different end points and return the health of each of them respectiely in a JSON format.
```	
Example: 
{"linkerd":"up","namerd":"up","linkerd_tcp":"up","linkerd_viz":"up"}
```	
4. This capability can also be achieved through the command line by running the following CURL command:
```		
	curl http://localhost:8080/health
```		


## PUT /shift/:N: Traffic Shifting

1. Run the following CURL command (in the <linkerd_demo_location> directory)
	```	 
	curl -s -o /dev/null -w "%{http_code}" localhost:8080/shift/<N>
		where <N> is the percentage of traffic shifted. When N=0, all traffic goes to redis1. 
		When N=100, all traffic goes to redis2 
	```	

2. This command should return a status code of 200, if not then it has resulted in an error which hasn't been handled yet. 

3. After getting a successful status code of 200, we need to run
```	
namerctl dtab get default
	This command fetches namerd's current routing configuration. So using this we can see that the final dtab 
	entry is routing all traffic between the two redis clusters. And then we can also verify the result by 
	looking at the linkerd-viz dashboard. It will show traffic switching from redis1 to redis2.	
	

Output:
	# version AAAAAAAAAAY=
	/svc        => /#/io.l5d.fs ;
	/svc/redis  => <100-N>.00*/svc/redis1 & <N>.00*/svc/redis2 ;		
This output means that N% traffic has been shifted to redis2 cluster and (100-N)% traffic to redis1 cluster.


Example Usage: 
	curl -s -o /dev/null -w "%{http_code}" localhost:8080/shift/30
		output: 200

	namerctl dtab get default

	Output:	
		# version AAAAAAAAAAY=
		/svc        => /#/io.l5d.fs ;
		/svc/redis  => 85.00*/svc/redis1 & 15.00*/svc/redis2 ;	
```		

4. To run this capability in the browser, hit the following URL
	http://localhost:8080/shift/60
5. 	"NO_CONTENT" will appear in the browser window. 
6. Goto the command line and get the current routing configuration by the command: 
```	
namerctl dtab get default
	
Output: 
	/svc        => /#/io.l5d.fs ;
	/svc/redis  => 40.00*/svc/redis1 & 60.00*/svc/redis2 ;	
```	


## Built With

* [Spring Boot](https://spring.io/guides/gs/spring-boot/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [Docker](https://www.docker.com/) - Container Platform
* [GitHub](https://github.com/) - Version Control 
* [Eclipse](http://www.eclipse.org/neon/) - IDE
* [Spring Tool Suite](https://spring.io/tools/sts/all) - STS

## Authors

* **Sneha Saran** 

## Acknowledgments

* README template : https://gist.github.com/PurpleBooth/109311bb0361f32d87a2/
* Maven Wrapper : https://github.com/takari/maven-wrapper