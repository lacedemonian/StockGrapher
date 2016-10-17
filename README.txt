StockGrapher v1.2.1

NEW IN THIS VERSION:

-Fixed a bug where closing any graph window (but not the query window) would shutdown the program completely.
  -To prevent Program Lingering, the Exit On Close functionality was moved to the Query window, since it allows for easily reopening any accidentally closed window without restarting the program. Organized default window spawn locations.
-Abstractified the Query.
  -The query is no longer case sensitive.
  -The query can be searched with the Enter key.

KNOWN BUGS and INCOMPLETE FEATURES:

The one year period asks for so many specific dates that the Library tries to make them literally smaller than a pixel to compensate. This naturally doesn't work well, and they all blend into a straight line. There is no way around this without editing the Library itself to make it possible to put labels only at certain intervals.

The program does nothing to alert the user if he or she tried to access an invalid or nonexistent file or company. But at least it catches the error and lets the user try again.

Reseting the zoom will make the y-axis start from zero.

The Title will always show NASDAQ as the exchange prefix. I omitted the prefix from the Query for simplicity's sake, but I have yet to fix the title to compensate.

USER INSTRUCTIONS:

1) Place the .csv of any publicly traded company over a 1 year period in the src folder. The file MUST be named as the company code, sans market prefix, in lower case. "NYSE:GE" and "YHOO", for example, should just read "ge.csv" and "yhoo.csv". NASDAQ:GOOG and NASDAQ:FB are included by default.

2) Run StockGrapher.jar

3) The Query window will appear and prompt you for a company code. Type the code, sans market prefix, and either click "Get Graphs" or press Enter. This window will remain open and functional at all times that the program is running.

4) You can click and drag veritcally on graphed area to zoom in or out on a given segment of the y-axis. This cannot be done with the x-axis.

5) You can right click on graphed area to view a list of graph editing functions. These were included with the Library but not all are implemented. Currently you can change the color, y-axis boundaries, titles, and fonts.

6) If you wish to close the program completely you *must* close the Query window.

VERSION HISTORY:

v1.2.0
-Removed all Test Data (commented out sections of code used for Debugging)
-Added Query window on launch.
  -The query allows user to set the file to read without accessing Graph/main().
  -The query will import any .csv of a one-year period from the /src/ folder.
-It turns out it's called "Abstracting" or "Abstractifying". I think i'll use Abstractifying from now on to prevent confusion with the Abstract modifier.

v1.1.1
-Combined the Price and Volume graphing functions into a single function with parameters so it can be used in more situations. This is something I do a lot. Is there a term for it?
  -Intended Effects:
    -Reduce line count, file size.
    -Any numerical stat can be directly tracked.
    -Stats to track are defined when instancing a new Graph object.
  -Side effect that I'm keeping: Rolling Average is now calculated for Volume, too.

v1.1.0
-Added Rolling Average to graph of stock price.
-Charts no longer go from 0 to 800 even though stock price lingers between 600 and 800, which previously made them almost unreadable.

v1.0.1
-fileName is now a Parameter of Ticker's constructor.
  -Ticker no longer needs to be directly accessed for any basic user functions.

v1.0
-Imported JFreeChart Libraries
-Added Graph.java
  -Added ability of Graph.java to produce two graph displays for Price and Volume.

v0.1
-Can read from single defined file.
  -Saves to array.
  -Prints to console.