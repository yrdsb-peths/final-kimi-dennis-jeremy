import greenfoot.*;

public class Enemy extends Actor
{
    public enum State { WALK, HURT, DEATH }

    public int speed;
    public int hp;
    public int maxHp;
    public int xpDrop;
    public int coinDrop;
    public int attackDamage;

    public double worldX;
    public double worldY;

    protected final boolean isElite;
    protected State state = State.WALK;
    protected int animFrame = 0;
    protected int animTimer = 0;
    protected int hurtTimer = 0;
    protected boolean facingLeft = false;
    protected boolean rewardsClaimed = false;

    int attackCooldown = 0;
    static final int ATTACK_INTERVAL = 60;
    static final int BASE_HP = 30;
    static final int BASE_ATTACK_DAMAGE = 5;
    static final int HURT_DURATION = 12;
    static final int ANIM_SPEED = 4;
    static final int SEPARATION_RADIUS = 70;
    static final double SEPARATION_STRENGTH = 2.2;
    static final int SPRITE_SIZE = 48;

    static GreenfootImage[] whiteWalk;
    static GreenfootImage[] whiteHurt;
    static GreenfootImage[] whiteDie;
    static GreenfootImage[] yellowWalk;
    static GreenfootImage[] yellowHurt;
    static GreenfootImage[] yellowDie;

    static
    {
        whiteWalk = loadFrames("Skeleton_01_White_Walk", 10);
        whiteHurt = loadFrames("Skeleton_01_White_Hurt", 5);
        whiteDie = loadFrames("Skeleton_01_White_Die", 13);
        yellowWalk = loadFrames("Skeleton_01_Yellow_Walk", 10);
        yellowHurt = loadFrames("Skeleton_01_Yellow_Hurt", 5);
        yellowDie = loadFrames("Skeleton_01_Yellow_Die", 13);
    }

    public Enemy(double worldX, double worldY, int round)
    {
        this(worldX, worldY, round, false);
    }

    protected Enemy(double worldX, double worldY, int round, boolean isElite)
    {
        this.worldX = worldX;
        this.worldY = worldY;
        this.isElite = isElite;

        int statRound = round;
        speed = 2 + Math.max(0, statRound - 1);
        hp = scaleByRound(BASE_HP, statRound);
        maxHp = hp;
        attackDamage = scaleByRound(BASE_ATTACK_DAMAGE, statRound);
        xpDrop = 3 + statRound / 3;
        coinDrop = 2 + statRound / 5;

        if(isElite)
        {
            hp *= 3;
            maxHp = hp;
            speed += 1;
            attackDamage *= 2;
            xpDrop *= 3;
            coinDrop *= 3;
        }

        setImage(getWalkFrames()[0]);
    }

    public void act()
    {
        if(getWorld() == null)
        {
            return;
        }

        if(state == State.DEATH)
        {
            updateDeathAnimation();
            return;
        }

        followPlayer();
        attackPlayer();
        updateWalkAnimation();
    }

    public void followPlayer()
    {
        if(getWorld() == null || state != State.WALK)
        {
            return;
        }

        if(getWorld() instanceof GameWorld)
        {
            GameWorld gw = (GameWorld)getWorld();
            double dx = gw.player.worldX - worldX;
            double dy = gw.player.worldY - worldY;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if(dist > 0)
            {
                double moveX = dx / dist * speed;
                double moveY = dy / dist * speed;

                double[] separation = getSeparationVector();
                moveX += separation[0] * SEPARATION_STRENGTH;
                moveY += separation[1] * SEPARATION_STRENGTH;

                double moveLen = Math.sqrt(moveX * moveX + moveY * moveY);
                if(moveLen > speed && moveLen > 0)
                {
                    moveX = moveX / moveLen * speed;
                    moveY = moveY / moveLen * speed;
                }

                worldX += moveX;
                worldY += moveY;

                if(moveX < 0)
                {
                    facingLeft = true;
                }
                else if(moveX > 0)
                {
                    facingLeft = false;
                }
            }
            return;
        }

        if(getWorld() instanceof MyWorld)
        {
            MyWorld world = (MyWorld)getWorld();
            Actor target = world.leon != null ? world.leon
                : (world.kaine != null ? world.kaine : world.aurea);
            if(target != null)
            {
                turnTowards(target.getX(), target.getY());
                move(speed);
            }
        }
    }

    private double[] getSeparationVector()
    {
        double sepX = 0;
        double sepY = 0;

        for(Enemy other : getWorld().getObjects(Enemy.class))
        {
            if(other == this || other.state == State.DEATH)
            {
                continue;
            }

            double dx = worldX - other.worldX;
            double dy = worldY - other.worldY;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if(dist > 0 && dist < SEPARATION_RADIUS)
            {
                sepX += dx / dist;
                sepY += dy / dist;
            }
        }

        return new double[] { sepX, sepY };
    }

