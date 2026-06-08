import greenfoot.*;

public class Lightning extends Weapon
{
    static GreenfootImage[] frames = new GreenfootImage[4];

    static
    {
        for(int i = 0; i < 4; i++)
        {
            frames[i] = new GreenfootImage("lightning/tile00" + i + ".png");
            if(frames[i].getWidth() <= 0)
            {
                frames[i] = new GreenfootImage(24, 48);
                frames[i].setColor(new Color(180, 220, 255));
                frames[i].fillRect(8, 0, 8, 48);
            }
            else
            {
                frames[i].scale(
                    Math.max(1, frames[i].getWidth() / 3),
                    Math.max(1, frames[i].getHeight() / 3)
                );
            }
        }
    }

    int frame = 0;
    int timer = 0;
    boolean hasHit = false;

    public Lightning(double worldX, double worldY, int damage)
    {
        super();
        this.worldX = worldX;
        this.worldY = worldY;
        this.damage = damage;

        setImage(frames[0]);
    }

    @Override
    public void act()
    {
        if(getWorld() == null) return;

        animate();

        if(getWorld() == null) return;
        if(!hasHit) checkHitEnemy(worldX, worldY, 30);
    }

    private void animate()
    {
        timer++;

        if(timer % 3 == 0)
        {
            frame++;

            if(frame >= frames.length)
            {
                if(getWorld() != null)
                {
                    getWorld().removeObject(this);
                }

                return;
            }

            setImage(frames[frame]);
        }
    }

    @Override
    protected void onHitEnemy(Enemy e, double dx, double dy, double dist)
    {
        if(!(getWorld() instanceof GameWorld)) return;

        GameWorld gw = (GameWorld) getWorld();
        boolean died = e.takeDamage(damage);
        if(died)
        {
            gw.handleEnemyDefeat(e);
        }
        hasHit = true;
    }
}