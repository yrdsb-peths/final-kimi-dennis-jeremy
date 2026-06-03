import greenfoot.*;

public abstract class Hero extends Actor
{
    // Core stats
    public int hp, maxHp;
    public int xp, coin;
    public int level;
    public int speed, stamina, power;
    public int xpToNextLevel;

    // World position
    public double worldX = 0;
    public double worldY = 0;
    public double moveX = 0;
    public double moveY = 0;

    // Animation state
    public enum State { IDLE, RUN, HIT, DEATH }
    public State state = State.IDLE;
    public int animFrame = 0;
    public int animTimer = 0;
    public static final int ANIM_SPEED = 4;

    // Facing and death
    public boolean facingLeft = false;
    public boolean isDead = false;
    public int hitTimer = 0;

    public Hero()
    {
        hp = 70;
        maxHp = 70;
        xp = 0;
        coin = 0;
        level = 1;
        speed = 4;
        stamina = 3;
        power = 3;
        xpToNextLevel = 10;
    }

    public void act()
    {
        if(getWorld() instanceof GameWorld)
        {
            return;
        }

        updateHero();
    }

    public void updateHero()
    {
        if(isDead)
        {
            onDeathAnimation();
            return;
        }

        readInput();
        updateHitRecovery();
        checkLevelUp();
        updateAnimation();
        checkDead();
    }

    // Input handling
    public void readInput()
    {
        moveX = 0;
        moveY = 0;
        if(state == State.HIT) return;

        if(Greenfoot.isKeyDown("w")) moveY = -speed;
        if(Greenfoot.isKeyDown("s")) moveY =  speed;
        if(Greenfoot.isKeyDown("a")) { moveX = -speed; facingLeft = true;  }
        if(Greenfoot.isKeyDown("d")) { moveX =  speed; facingLeft = false; }

        worldX += moveX;
        worldY += moveY;
    }

    // Damage and death
    public void takeHit(int damage)
    {
        if(isDead) return;
        hp -= damage;
        if(hp <= 0)
        {
            hp = 0;
            isDead = true;
            setState(State.DEATH);
        }
        else
        {
            setState(State.HIT);
            hitTimer = 12; // Hit animation duration
        }
    }

    protected void setState(State newState)
    {
        if(state != newState)
        {
            state     = newState;
            animFrame = 0;
            animTimer = 0;
        }
    }

    private void updateHitRecovery()
    {
        if(state == State.HIT)
        {
            hitTimer--;

            if(hitTimer <= 0)
            {
                setState(State.IDLE);
            }
        }
    }

    // Animation update (implemented per hero)
    protected abstract void updateAnimation();
    protected abstract void onDeathAnimation();

    // Level-up system
    public void gainXP(int amount)   { xp += amount; }
    public void gainCoin(int amount) { coin += amount; }

    public void checkLevelUp()
    {
        if(xp >= xpToNextLevel)
        {
            xp -= xpToNextLevel;
            level++;
            xpToNextLevel = (int)(xpToNextLevel * 1.5);
            speed   += 1;
            stamina += 1;
            power   += 1;
            maxHp   += 10;
            hp       = maxHp;

            if(getWorld() instanceof GameWorld)
                ((GameWorld)getWorld()).addAttributePoint();
        }
    }

    private void checkDead()
    {
        if(hp <= 0 && !isDead)
        {
            isDead = true;
            setState(State.DEATH);
        }
    }

    public int getDamage()
    {
        return 10 + (power - 3) * 5;
    }
}
