# Migrate

### Team Dank Idea Central
- Christopher Ermel
- Seena Rowhani
- Alexander Maxwell
- Basim Ramadhan

#### About
Our project is a social application that encourages users to explore events that are occurring in an area of interest. The app will calculate the tweet destiny of a geographical location to determine where people are gathering. Then provide users with a live map of where popular events are occurring.


The purpose of the application is to bring people together. The application takes advantage of people sharing content while at interesting events to determine its location. The number of tweets occurring at a given location will be proportional to the number of people at the event. As the event sizes grow, there is a greater likelihood that the event may be of interest to a user.


The application will use an algorithm to calculate the potential interest of the location to a user. The main indicator of the locations popularity will be determined using tweet density. The algorithm will also consider the location of tweets from the people the user is following. The algorithm will take into account the multiple factors to rank the locations.
The locations of interest will be displayed with coloured marker on a map so the user can easily identify where the events are occurring. The size of the marker will grow and shrink based on the popularity rank of the location. The user will be able to select a location on the map to help determine what event is occurring. This will be done by providing tweets from that location. The tweets will provide context to the event occurring through their content, and media associated the tweet (pictures and videos).


The live aspect of our application caters to people who are on the go. As events can occur within a matter of hours, being able to be mobile is a key requirement of our application. For this reason, we believe the use of the application would be most engaging on the mobile platform.
Tweets are often used to share the experience of an event. Our application encourages users to not only experience those events through social media content, but be at the event to experience it themselves. This encourages people to be more social and explore new events and experience new things. Our application provides value by taking advantage of the sharing of social media content, to make social media a truly social experience.


#### Functional Requirements
- The app must provide a map of nodes that represent tweet activity in the close proximity to the user.
- The map must use a Google Maps integration.
- The map must update in real­time in response to the latest tweets.
- The nodes must be sized and coloured. This will be based on:
    - The density of Tweets originating from the node’s geolocation.
    - The number of Tweets coming from people the user follows.
    - The number of Tweets coming from people the user is followed by.
- Each node must be expandable to view the tweets associated with that node. app must authenticate and identify the user via the Twitter Auth API.
app must be able to send notifications to the user on relevant events that are
occurring.
- The user must be able to share a node using Android’s built­in share feature.
- The user must be able to view directions from their current location to nodes displayed
on a map by being redirected to the Google Maps app.
- The app must be built for the Android mobile platform.

#### Non­functional Requirements
- Performance and Scalability: the app must be able to keep up with the demand of receiving/processing large amount of tweets over a live stream
    - If tweets are unable to be processed, then the application will not be able to provide information to the user
 Usability: the map interface must be easy and intuitive to use
- If the user does not understand the information being displayed on the map, then
the application becomes useless as it won’t convey any meaning to them 
- Security: the app must be ensure users’ sensitive twitter profile data is secure
    - If the application allows third parties to obtain the user’s Twitter sign in
information, then the application will lose popularity and will not be used.


#### User Scenarios

##### User Scenario #1

A user can use Migrate to find the hottest events currently happening in their city through the buzz happening on Twitter. To do this, the user opens the Migrate app on their Android mobile device (#6). The user authenticates with Twitter and logs into the app (#2). The user then watches the map (#1) as it populates with nodes in real­time (#1.2), each representing an event happening somewhere in the user’s city. The user opens a node they are interested in to view the Tweets related to the event happening at that location (#1.4). If the user needs directions to the location, they can view directions after being redirected to the Google Maps app (#5).


##### User Scenario #2

A user can use Migrate to monitor for events in the background of their mobile Android device (#6). A notification will pop up once an event has become relevant enough (#3), the user will be able to click the notification to boot up the app and display the event node on the map (#1). The user will then be able to expand the node details to determine the tweet information pertaining to the event (#1.4).
