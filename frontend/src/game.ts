import { BoardView } from "./board";

type Piece = {
    type: string;
    color: string;
    coordinates: { file: string; rank: number };
};

export class Game {
    private ws!: WebSocket;
    private board!: BoardView;
    private pieces: Piece[] = [];
    private selected: string | null = null;
    private currentTurn: string = "WHITE";
    private availableMoves: string[] = [];
    private gameOver = false;

    constructor() {
        this.initUI();
        this.initSocket();
    }

    private initUI() {
        const app = document.getElementById("app")!;
        this.board = new BoardView(this.onCellClick.bind(this));
        this.board.render(app);
    }

    private render(pieces: Piece[]) {
        this.pieces = pieces;
        this.board.clear();
        this.board.clearHighlights();

        pieces.forEach(p => {
            const pos = `${p.coordinates.file}${p.coordinates.rank}`;
            this.board.setPiece(pos, this.getSymbol(p.type, p.color));
        });
    }

    private initSocket() {
        this.ws = new WebSocket("ws://localhost:8080/ws");
        this.ws.onmessage = ({ data }) => {
            const msg = JSON.parse(data);
            this.handleMessage(msg);
        };
    }

    private handleMessage(data: any) {
        switch (data.type) {
            case "INIT":
            case "STATE":
                this.handleState(data);
                break;
            case "MOVES":
                this.handleMoves(data);
                break;
            case "INVALID_MOVE":
                this.resetSelection();
                this.showError(`Некорректный ход. Доступные: ${data.availableMoves.join(", ")}`);
                break;
            case "ERROR":
                this.resetSelection();
                this.showError(data.message);
                break;
        }
    }

    private handleState(data: any) {
        this.currentTurn = data.turn;
        this.render(data.pieces);
        this.resetSelection();

        switch (data.state) {
            case "CHECK":
                this.showInfo("ШАХ!");
                break;
            case "CHECKMATE":
                this.gameOver = true;
                this.showInfo("МАТ! Игра окончена");
                break;
            case "STALEMATE":
                this.gameOver = true;
                this.showInfo("ПАТ! Нет доступных ходов");
                break;
        }
    }

    private handleMoves(data: any) {
        if (!this.selected) return;
        if (!data.moves?.length) {
            this.showError(`Фигура на ${this.selected} не имеет ходов`);
            this.resetSelection();
            return;
        }

        this.availableMoves = data.moves;
        this.board.highlightMoves(this.availableMoves);
    }

    private onCellClick(coord: string) {
        if (this.gameOver) {
            this.showError("Игра завершена");
            return;
        }
        if (!this.selected) {
            this.selectCell(coord);
            return;
        }

        this.makeMove(coord);
    }

    private selectCell(coord: string) {
        const piece = this.getPiece(coord);

        if (!piece) {
            this.showError("Клетка пустая");
            return;
        }
        if (piece.color !== this.currentTurn) {
            this.showError(`Сейчас ходят ${this.getTurnLabel(this.currentTurn)}`);
            return;
        }

        this.selected = coord;
        this.board.clearHighlights();
        this.send("GET_MOVES", { from: coord });
    }

    private makeMove(to: string) {
        this.send("MOVE", { from: this.selected, to });
    }

    private resetSelection() {
        this.selected = null;
        this.availableMoves = [];
        this.board.clearHighlights();
    }

    private send(type: string, payload: any) {
        this.ws.send(JSON.stringify({ type, ...payload }));
    }

    private getPiece(coord: string): Piece | undefined {
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

    private showError(message: string) {
        alert(message);
    }

    private showInfo(message: string) {
        console.log(message);
    }
}