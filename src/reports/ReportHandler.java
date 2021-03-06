package reports;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import controller.GameController;
import results.ResultManager;

import java.util.Objects;
import java.util.Scanner;

/**
 * @brief Oluşturulmuş mapleri kullanarak raporlamaları hazırlayan class
 * 
 */
public class ReportHandler {

	private ArrayList<Integer> playerRankList;
	private HashMap<Integer, String> playerMap;
	private HashMap<Integer, Integer> playerWinMap;
	private HashMap<Integer, Integer> playerLoseMap;
	private HashMap<Integer, Integer> playerScoreMap;
	private HashMap<Integer, ArrayList<Integer>> matchMap;
	static Scanner sc = new Scanner(System.in);

	/**
	 * @brief ResultManager classında oluşturduğumuz mapleri alarak oluşturulmuş constructor.
	 */
	public ReportHandler(ResultManager resultManager) {
		this.playerRankList = resultManager.getPlayerRankList();
		this.playerMap = resultManager.getPlayerMap();
		this.playerWinMap = resultManager.getPlayerWinMap();
		this.playerLoseMap = resultManager.getPlayerLoseMap();
		this.playerScoreMap = resultManager.getPlayerScoreMap();
		this.matchMap = resultManager.getMatchMap();
	}

	/**
	 * @brief Oyuncuların ID, isim, galibiyet, mağlubiyet ve skorlarını, skor
	 *        bazında azalarak raporlayan method
	 * 
	 * @example 1-) 36 Jacquelynn -> G:5 M:1 SKOR:14
	 *		    2-) 13 Hunter -> G:5 M:2 SKOR:13
	 *		    3-) 31 Fernanda -> G:5 M:2 SKOR:13
	 */
	protected void showRankList() {

		if (playerRankList.size() != 0) {
			for (int i = 0; i < playerRankList.size(); i++) {
				Integer playerId = playerRankList.get(i);
				System.out.println(i + 1 + "-) " + (playerId - 1) + " " + playerMap.get(playerId) + " -> G:"
						+ playerWinMap.get(playerId) + " M:" + playerLoseMap.get(playerId) + " SKOR:"
						+ playerScoreMap.get(playerId));
			}
		} else {
			System.out.println("bip - bop. Sistem arızası. (Oyuncu listesi bulunamadı!)");
		}
	}

	/**
	 * @brief Oyuncuların kimlerle maç yaptıklarını ve sonuçlarını raporlamaya yarayan method
	 * 
	 * @example 1-) Wesley -> Micheline: M, 
	 *			2-) Melodie -> Solange: G, Dave: M, Antwan: M, Hunter: M, Marica: G, Windy: M, 
	 * 			
	 */
	private void showMatchResults() {

		for (int i = 0; i < playerMap.size(); i++) {
			String match_results = "";

			for (int j = 0; j < matchMap.get(i + 1).size(); j++) {
				int matchScore = matchMap.get(i + 1).get(j);
				if (matchScore < 0) {
					matchScore *= -1;
				}
				match_results += playerMap.get(matchScore);
				if (matchMap.get(i + 1).get(j) > 0) {
					match_results += ": G, ";
				} else {
					match_results += ": M, ";
				}
			}
			System.out.println((i + 1) + "-) " + playerMap.get(i + 1) + " -> " + match_results);
		}
	}

	/**
	 * @brief Skor listesindeki sıralamaya göre ardışık 2 kişinin maç yapmasını öneren method
	 * 
	 * @example 1-) Jacquelynn VS Hunter
	 *			2-) Fernanda VS Jaye
	 *			3-) Tai VS Denyse
	 * 			
	 */
	private void suggestedNextMatches() {
		int lineIndex = 1;
		for (int i = 0; i < playerRankList.size(); i += 2, lineIndex++) {
			System.out.println(lineIndex + "-) " + playerMap.get(playerRankList.get(i)) + " VS "
					+ playerMap.get(playerRankList.get(i + 1)));
		}
	}

	/**
	 * @brief Oyuncu özelinde sıralamasını gösteren method
	 * 
	 * @example Jeanine sıralamalası: 26
	 * 
	 * @param String username
	 * 			
	 */
	private void showRankByUsername(String username) {
		Integer playerId = getIdByUsername(username);
		if (playerId != -1) {
			System.out.println(username + " sıralamalası: " + (playerRankList.indexOf(playerId) + 1));
		}
	}

	/**
	 * @brief Oyuncu özelinde galibiyet-mağlubiyet sayısını gösteren method
	 * 
	 * @example Jeanine -> G:2 M:3
	 * 
	 * @param String username
	 * 			
	 */
	private void showWinLoseByUsername(String username) {
		Integer playerId = getIdByUsername(username);
		if (playerId != -1) {
			System.out.println(username + " -> G:" + playerWinMap.get(playerId) + " M:" + playerLoseMap.get(playerId));
		}
	}

