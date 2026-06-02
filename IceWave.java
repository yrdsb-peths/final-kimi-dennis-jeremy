import greenfoot.*;

public class IceWave extends Weapon
{
    static GreenfootImage[] frames = new GreenfootImage[28];

    static
    {
        for(int i = 0; i < 28; i++)
        {
            frames[i] = new GreenfootImage("IceWave/IceWave" + i + ".png");
            int w = frames[i].getWidth();
            int h = frames[i].getHeight();
            if(w > 0 && h > 0)
                frames[i].scale(w * 3, h * 3);
        }
    }

    int frame = 0;
    int animationTimer = 0;

    static final int RADIUS   = 160;
    static final int KNOCKBACK = 60;

    boolean damageDealtThisCycle = false;

    public IceWave()
    {
        super();
        setImage(frames[0]);
    }

    @Override
    public void act()
    {
        if(getWorld() == null) return;

        GameWorld gw = (GameWorld)getWorld();
        worldX = gw.player.worldX;
        worldY = gw.player.worldY;

        animate();
        if(frame == 10) checkHitEnemy(worldX, worldY, RADIUS);
    }

    private void animate()
    {
        animationTimer++;
        if(animationTimer % 3 == 0)
        {
            frame++;
            if(frame >= frames.length)
            {
                frame = 0;
                damageDealtThisCycle = false;
            }
            setImage(frames[frame]);
        }
    }

    @Override
    protected void onHitEnemy(Enemy e, double dx, double dy, double dist)
    {
        if(damageDealtThisCycle) return;
        damageDealtThisCycle = true;

        if(dist == 0) return;

        GameWorld gw = (GameWorld)getWorld();
        boolean died = e.takeDamage(damage);
        if(died)
        {
            gw.player.gainXP(e.xpDrop);
            gw.player.gainCoin(e.coinDrop);
            if(e.getWorld() != null) gw.removeObject(e);
        }
        else
        {
            // Knockback
            e.worldX += dx / dist * KNOCKBACK;
            e.worldY += dy / dist * KNOCKBACK;
        }
    }
}