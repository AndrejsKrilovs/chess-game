import { BoardView  } from "./board";

export class Game {
    private ws: WebSocket;

    constructor() {
        this.initUI();
        this.initSocket();
    }

    private initUI() {
        const app = document.getElementById("app")!;
        const board = new BoardView(this.onCellClick.bind(this));
        board.render(app);
    }

    private initSocket() {
        this.ws = new WebSocket("ws://localhost:8080/ws");
        this.ws.onmessage = (event) => {
            const data = JSON.parse(event.data);
            console.log(data)
            if (data.type === "INIT") {
                this.render(data.pieces);
            }
        };
    }

    private render(pieces: any[]) {
        document.querySelectorAll(".cell").forEach(c => {
            if (!c.querySelector(".coord")) {
                c.textContent = "";
            }
        });

        pieces.forEach(p => {
            const pos = `${p.coordinates.file}${p.coordinates.rank}`;
            const cell = document.querySelector(`[data-pos="${pos}"]`);
            if (!cell) return;
            cell.appendChild(
                document.createTextNode(this.getSymbol(p.type, p.color))
            );
        });
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

		private onCellClick(coord: string) {
				console.log(coord);
    }
}