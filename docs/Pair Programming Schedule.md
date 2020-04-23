### Pair Programming 
Semester 1: Change pairs every week
| Iteration | Pair | Task | Pair | Task | Pair | Task
| ------ | ------ | ------ | ------- | ------- | ------ | ------- |
| 1 | Akshay, Nikhil | Research on use of Android and App development technology | Sarvani, Sujit | Research on P2P and feasibility of Encryption Techniques | Utkarsh, Taranvir | Research on server-side technology and cloud server research
| 2 | Nikhil, Sarvani | Detailed Requirement Specifications for Deliverable 1 | Sujit, Utkarsh | Functional and Technical Architectures | Taranvir, Akshay | Project Development Plan
| 3 | Akshay, Sujit | P2P communicaiton Proof of Concept | Sarvani, Taranvir | Android Application Proof of Concept | Utkarsh, Nikhil | Map API, Infrastructure Setup
| 4 | Sarvani, Utkarsh | Preparing thin slice of Android App, Map API | Taranvir, Akshay | Database Design – Local and Cloud, Continuous Integration | Nikhil, Akshay | Preparing thin slice of P2P Development | 
| 5 | Utkarsh, Akshay | Finalizing thin slice of P2P Mesh | Taranvir, Nikhil | Finalizing thin slice of Map API, Android | Sarvani, Sujit | Finalizing thin slice of Database Design |
| 6 | Taranvir, Sarvani | P2P Communication Core | Nikhil, Akshay | Maps – Routing and Matching | Sujit, Utkarsh | Authentication and Security |
| 7 | Akshay, Sarvani | Application development | Taran, Sujit| Server – Side API | Utkarsh, Nikhil|P2P – Messaging and Chat|
|8|Akshay, Sujit|P2P Services|Nikhil, Sarvani|App Development - UI|Utkarsh, Taranvir|App Development - Core|
|9|Akshay, Taranvir|App Development - Core|Nikhil, Utkarsh|App Development - UI|Sujit, Sarvani|P2P Services|
|10|Akshay, Utkarsh|App Development - UI & Mock Acceptance Tests|Nikhil, Sujit|App Development - Core|Taranvir, Sarvani|P2P Services|
|11|Akshay, Sujit|Server-Side Development|Nikhil, Sarvani|App Development - Core|Utkarsh, Taranvir|Map – Routing and Filtering|
|12|Akshay, Nikhil|Server – Side Development|Sujit, Taranvir|App Development & Mock Acceptance Tests|Utkarsh, Sarvani|Map – Routing and Filtering|
|13|Akshay, Sarvani|Map – Routing and Filtering|Sujit, Nikhil|P2P Services|Utkarsh, Taranvir|App Development|
|14|Akshay, Taranvir|Integration Testing|Sujit, Utkarsh|User Acceptance Testing|Sarvani, Nikhil|Bug Fixing & Mock Acceptance Tests|
|15|Akshay, Nikhil|Bug Fixing|Sujit, Sarvani|Production Testing|Sarvani, Taranvir|Bug Fixing|

Semester 2: Change pairs every two weeks
 | Iteration (Contd.) | Pair | Task | Pair | Task | Pair | Task
