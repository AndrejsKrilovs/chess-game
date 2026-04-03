import './style.css'

const app = document.getElementById("app")!;
const files = ["a","b","c","d","e","f","g","h"];

const createBoard = () => {
    const board = document.createElement("div");
    board.className = "board";

    for (let row = 0; row < 8; row++) {
        for (let col = 0; col < 8; col++) {

            const cell = document.createElement("div");
            const isLight = (row + col) % 2 === 0;
            cell.className = `cell ${isLight ? "light" : "dark"}`;

            const coord = `${files[col]}${8 - row}`;
            cell.dataset.pos = coord;
            if (row === 7) {
                const label = document.createElement("span");
                label.className = "coord bottom";
                label.textContent = files[col];
                cell.appendChild(label);
            }
            if (col === 0) {
                const label = document.createElement("span");
                label.className = "coord left";
                label.textContent = String(8 - row);
                cell.appendChild(label);
            }

            cell.onclick = () => console.log(coord);
            board.appendChild(cell);
        }
    }

    return board;
};

app.appendChild(createBoard());
const ws = new WebSocket("ws://localhost:8080/ws");

ws.onmessage = (event) => {
    const { type, pieces } = JSON.parse(event.data);
    if (type !== "INIT") return;

    document.querySelectorAll(".cell").forEach(c => {
        if (!c.querySelector(".coord")) {
            c.textContent = "";
        }
    });

    pieces.forEach((p: any) => {
        const pos = `${p.coordinates.file}${p.coordinates.rank}`;
        const cell = document.querySelector(`[data-pos="${pos}"]`);
        if (!cell) return;

        cell.appendChild(document.createTextNode(getSymbol(p.type, p.color)));
    });
};

const SYMBOLS: any = {
    Pawn: { WHITE: "♙", BLACK: "♟" },
    Rook: { WHITE: "♖", BLACK: "♜" },
    Knight: { WHITE: "♘", BLACK: "♞" },
    Bishop: { WHITE: "♗", BLACK: "♝" },
    Queen: { WHITE: "♕", BLACK: "♛" },
    King: { WHITE: "♔", BLACK: "♚" }
};

const getSymbol = (type: string, color: string): string => {
    return SYMBOLS[type]?.[color] || "?";
};