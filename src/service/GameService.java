package service;

import constant.exception.ErrorMessage;
import dto.ExceptionDto;
import model.game.Enemy;
import model.game.Player;
import view.OutputView;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class GameService {
    private final AtomicBoolean paymentCompleted;
    private final OutputView outputView;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition turnCondition = lock.newCondition();
    public static Player player;
    public static Enemy enemy;

    public GameService(AtomicBoolean paymentCompleted) {
        this.paymentCompleted = paymentCompleted;
        this.outputView = OutputView.getInstance();
        player = new Player(paymentCompleted, lock, turnCondition);
        enemy = new Enemy(paymentCompleted, lock, turnCondition);
    }

    public void startBattle() {
        outputView.printBattleStart();

        Thread playerThread = new Thread(player);
        Thread enemyThread = new Thread(enemy);

        playerThread.start();
        enemyThread.start();

        lock.lock();
        try {
            turnCondition.signal();
        } finally {
            lock.unlock();
        }

        while (!paymentCompleted.get() && player.isAlive() && enemy.isAlive()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                outputView.handleExceptionMessage(new ExceptionDto(
                        ErrorMessage.INTERRUPTED.getMessage()
                ));
                break;
            }
        }

        playerThread.interrupt();
        enemyThread.interrupt();
    }
}