| ------ | ------ | ------ | ------- | ------- | ------ | ------- |
|16|Akshay, Nikhil|Isolating the devices on P2P since it is currently visible to anyone on Wifi Direct|Sarvani, Sujit|Communication of multiple devices using P2P|Utkarsh, Taranvir|Building an offline map with tiles and navigation using Graphhopper|
|17|Nikhil, Sarvani|Exploring the DNS - SD approach and build the P2P module|Sujit, Utkarsh|Research on MQTT & Mock Acceptance Tests|Taranvir, Akshay|Offline Route Calculation using Graphhopper|
|18 (No significant work was possible as 4 of the members were travelling back home)|Akshay, Sujit|Resolving the bugs in the offline map|Sarvani, Taranvir|Research and try tools to successfully follow the pair programming paradigm|Utkarsh, Nikhil|Set up integration methods to be used while working from home|
|19|Sarvani, Utkarsh| User Interface |Taranvir, Akshay|Integrating the output of Geocoding component into maps|Nikhil, Akshay|Integration of Peer communication module: convert Kotlin to Java|
|20|Utkarsh, Akshay|User Interface|Taranvir, Sujit|Geocoding in Maps|Sarvani, Nikhil|Integration of Peer communication module & Mock Acceptance Tests|
|21|Nikhil, Utkarsh|Integration of Geocoding on UI|Taranvir, Akshay|Maps & Mock Acceptance Tests|Sujit, Sarvani|P2P Communication|
|22|Akshay, Sarvani|Documentation and Integration|Taran, Sujit|Documentation and Server Communication|Utkarsh, Nikhil|Documentation and User Interface|
### Development

Want to contribute? Great!

Dillinger uses Gulp + Webpack for fast developing.
Make a change in your file and instantaneously see your updates!

Open your favorite Terminal and run these commands.

First Tab:
```sh
$ node app
```

Second Tab:
```sh
$ gulp watch
```

(optional) Third:
```sh
$ karma test
```
#### Building for source
For production release:
```sh
$ gulp build --prod
```
Generating pre-built zip archives for distribution:
```sh
$ gulp build dist --prod
```
### Docker
Dillinger is very easy to install and deploy in a Docker container.

By default, the Docker will expose port 8080, so change this within the Dockerfile if necessary. When ready, simply use the Dockerfile to build the image.

```sh
cd dillinger
docker build -t joemccann/dillinger:${package.json.version} .
```
This will create the dillinger image and pull in the necessary dependencies. Be sure to swap out `${package.json.version}` with the actual version of Dillinger.

Once done, run the Docker image and map the port to whatever you wish on your host. In this example, we simply map port 8000 of the host to port 8080 of the Docker (or whatever port was exposed in the Dockerfile):

```sh
docker run -d -p 8000:8080 --restart="always" <youruser>/dillinger:${package.json.version}
```

Verify the deployment by navigating to your server address in your preferred browser.

```sh
127.0.0.1:8000
```

#### Kubernetes + Google Cloud

See [KUBERNETES.md](https://github.com/joemccann/dillinger/blob/master/KUBERNETES.md)


### Todos

 - Write MORE Tests
 - Add Night Mode

License
----

MIT


**Free Software, Hell Yeah!**

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [dill]: <https://github.com/joemccann/dillinger>
   [git-repo-url]: <https://github.com/joemccann/dillinger.git>
   [john gruber]: <http://daringfireball.net>
   [df1]: <http://daringfireball.net/projects/markdown/>
   [markdown-it]: <https://github.com/markdown-it/markdown-it>
   [Ace Editor]: <http://ace.ajax.org>
   [node.js]: <http://nodejs.org>
   [Twitter Bootstrap]: <http://twitter.github.com/bootstrap/>
   [jQuery]: <http://jquery.com>
   [@tjholowaychuk]: <http://twitter.com/tjholowaychuk>
   [express]: <http://expressjs.com>
   [AngularJS]: <http://angularjs.org>
   [Gulp]: <http://gulpjs.com>

   [PlDb]: <https://github.com/joemccann/dillinger/tree/master/plugins/dropbox/README.md>
   [PlGh]: <https://github.com/joemccann/dillinger/tree/master/plugins/github/README.md>
   [PlGd]: <https://github.com/joemccann/dillinger/tree/master/plugins/googledrive/README.md>
   [PlOd]: <https://github.com/joemccann/dillinger/tree/master/plugins/onedrive/README.md>
   [PlMe]: <https://github.com/joemccann/dillinger/tree/master/plugins/medium/README.md>
   [PlGa]: <https://github.com/RahulHP/dillinger/blob/master/plugins/googleanalytics/README.md>
