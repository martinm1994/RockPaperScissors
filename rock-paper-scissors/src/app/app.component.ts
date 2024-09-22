import { Component } from '@angular/core';
import { GameService } from './game.service';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  providers: [GameService]
})
export class AppComponent {
  playerMove: string = '';
  result: string = '';
  computerMove: string = '';
  gameStarted: boolean = false;

  constructor(private gameService: GameService) {}

  startGame() {
    this.gameService.startGame().subscribe(response => {
      this.gameStarted = true;
      this.result = '';
      this.playerMove = '';
      this.computerMove = '';
    });
  }

  makeMove(move: string) {
    this.playerMove = move;
    this.gameService.makeMove(move).subscribe(response => {
      this.computerMove = response.computerMoves;
      this.result = response.result;
    }, error => {
      console.error('Error making move:', error);
      
    });
  }

  terminateGame() {
    this.gameService.terminateGame().subscribe(response => {
      this.gameStarted = false;
    });
  }
}
