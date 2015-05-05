# NHL Scraper v1.1

#### What's new in v1.1:
- Replaced the Refresh button with a Settings button, which opens a Settings dialog beside the scraper
- The scraper can now be set to stay on top of all windows, and the scraping interval can be adjusted from 15 seconds to 2 hours
- Settings are now serialized to the file "settings.stg"
- Bug fix: Game time will now properly be red and bolded in the final 5 minutes of regulation
- Bug fix: The winner of a game that has recently ended will now be bolded

![](http://puu.sh/hC21D/78bff86346.png)

### What is NHL Scraper?

NHL Scraper is a lightweight application that keeps you up to date with the latest hockey scores from around the league. It fetches the latest scores from [Sportsnet](http://www.sportsnet.ca/hockey/nhl/scores/), and displays them in a small, borderless
window at the bottom right corner of your screen.

The interface is split into 'cards' for each game, which contain both team's logos, names, goals scored, and the status of the
game (e.g. 12:59 1st, or FINAL). If there are more than 3 games to report, a simple scroll bar appears to easily check all game
scores. The refresh interval can be adjusted from 15 seconds to 2 hours, and if you never want to miss and update, the scraper can be set to stay on top of all windows!
