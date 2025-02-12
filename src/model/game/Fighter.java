package model.game;

import util.RandomNumGenerator;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Fighter implements Runnable{
    protected final AtomicInteger hp;
    protected final AtomicBoolean paymentCompleted;
    private static final int MIN_HP = 0;

    protected Fighter(int unitHp, AtomicBoolean paymentCompleted) {
        this.hp = new AtomicInteger(unitHp);
        this.paymentCompleted = paymentCompleted;
    }

    protected abstract void attackProcess();

    public void takeDamage(int damage) {
        hp.addAndGet(-damage);
    }

    public boolean isAlive() {
        return hp.get() > 0;
    }

    public int getHp() {
        return Math.max(hp.get(), MIN_HP);
    }

    protected int generateRandomDamage() {
        return RandomNumGenerator.generate(20, 5);
    }

    protected void waitBeforeAttack() throws InterruptedException {
        Thread.sleep(1000);
    }
}
