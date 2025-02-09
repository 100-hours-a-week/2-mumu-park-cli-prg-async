package model.game;

import service.GameService;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Enemy extends Fighter {
    private static final int ENEMY_HP = 50;
    private final ReentrantLock lock;
    private final Condition turnCondition;

    public Enemy(AtomicBoolean paymentCompleted, ReentrantLock lock, Condition turnCondition) {
        super(ENEMY_HP, paymentCompleted);
        this.lock = lock;
        this.turnCondition = turnCondition;
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

        System.out.println("[⚠️] 적이 공격! 데미지 : " + damage +", 적 체력: " + GameService.player.getHp());
    }
}