    public void attackPlayer()
    {
        if(getWorld() == null || state != State.WALK)
        {
            return;
        }

        if(getWorld() instanceof GameWorld)
        {
            GameWorld gw = (GameWorld)getWorld();
            Hero p = gw.player;
            attackCooldown++;

            if(attackCooldown >= ATTACK_INTERVAL)
            {
                double dx = p.worldX - worldX;
                double dy = p.worldY - worldY;

                if(Math.sqrt(dx * dx + dy * dy) < 35)
                {
                    p.takeHit(attackDamage);
                    attackCooldown = 0;
                }
            }
        }
    }

    public boolean takeDamage(int damage)
    {
        if(state == State.DEATH || rewardsClaimed)
        {
            return true;
        }

        hp -= damage;

        if(hp <= 0)
        {
            hp = 0;
            startDeath();
            return true;
        }

        startHurt();
        return false;
    }

    public boolean takeDamage(int damage, Hero source)
    {
        boolean died = takeDamage(damage);

        if(died && source != null && getWorld() instanceof MyWorld)
        {
            source.gainXP(xpDrop);
            source.gainCoin(coinDrop);

            if(getWorld() != null)
            {
                getWorld().removeObject(this);
            }
        }

        return died;
    }

    protected void startHurt()
    {
        state = State.HURT;
        hurtTimer = HURT_DURATION;
        animFrame = 0;
        animTimer = 0;
        applyFrame(getHurtFrames(), animFrame);
    }

    protected void startDeath()
    {
        state = State.DEATH;
        animFrame = 0;
        animTimer = 0;
        applyFrame(getDieFrames(), animFrame);
    }

    private void updateWalkAnimation()
    {
        if(state == State.HURT)
        {
            animTimer++;

            if(animTimer % ANIM_SPEED == 0)
            {
                animFrame++;

                if(animFrame >= getHurtFrames().length)
                {
                    animFrame = getHurtFrames().length - 1;
                }

                applyFrame(getHurtFrames(), animFrame);
            }

            hurtTimer--;

            if(hurtTimer <= 0)
            {
                state = State.WALK;
                animFrame = 0;
                animTimer = 0;
            }

            return;
        }

        if(state != State.WALK)
        {
            return;
        }

        animTimer++;

        if(animTimer % ANIM_SPEED == 0)
        {
            animFrame = (animFrame + 1) % getWalkFrames().length;
            applyFrame(getWalkFrames(), animFrame);
        }
    }

    private void updateDeathAnimation()
    {
        animTimer++;

        if(animTimer % ANIM_SPEED == 0)
        {
            animFrame++;

            if(animFrame < getDieFrames().length)
            {
                applyFrame(getDieFrames(), animFrame);
            }
            else if(!rewardsClaimed)
            {
                finishDefeat();
            }
        }
    }

    protected void finishDefeat()
    {
        if(rewardsClaimed || getWorld() == null)
        {
            return;
        }

        rewardsClaimed = true;

        if(getWorld() instanceof GameWorld)
        {
            ((GameWorld)getWorld()).handleEnemyDefeat(this);
        }
        else if(getWorld() != null)
        {
            getWorld().removeObject(this);
        }
    }

    protected GreenfootImage[] getWalkFrames()
    {
        return isElite ? yellowWalk : whiteWalk;
    }

    protected GreenfootImage[] getHurtFrames()
    {
        return isElite ? yellowHurt : whiteHurt;
    }

    protected GreenfootImage[] getDieFrames()
    {
        return isElite ? yellowDie : whiteDie;
    }

    protected void applyFrame(GreenfootImage[] frames, int index)
    {
        if(frames.length == 0)
        {
            return;
        }

        int safeIndex = Math.max(0, Math.min(index, frames.length - 1));
        GreenfootImage image = new GreenfootImage(frames[safeIndex]);

        if(facingLeft)
        {
            image.mirrorHorizontally();
        }

        setImage(image);
    }

    private static GreenfootImage[] loadFrames(String folder, int count)
    {
        GreenfootImage[] frames = new GreenfootImage[count];

        for(int i = 0; i < count; i++)
        {
            String path = folder + "/tile" + String.format("%03d", i) + ".png";
            frames[i] = new GreenfootImage(path);

            if(frames[i].getWidth() > 0)
            {
                frames[i].scale(SPRITE_SIZE, SPRITE_SIZE);
            }
            else
            {
                frames[i] = new GreenfootImage(SPRITE_SIZE, SPRITE_SIZE);
                frames[i].setColor(isElitePlaceholder(folder) ? Color.YELLOW : Color.WHITE);
                frames[i].fillOval(4, 4, SPRITE_SIZE - 8, SPRITE_SIZE - 8);
            }
        }

        return frames;
    }

    private static boolean isElitePlaceholder(String folder)
    {
        return folder.contains("Yellow");
    }

    private int scaleByRound(int baseValue, int round)
    {
        int multiplierSteps = Math.max(0, round - 1);
        long scaledValue = baseValue;

        for(int i = 0; i < multiplierSteps; i++)
        {
            scaledValue *= 2;

            if(scaledValue > Integer.MAX_VALUE)
            {
                return Integer.MAX_VALUE;
            }
        }

        return (int)scaledValue;
    }
}
