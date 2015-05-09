# NHL Score Tracker 1.2

#### What's new in 1.2:
- Replaced the Refresh button with a Settings button, which opens a Settings dialog beside the tracker
- The tracker can now be set to stay on top of all windows, and the scraping interval can be adjusted from 15 seconds to 2 hours
- The tracker will now intelligently sort game cards to keep active games at the top of the list
- Settings are now serialized to the file "settings.stg"
- The tracker now flashes a game card yellow when a goal is scored, and fades back to white over two seconds
- Small UI tweaks
- Bug fix: Game time will now properly be red and bolded in the final 5 minutes of regulation
- Bug fix: The winner of a game that has recently ended will now be bolded
- Bug fix: Games that have not started yet will no longer show the score as 0 - 0
- Bug fix: Games that ended in overtime or shootout will now be sorted and formatted properly
- Bug fix: Changing the refresh frequency now correctly replaces the old frequency
- Bug fix: The date shown in the header of the UI now better reflects the date the games shown were played

![](http://puu.sh/hD3MM/fe3f7c6881.png)

### What is NHL Score Tracker?

NHL Score Tracker is a lightweight application that keeps you up to date with the latest hockey scores from around the league. It fetches the latest scores from [Sportsnet](http://www.sportsnet.ca/hockey/nhl/scores/), and displays them in a small, sleek borderless window at the top right corner of your screen.

The interface is split into 'cards' for each game, which contain both team's logos, names, goals scored, and the status of the
game (e.g. 12:59 1st, or FINAL). If there are more than 3 games to report, a simple scroll bar appears to easily check all game
scores. The refresh interval can be adjusted from 15 seconds to 2 hours, and if you never want to miss an update the tracker can be set to stay on top of all windows, no matter what program you're using!

Any donations are always appreciated!

[![PayPal](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=3N3QXHX6KJFKG)
