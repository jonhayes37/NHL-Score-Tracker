package mainPackage;

public class Game {
	
	private short homeGoals;
	private short awayGoals;
	private String homeTeam;
	private String awayTeam;
	private String gameTime;
	
	public Game(String homeTeam, String awayTeam, short homeGoals, short awayGoals, String time) {
		this.homeGoals = homeGoals;
		this.awayGoals = awayGoals;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.gameTime = time;
	}
	
	public void updateGoals(String team, short numGoals) {
		if (this.homeTeam.equals(team)) {
			this.homeGoals = numGoals;
		}else if (this.awayTeam.equals(team)) {
			this.awayGoals = numGoals;
		}
	}
	
	public boolean isTied() {
		return this.homeGoals == this.awayGoals;
	}
	
	public boolean scoreChanged(Game otherGame) {
		return (this.homeGoals + this.awayGoals) == (otherGame.getHomeGoals() + otherGame.getAwayGoals());
	}
	
	public boolean equals(Game g) {
		return this.homeTeam == g.getHomeTeam() && this.awayTeam == g.getAwayTeam();
	}
	
	public short getHomeGoals() { return this.homeGoals; }
	public short getAwayGoals() { return this.awayGoals; }
	public String getHomeTeam() { return this.homeTeam; }
	public String getAwayTeam() { return this.awayTeam; }
	public String getGameTime() { return this.gameTime; }
	public void setGameTime(String time) { this.gameTime = time; }
}
