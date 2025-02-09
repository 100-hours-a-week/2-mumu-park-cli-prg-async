package service;

import model.game.Enemy;
import model.game.Player;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class GameService {
    private final AtomicBoolean paymentCompleted;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition turnCondition = lock.newCondition();
    public static Player player;
    public static Enemy enemy;

    public GameService(AtomicBoolean paymentCompleted) {
        this.paymentCompleted = paymentCompleted;
        player = new Player(paymentCompleted, lock, turnCondition);
        enemy = new Enemy(paymentCompleted, lock, turnCondition);
    }

    public void startBattle() {
        System.out.println("[🎮] 결제 대기 중! 전투 시작!\n");

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
                System.out.println("[⚠] 전투 중단 감지!");
                break;
            }
        }

        playerThread.interrupt();
        enemyThread.interrupt();
    }
}