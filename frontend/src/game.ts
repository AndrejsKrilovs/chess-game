import { BoardView  } from "./board";

export class Game {
    private ws: WebSocket;
    private board!: BoardView;
    private pieces: any[] = [];
    private selected: string | null = null;
    private currentTurn: string = "WHITE";
    private availableMoves: string[] = [];

    constructor() {
        this.initUI();
        this.initSocket();
    }

    private initUI() {
        const app = document.getElementById("app")!;
        this.board = new BoardView(this.onCellClick.bind(this));
        this.board.render(app);
    }

    private initSocket() {
        this.ws = new WebSocket("ws://localhost:8080/ws");
        this.ws.onmessage = (event) => {
            const data = JSON.parse(event.data);
            switch (data.type) {
                case "INIT":
                case "STATE":
										this.currentTurn = data.turn;
                    this.render(data.pieces);
                    break;
                case "MOVES":
										console.log(data.moves);
                    if (!this.selected) return;
                    if (!data.moves?.length) {
                        this.showError(`Фигура на ${this.selected} не имеет ходов`);
                        this.selected = null;
                        this.board.clearHighlights();
                        return;
                    }
                    this.availableMoves = data.moves;
                    this.board.highlightMoves(this.availableMoves);
                    break;
                case "INVALID_MOVE":
                    this.selected = null;
                    this.showError(`Некорректный ход. Доступные ходы: ${data.availableMoves.join(", ")}`);
                    break;
                case "ERROR":
                    this.selected = null;
                    this.showError(data.message);
                    break;
            }
        };
    }

    private render(pieces: any[]) {
        this.pieces = pieces;
        this.board.clear();
        this.board.clearHighlights();

        pieces.forEach(p => {
            const pos = `${p.coordinates.file}${p.coordinates.rank}`;
            this.board.setPiece(pos, this.getSymbol(p.type, p.color));
        });
    }

    private hasPiece(coord: string): boolean {
        return this.pieces.some(
            p => `${p.coordinates.file}${p.coordinates.rank}` === coord
        );
    }

    private onCellClick(coord: string) {
				const piece = this.getPiece(coord);
        if (!this.selected && !this.hasPiece(coord)) {
            this.showError("Клетка пустая");
            return;
        }
				if (!this.selected && piece.color !== this.currentTurn) {
          this.showError(`Сейчас ходят ${this.getTurnLabel(this.currentTurn)}`);
          return;
        }
        if (!this.selected) {
            this.selected = coord;
            this.board.clearHighlights();
            this.ws.send(JSON.stringify({
                type: "GET_MOVES",
                from: coord
            }));
            return;
        }

        this.ws.send(JSON.stringify({
            type: "MOVE",
            from: this.selected,
            to: coord
        }));
        this.selected = null;
        this.availableMoves = [];
        this.board.clearHighlights();
    }

    private showError(message: string) {
        alert(message);
    }

		private getPiece(coord: string): any | undefined {
        return this.pieces.find(
            p => `${p.coordinates.file}${p.coordinates.rank}` === coord
        );
    }

    private getSymbol(type: string, color: string): string {
        const map: any = {
            Pawn: { WHITE: "♙", BLACK: "♟" },
            Rook: { WHITE: "♖", BLACK: "♜" },
            Knight: { WHITE: "♘", BLACK: "♞" },
            Bishop: { WHITE: "♗", BLACK: "♝" },
            Queen: { WHITE: "♕", BLACK: "♛" },
            King: { WHITE: "♔", BLACK: "♚" }
        };

        return map[type]?.[color] || "?";
    }

		private getTurnLabel(color: string): string {
        return { WHITE: "белые", BLACK: "чёрные" }[color] || color;
    }
}