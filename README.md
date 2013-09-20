GravityGolf
======================

GravityGolf is a 2-d game that simulates the movement of objects in space, where they are affected by the gravitational pull of other planets.

The goal of the game is to launch the ball (typically a small red ball) to a Goal (usually white).

The basic premise of the game is relatively simple; the above mechanics are implemented is less than 100 lines of code.
However, the addition of many small features into one project has made management and design significant challenges.

Read more about the project at http://splewis.net/projects/gravity-golf/


Running from a jar in the Releases Section
-----------------------------------------------

Download and extract the folder. Change into the folder and run:

		java -jar GravityGolf.jar



Building and Running in Eclipse
-------------------------------------

I wrote the project using Eclipse. When the Scala portions were added
the Scala IDE for Eclipse plugin (http://scala-ide.org/index.html) was used.

To import into Eclipse after a git clone:

* Create a new project named GravityGolf
* Import the resources in the cloned folder to the project you created
* Right click the project, select configure, add Scala nature
* You may also need to add JUnit 4, help on errors on JUnit statements/imports
  will let you add it to the build path easily


Building and Running with SBT
---------------------------------------
One day...
