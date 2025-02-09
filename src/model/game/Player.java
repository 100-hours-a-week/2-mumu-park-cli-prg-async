package model.game;

import service.GameService;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Player extends Fighter {
    private static final int PLAYER_HP = 100;
    private final ReentrantLock lock;
    private final Condition turnCondition;

    public Player(AtomicBoolean paymentCompleted, ReentrantLock lock, Condition turnCondition) {
        super(PLAYER_HP, paymentCompleted);
        this.lock = lock;
        this.turnCondition = turnCondition;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            turnCondition.await();
        } catch (InterruptedException e) {
            return;
        } finally {
            lock.unlock();
        }

        while (!paymentCompleted.get() && isAlive() && GameService.enemy.isAlive()) {
            lock.lock();
            try {
                if (paymentCompleted.get()) break;

                waitBeforeAttack();
                attackProcess();

                turnCondition.signal();
                turnCondition.await();
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
        GameService.enemy.takeDamage(damage);

        System.out.println("[⚔️] 내가 공격! 데미지 : " + damage +", 적 체력: " + GameService.enemy.getHp());
    }
}
