# TeamInfoFinder
This project uses *The Blue Alliance API* to prescout FRC competitions. With regards to FRC, scouting is the collection of data, and typically involves the collection of **primary** data at a competition that is not otherwise provided by FIRST. Prescouting is the collection of data before an event. It typically involves the collection of **secondary** data and requires more manual intervention.

This project aims to automate the gathering/averaging of useful secondary data, such that prescouting, and thus strategising for matches/alliance selection is significantly easier. It does this by providing a rough indication as to the abilities of given teams, by averaging selected statistics across all qualification matches played by that team in a year. The project can be used to prescout any group of teams in any year from 2015 and onwards.

## Getting Started
Currently, there is no formal way to easily deploy and run this project on your computer, as it is still in the pre-alpha development phase. Check back for a formal compiled release later. If you want to run this project currently, clone this repository into the src folder of an eclipse project, and run it from there. See below for more details.

### Prerequisites
This project currently requires the following to run:

* Eclipse Java Edition
* An internet connection
* The latest version of Java

### Installing
1. Download Java Eclipse and place the program in a convenient area of your computer - 
[Eclipse Downloads](http://www.eclipse.org/downloads/eclipse-packages/)
2. Open Eclipse, and create a new Java Project with a name similar to *TeamInfoFinder*
3. Click the *Clone or Download* button at the top right of the main github page for this project and download the project as a Zip.
4. Unzip the downloaded folder and navigate to your Eclipse Workspace in your *File Explorer* - [How to find your workspace address](https://viralpatel.net/blogs/get-eclipse-current-workspace-path/)
5. Navigate inside the *src* folder inside your newly-created project
6. Paste all files contained in the downloaded zip into the *src* folder
7. Navigate to your workspace in Eclipse, right click on the project, and select refresh

You *TeamInfoFinder* is now ready to run! Select the green arrow at the top of the screen to compile and run the project.

### Program Usage
Check back here later after more work is completed on the program UI, and it begins to become more finalised

## Built Using
* [Git](https://git-scm.com/about) is used for version control
* [okHTTP](https://github.com/square/okhttp) is used for TBA API queries
* [JSOn-java](https://github.com/stleary/JSON-java) is used for interpreting pulled JSON data
* [The Blue Alliance](https://www.thebluealliance.com/apidocs/v3) stores and derives FRC data provided by FIRST International. Used to easily pull statistics off the internet in real-time as they are uploaded.

## Contributing
Currently the ability to contribute to this project is not open to the public. Contributing Guidelines will probably be made later. IDK.

## Versioning
Versioning is not properly done at the moment. We might do it later... (http://semver.org/)

## Authors
* **Matthew Brian** - *Initial Work* - [Kludgy4](https://github.com/Kludgy4)

## Acknowledgements
* No-one for the moment. Contribute to the project in any way and we might tip our hat to you down here!

## License
Licensing will later be investigated using [this](https://termsfeed.com/blog/license-software/) website. For the moment, I guess we'll use [GNU](https://www.gnu.org/licenses/gpl-3.0.en.html)!
