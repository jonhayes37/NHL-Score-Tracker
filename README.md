# NHL Score Tracker 1.3

#### What's new in 1.3:
- The tracker's refresh frequency can now be adjusted from 5 seconds to 1 hour
- Adjusted the flashing animation to make new goals scored more obvious
- Added a setting which allows you to choose how many games to show before adding a scrollbar
- The scrollbar now intelligently scrolls between games when using the mouse wheel or clicking the arrows on the scrollbar
- Adjusted 'Cancel' and 'Save' button sizes in the settings window
- The tracker will now jump to the top window or flash in the taskbar when a goal scored and it is not on top
- Improved the connection error dialog's functionality and clarity
- Added a setting to toggle showing the flash animation when a goal scored
- Small UI tweaks / improvements
- **Bug fix:** All scraping processes now properly close on exit
- **Bug fix:** The tracker will now show starting status for a game ("20:00 1ST") if the scraped data does not contain info about the game time, but the game has started based on system time
- **Bug fix:** The correct date will now be shown when viewing scores on the morning of the first day of a month (e.g. Viewing scores on May 1 before 12:00 PM will now correctly show April 30 as the date of the games)

![](http://puu.sh/hD3MM/fe3f7c6881.png)

### What is NHL Score Tracker?

NHL Score Tracker is a lightweight application that keeps you up to date with the latest hockey scores from around the league. It fetches the latest scores from [Sportsnet](http://www.sportsnet.ca/hockey/nhl/scores/), and displays them in a small, sleek borderless window at the top right corner of your screen.

The interface is split into 'cards' for each game, which contain both team's logos, names, goals scored, and the status of the game (e.g. "12:59 1st", or "FINAL"). You can choose how many games you want to see at a glance, and if there are more games than your preferred number to report, a simple scroll bar appears to easily check all game scores. The refresh interval can be adjusted from 5 seconds to 1 hour, and if you never want to miss an update the tracker can be set to stay on top of all windows, no matter what program you're using!

Projects like NHL Score Tracker take lots of effort and dedication to produce and continually improve. Donations are always appreciated, no matter the size!

[![PayPal](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=3N3QXHX6KJFKG)
