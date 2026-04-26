class EndGameComponent {
  init = (root: HTMLElement) => {
    root.innerHTML = `
			<div class="toast-content">
        <div>Вы действительно хотите завершить игру досрочно?</div>

        <div class="toast-actions">
          <button class="btn btn-end" data-yes>Да</button>
          <button class="btn btn-end" data-no>Нет</button>
        </div>
      </div>
    `
  }
}

export const endGameComponent = new EndGameComponent()