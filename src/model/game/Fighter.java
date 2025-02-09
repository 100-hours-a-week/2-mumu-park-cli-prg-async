package model.game;

import util.RandomNumGenerator;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Fighter implements Runnable{
    protected final AtomicInteger hp;
    protected final AtomicBoolean paymentCompleted;

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
        return hp.get();
    }

    protected int generateRandomDamage() {
        return RandomNumGenerator.generate(20, 5);
    }

    protected void waitBeforeAttack() throws InterruptedException {
        Thread.sleep(1000);
    }
}
