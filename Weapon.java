import greenfoot.*;

public abstract class Weapon extends Actor
{
    // World position
    public double worldX;
    public double worldY;

    // Damage amount
    public int damage;

    public Weapon()
    {
        this.worldX = 0;
        this.worldY = 0;
        this.damage = 10;
    }

    // Abstract act() implemented by each weapon
    public abstract void act();

    // Enemy hit detection (optional override)
    protected void checkHitEnemy(double checkX, double checkY, double radius)
    {
        if(getWorld() == null) return;
        GameWorld gw = (GameWorld)getWorld();

        for(Enemy e : gw.getObjects(Enemy.class))
        {
            double dx = e.worldX - checkX;
            double dy = e.worldY - checkY;
            double dist = Math.sqrt(dx*dx + dy*dy);

            if(dist < radius)
            {
                onHitEnemy(e, dx, dy, dist);
            }
        }
    }

    // Called when an enemy is hit (implemented per weapon)
    protected abstract void onHitEnemy(Enemy e, double dx, double dy, double dist);
}