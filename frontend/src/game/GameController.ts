class GameController {
	control = (eventBus: EventBus) => {
		eventBus.on("START_GAME", ({ color }) => eventBus.emit("WS_SEND", { type: "START_GAME", payload: { color } }))
		eventBus.on("END_GAME", () => eventBus.emit("WS_SEND", { type: "END_GAME" }))
	}
}

export const gameController = new GameController()