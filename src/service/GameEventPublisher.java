package service;

import dto.ExceptionDto;
import dto.GameProgressInfo;
import view.OutputView;

public class GameEventPublisher {
    private final OutputView outputView;

    public GameEventPublisher() {
        this.outputView = OutputView.getInstance();
    }

    public void publishGameStart() {
        outputView.printBattleStart();
    }

    public void publishAttackEvent(GameProgressInfo gameProgressInfo) {

        outputView.printGameProgress(gameProgressInfo);
    }

    public void publishErrorEvent(String errorMessage) {
        outputView.handleExceptionMessage(new ExceptionDto(errorMessage));
    }
}
