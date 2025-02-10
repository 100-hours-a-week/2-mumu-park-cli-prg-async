package model.game;

import dto.GameProgressInfo;
import service.GameService;
import view.OutputView;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Enemy extends Fighter {
    private static final int ENEMY_HP = 50;
    private final ReentrantLock lock;
    private final Condition turnCondition;
    private final OutputView outputView;

    public Enemy(AtomicBoolean paymentCompleted, ReentrantLock lock, Condition turnCondition) {
        super(ENEMY_HP, paymentCompleted);
        this.lock = lock;
        this.turnCondition = turnCondition;
        this.outputView = OutputView.getInstance();
    }

    @Override
    public void run() {
        while (!paymentCompleted.get() && isAlive() && GameService.player.isAlive()) {
            lock.lock();
            try {
                turnCondition.await();
                if (paymentCompleted.get()) break;

                waitBeforeAttack();
                attackProcess();

                turnCondition.signal();
            } catch (InterruptedException e) {
                break;
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    protected void attackProcess() {
        int damage = generateRandomDamage();
        GameService.player.takeDamage(damage);

        outputView.printGameProgress(new GameProgressInfo(
                false, damage, GameService.player.getHp()
        ));
    }
}