import greenfoot.*;

public class Fireball extends Weapon
{
    static GreenfootImage[] frames = new GreenfootImage[44];

    static
    {
        for(int i = 0; i < 44; i++)
        {
            String num = String.format("%03d", i);
            frames[i] = new GreenfootImage("FireBall/tile" + num + ".png");

            if(frames[i].getWidth() <= 0)
            {
                frames[i] = new GreenfootImage(12, 12);
                frames[i].setColor(new Color(255, 120, 40));
                frames[i].fillOval(0, 0, 12, 12);
            }
        }
    }

    int frame = 0;
    int animationTimer = 0;

    double speed = 6;
    double velX;
    double velY;

    public Fireball(double startX, double startY, double targetX, double targetY, int damage)
    {
        super();
        this.worldX = startX;
        this.worldY = startY;
        this.damage = damage;

        double dx = targetX - startX;
        double dy = targetY - startY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if(distance == 0)
        {
            distance = 1;
        }

        velX = dx / distance * speed;
        velY = dy / distance * speed;

        setImage(frames[0]);
        setRotation((int)Math.toDegrees(Math.atan2(dy, dx)));
    }

    @Override
    public void act()
    {
        if(getWorld() == null)
        {
            return;
        }

        worldX += velX;
        worldY += velY;

        if(getWorld() instanceof MyWorld)
        {
            setLocation((int)worldX, (int)worldY);
        }

        animate();
        checkHitEnemy(worldX, worldY, 25);
        checkRange();
    }

    private void animate()
    {
        animationTimer++;

        if(animationTimer % 3 == 0)
        {
            frame = (frame + 1) % frames.length;
            int rotation = getRotation();
            setImage(new GreenfootImage(frames[frame]));
            setRotation(rotation);
        }
    }

    @Override
    protected void onHitEnemy(Enemy enemy, double dx, double dy, double distance)
    {
        World world = getWorld();
        boolean died = enemy.takeDamage(damage);

        if(died)
        {
            if(world instanceof GameWorld)
            {
                GameWorld gameWorld = (GameWorld)world;
                gameWorld.player.gainXP(enemy.xpDrop);
                gameWorld.player.gainCoin(enemy.coinDrop);
            }
            else if(world instanceof MyWorld)
            {
                ((MyWorld)world).giveSelectedPlayerReward(enemy.xpDrop, enemy.coinDrop);
            }

            if(enemy.getWorld() != null)
            {
                world.removeObject(enemy);
            }
        }

        if(getWorld() != null)
        {
            world.removeObject(this);
        }
    }

    private void checkRange()
    {
        if(getWorld() == null)
        {
            return;
        }

        if(getWorld() instanceof MyWorld)
        {
            World world = getWorld();

            if(getX() < 0 || getX() > world.getWidth() || getY() < 0 || getY() > world.getHeight())
            {
                world.removeObject(this);
            }

            return;
        }

        GameWorld gameWorld = (GameWorld)getWorld();
        double dx = worldX - gameWorld.player.worldX;
        double dy = worldY - gameWorld.player.worldY;

        if(Math.sqrt(dx * dx + dy * dy) > 1000)
        {
            gameWorld.removeObject(this);
        }
    }
}