	/**
	 * @brief Oyuncu özelinde skorunu gösteren method
	 * 
	 * @example Jeanine skoru: 3
	 * 
	 * @param String username
	 * 			
	 */
	private void showScoreByUsername(String username) {
		Integer playerId = getIdByUsername(username);
		if (playerId != -1) {
			System.out.println(username + " skoru: " + playerScoreMap.get(playerId));
		}
	}

	/**
	 * @brief Oyuncu özelinde galibiyet sayısını gösteren method
	 * 
	 * @example Jeanine galibiyet sayısı: 2
	 * 
	 * @param String username
	 * 			
	 */
	private void showWinByUsername(String username) {
		Integer playerId = getIdByUsername(username);
		if (playerId != -1) {
			System.out.println(username + " galibiyet sayısı: " + playerWinMap.get(playerId));
		}
	}

	/**
	 * @brief Oyuncu özelinde mağlubiyet sayısını gösteren method
	 * 
	 * @example Jeanine mağlubiyet sayısı: 3
	 * 
	 * @param String username
	 * 			
	 */
	private void showLoseByUsername(String username) {
		Integer playerId = getIdByUsername(username);
		if (playerId != -1) {
			System.out.println(username + " mağlubiyet sayısı: " + playerLoseMap.get(playerId));
		}
	}

	/**
	 * @brief username parametre alan methodlar için, oyuncunun IDsini dönen method.
	 * 
	 * @note Bu method genel kullanımlar için Utils classı oluşturularak içine konabilirdi.
	 * 
	 * @param String username
	 * 
	 * @return Integer playerID
	 * 			
	 */
	private Integer getIdByUsername(String username) {
		for (Entry<Integer, String> entry : playerMap.entrySet()) {
			if (Objects.equals(username, entry.getValue())) {
				return entry.getKey();
			}
		}
		System.out.println("Hatalı isim girişi!");
		return -1;
	}

	/**
	 * @brief username girilmesi istenen methodlar için kullanıcıdan username alan method
	 * 
	 * @return String playerName
	 * 			
	 */
	private String getUsernameInput() {
		System.out.println("Lütfen oyuncu ismini giriniz:");
		String playerName = sc.next();
		return playerName;
	}

	public static void main(String[] args) throws FileNotFoundException {

		GameController facade = new GameController();
		facade.resultManagerGetInstance().scoreHandler(facade);
		ReportHandler reportHandler = new ReportHandler(facade.resultManagerGetInstance());
		boolean exit = false;

		while (!exit) {
			System.out.println(
					"        **** GAME RANKING ****\nLütfen aşağıdaki seçeneklerden görmek istediğiniz sonucun başındaki numarayı giriniz:\n\n"
							+ "1-) Skor Sıralı Liste\n" + "2-) Maç Sonuçları\n" + "3-) Önerilen Karşılaşma Listesi\n"
							+ "4-) Oyuncu Sıralamasını Göster\n" + "5-) Oyuncu Skorunu Göster\n"
							+ "6-) Oyuncu Galibiyet Mağlubiyet Sayısını Göster\n"
							+ "7-) Oyuncu Galibiyet Sayısını Göster\n" + "8-) Oyuncu Mağlubiyet Sayısını Göster\n");
			int option = sc.nextInt();
			String playerName = "";

			switch (option) {
			case 1:
				reportHandler.showRankList();
				break;
			case 2:
				reportHandler.showMatchResults();
				break;
			case 3:
				reportHandler.suggestedNextMatches();
				break;
			case 4:
				playerName = reportHandler.getUsernameInput();
				reportHandler.showRankByUsername(playerName);
				break;
			case 5:
				playerName = reportHandler.getUsernameInput();
				reportHandler.showScoreByUsername(playerName);
				break;
			case 6:
				playerName = reportHandler.getUsernameInput();
				reportHandler.showWinLoseByUsername(playerName);
				break;
			case 7:
				playerName = reportHandler.getUsernameInput();
				reportHandler.showWinByUsername(playerName);
				break;
			case 8:
				playerName = reportHandler.getUsernameInput();
				reportHandler.showLoseByUsername(playerName);
				break;
			default:
				System.out.println("Yanlış değer girdiniz.");
			}

			System.out.println("\nDevam etmek istiyor musunuz? [E/H]");
			if (sc.next().equalsIgnoreCase("H")) {
				System.out.println("Bye =)");
				exit = true;
			}
		}
	}
}
