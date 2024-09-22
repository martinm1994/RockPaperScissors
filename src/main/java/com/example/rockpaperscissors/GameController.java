package com.example.rockpaperscissors;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.http.HttpSession;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/game")
@SessionAttributes("game")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class GameController {

	private final List<String> MOVES = Arrays.asList("Rock", "Paper", "Scissors");

	public static class Game {
		private String playerMoves = "";
		private String computerMoves = "";
		private String result;

		public String getPlayerMoves() {
			return playerMoves;
		}

		public void setPlayerMoves(String playerMoves) {
			this.playerMoves = playerMoves;
		}

		public String getComputerMoves() {
			return computerMoves;
		}

		public void setComputerMoves(String computerMoves) {
			this.computerMoves = computerMoves;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}
	}

	@PostMapping("/start")
	public ResponseEntity<Game> startGame(HttpSession session) {
		Game game = new Game();
		session.setAttribute("game", game);
		return ResponseEntity.ok(game);
	}

	@PostMapping("/move")
	public ResponseEntity<?> makeMove(@RequestBody Map<String, String> requestBody,
			@SessionAttribute("game") Game game) {
		try {
			if (!requestBody.containsKey("move")) {
				return ResponseEntity.badRequest().body("Mossing 'move' in the request body.");
			}

			String playerMove = requestBody.get("move");

			if (!MOVES.contains(playerMove)) {
				return ResponseEntity.badRequest().body("Invalid move. Valid moves are: Rock, Paper, Scissors.");
			}

			String computerMove = MOVES.get(new Random().nextInt(MOVES.size()));
			game.setPlayerMoves(playerMove);
			game.setComputerMoves(computerMove);

			String result = determineWinner(playerMove, computerMove);
			game.setResult(result);
			return ResponseEntity.ok(game);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An internal error occurred. Please try again later.");
		}
	}

	@PostMapping("/terminate")
	public ResponseEntity<?> terminateGame(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return ResponseEntity.ok(Collections.singletonMap("message", "Game session terminated."));
	}

	private String determineWinner(String playerMove, String computerMove) {
		if (playerMove.equals(computerMove)) {
			return "Draw";
		}
		if ((playerMove.equalsIgnoreCase("rock") && computerMove.equalsIgnoreCase("scissors")) ||
				(playerMove).equalsIgnoreCase("scissors") && computerMove.equalsIgnoreCase("paper") ||
				(playerMove.equalsIgnoreCase("paper") && computerMove.equalsIgnoreCase("rock"))) {
			return "Player";
		}
		return "Computer";
	}
}
