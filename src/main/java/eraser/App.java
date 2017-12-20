package eraser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class App {
	
	private static final String cons_key = "";
	private static final String cons_secret = "";
	private static final String access_token = "";
	private static final String access_secret = "";
	
	private Twitter twitter;
	private	ResponseList<Status> statuses = null;
	private	ResponseList<Status> favorites = null;
	private Paging pg = new Paging();
	
	protected Paging getPg() {
		return pg;
	}

	private Long l = 0L;
	
	public String Sample() {
		return "Sample";
	}
	
	public Twitter setup() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(cons_key)
		  .setOAuthConsumerSecret(cons_secret)
		  .setOAuthAccessToken(access_token)
		  .setOAuthAccessTokenSecret(access_secret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		return twitter= tf.getInstance();
	}
	
	public Status postTweet(String message) {
		try {
			Status s = twitter.updateStatus(message);
			System.out.println("Successfully tweeted: " + message);
			return s;
		} catch (TwitterException te) {
			te.printStackTrace();
			System.err.println("Failed to Post a tweet" + te.getMessage());
			return null;
		}
	}
	
	public User verifyCredentials() {
		try {
			User u = twitter.verifyCredentials();
			System.out.println("Successfully verified credentials.");
			return u;
		} catch (TwitterException te) {
			te.printStackTrace();
			System.err.println("Failed to verify" + te.getMessage());
			return null;
		}
	}
	
	public void getFavorites() {
		int pos = 0;
		
		try {
			favorites = twitter.getFavorites(pg);
			pos = favorites.size();
			if(favorites != null && pos > 1) {
				l = favorites.get(pos - 1).getId();
				pg.setMaxId(l);
			}
	        System.out.println("Getting favorites: done.");
		} catch (TwitterException te) {
			te.printStackTrace();
			System.err.println("Failed to get favorites: " + te.getMessage());
		}      
	}
	
	public void destroyFavorite(String id) {
		try {
            twitter.destroyFavorite(Long.parseLong(id));
            System.out.println("Successfully unfavorited status " + id + ".");
        } catch (TwitterException te) {
            te.printStackTrace();
            System.err.println("Failed to unfavorite status: " + te.getMessage());
        }	
	}
	
	public void destroyFavorites() {
		for(Status s : favorites) {
			destroyFavorite(Long.toString(s.getId()));
		}
	}
	
	public void getTweets() {
		try {
			statuses = twitter.getUserTimeline(pg);
			if(statuses != null && statuses.size() > 0) {
	        	l = statuses.get(statuses.size() -1 ).getId();
	        	pg.setMaxId(l);
	            System.out.println("got more 20 tweets, last one is: " + Long.toString(l));
			}
        } catch (TwitterException te) {
            te.printStackTrace();
            System.err.println("Failed to get timeline: " + te.getMessage());
        }
	}
	
	public void untweet(String id) {
		try {
            twitter.destroyStatus(Long.parseLong(id));
            System.out.println("Successfully deleted status [" + id + "].");
        } catch (TwitterException te) {
            te.printStackTrace();
            System.err.println("Failed to delete status: " + te.getMessage());
        }
	}
	
	public void untweet(long id) {
		try {
            twitter.destroyStatus(id);
            System.out.println("Successfully deleted status [" + id + "].");
        } catch (TwitterException te) {
            te.printStackTrace();
            System.err.println("Failed to delete status: " + te.getMessage());
        }
	}
	
	public void destroyTweets() {
		for(Status s : statuses) {
			untweet(s.getId());
		}
	}
	
	public void saveToFile() {
		PrintWriter pWriter = null;
		try {
			pWriter = new PrintWriter(new FileWriter("tweets.txt"));
			for(Status s : this.getStatuses()) {
				pWriter.println(Long.toString(s.getId()));
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(pWriter != null) {
				pWriter.close();				
			}
		}
	}
	
	public void getRate() {
		Map<String, RateLimitStatus> rateLimitStatus;
		try {
			rateLimitStatus = twitter.getRateLimitStatus();
	        for (String endpoint : rateLimitStatus.keySet()) {
	            RateLimitStatus status = rateLimitStatus.get(endpoint);
	            if(status.getLimit() != status.getRemaining()) {
		            System.out.println("Endpoint: " + endpoint);
		            System.out.println(" Limit: " + status.getLimit());
		            System.out.println(" Remaining: " + status.getRemaining());
		            System.out.println(" ResetTimeInSeconds: " + status.getResetTimeInSeconds());
		            System.out.println(" SecondsUntilReset: " + status.getSecondsUntilReset());
	            }
	}
		} catch (TwitterException e) {
			e.printStackTrace();
		}

	}
	
	public void favouriteStuff() {
		for(int i = 0; i < 3; ++i) {
			getFavorites();
			destroyFavorites();
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.err.println("Error in some Thread");
			}
		}
	}
	
	public void tweetstuff() {
		for(int i = 0; i < 5; ++i) {
			getTweets();
			destroyTweets();
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.err.println("erro na thread");
			}
		}
	}
	public static void main(String[] args) {
		App m = new App();
		m.setup();
		m.getPg().setCount(20);
		m.getRate();
		m.favouriteStuff();
	}
	
	protected void setL(Long l) {
		this.l = l;
	}

	protected ResponseList<Status> getStatuses() {
		return statuses;
	}

	protected Twitter getTwitter() {
		return twitter;
	}
}
