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
    double velX, velY;

    public Fireball(double startX, double startY, double targetX, double targetY, int damage)
    {
        super();
        this.worldX = startX;
        this.worldY = startY;
        this.damage = damage;

        double dx = targetX - startX;
        double dy = targetY - startY;

        double dist = Math.sqrt(dx * dx + dy * dy);

        if(dist == 0)
        {
            dist = 1;
        }

        velX = dx / dist * speed;
        velY = dy / dist * speed;

        setImage(frames[0]);
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        setRotation((int)angle);
    }

    @Override
    public void act()
    {
        if(getWorld() == null) return;

        worldX += velX;
        worldY += velY;

        World world = getWorld();

        if(world instanceof MyWorld)
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

            int rot = getRotation();

            setImage(new GreenfootImage(frames[frame]));
            setRotation(rot);
        }
    }

    @Override
    protected void onHitEnemy(Enemy e, double dx, double dy, double dist)
    {
        GameWorld gw = (GameWorld)getWorld();
        boolean died = e.takeDamage(damage);
        if(died)
        {
            gw.player.gainXP(e.xpDrop);
            gw.player.gainCoin(e.coinDrop);
            if(e.getWorld() != null) gw.removeObject(e);
        }
        if(getWorld() != null) gw.removeObject(this);
    }

    private void checkRange()
    {
        if(getWorld() == null) return;

        if(getWorld() instanceof MyWorld)
        {
            World world = getWorld();

            if(getX() < 0 || getX() > world.getWidth() || getY() < 0 || getY() > world.getHeight())
            {
                world.removeObject(this);
            }

            return;
        }

        GameWorld gw = (GameWorld)getWorld();
        double dx = worldX - gw.player.worldX;
        double dy = worldY - gw.player.worldY;
        if(Math.sqrt(dx*dx + dy*dy) > 1000){
            gw.removeObject(this);
        }
    }
}