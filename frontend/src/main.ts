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
                cell.innerHTML += `<span class="coord bottom">${files[col]}</span>`;
            }
            if (col === 0) {
                cell.innerHTML += `<span class="coord left">${8 - row}</span>`;
            }

            cell.onclick = () => console.log(coord);
            board.appendChild(cell);
        }
    }

    return board;
};

app.appendChild(createBoard());