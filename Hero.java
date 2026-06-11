import greenfoot.*;

public abstract class Hero extends Actor
{
    public int hp, maxHp;
    public int xp, coin;
    public int level;
    public int speed, stamina, power;
    public int xpToNextLevel;

    public double worldX = 0;
    public double worldY = 0;
    public double moveX = 0;
    public double moveY = 0;

    public enum State { IDLE, RUN, HIT, DEATH }
    public State state = State.IDLE;

    public int animFrame = 0;
    public int animTimer = 0;
    public static final int ANIM_SPEED = 4;

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
    
    private void checkRestart()
    {
        if(isDead && Greenfoot.isKeyDown("space"))
        {
            Greenfoot.setWorld(new TitleScreen());
        }
    }
    
    public void act()
    {
        if(getWorld() instanceof GameWorld || getWorld() instanceof MyWorld)
        {
            updateHero();
        }
    }

    public void updateHero()
    {
        if(isDead)
        {
            onDeathAnimation();
            checkRestart();
            return;
        }
    
        readInput();
        updateHitRecovery();
        checkLevelUp();
        updateAnimation();
        checkDead();
    }
    public void readInput()
    {
        moveX = 0;
        moveY = 0;

        if(state == State.HIT)
        {
            return;
        }

        if(Greenfoot.isKeyDown("w"))
        {
            moveY = -speed;
        }

        if(Greenfoot.isKeyDown("s"))
        {
            moveY = speed;
        }

        if(Greenfoot.isKeyDown("a"))
        {
            moveX = -speed;
            facingLeft = true;
        }

        if(Greenfoot.isKeyDown("d"))
        {
            moveX = speed;
            facingLeft = false;
        }

        worldX += moveX;
        worldY += moveY;

        if(getWorld() instanceof MyWorld)
        {
            setLocation((int)worldX, (int)worldY);
        }
    }

    public void takeHit(int damage)
    {
        if(isDead)
        {
            return;
        }

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
            hitTimer = 12;
        }
    }

    protected void setState(State newState)
    {
        if(state != newState)
        {
            state = newState;
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

    protected abstract void updateAnimation();

    protected abstract void onDeathAnimation();

    public void gainXP(int amount)
    {
        xp += amount;
        checkLevelUp();
    }

    public void gainCoin(int amount)
    {
        coin += amount;
    }

    public void checkLevelUp()
    {
        while(xp >= xpToNextLevel)
        {
            xp -= xpToNextLevel;
            level++;

            xpToNextLevel = (int)(xpToNextLevel * 1.5);

            maxHp += 10;
            hp = maxHp;

            if(getWorld() instanceof GameWorld)
            {
                ((GameWorld)getWorld()).addAttributePoint();
            }
        }
    }

    private void checkDead()
    {
        if(hp <= 0 && !isDead)
        {
            isDead = true;
            setState(State.DEATH);
    
            World world = getWorld();
    
            if(world != null)
            {
                world.showText("GAME OVER", world.getWidth() / 2, world.getHeight() / 2 - 20);
                world.showText("Press SPACE to Restart", world.getWidth() / 2, world.getHeight() / 2 + 20);
            }
        }
    }

    public int getDamage()
    {
        return 10 + (power - 3) * 5;
    }
}
