import greenfoot.*;

public class Bullet extends Actor
{
    int damage;
    int speed = 10;

    public double worldX;
    public double worldY;

    double dx;
    double dy;
    double dist;

    public Bullet(double startX, double startY,
                  double targetX, double targetY,
                  int damage)
    {
        this.worldX = startX;
        this.worldY = startY;

        this.damage = damage;

        GreenfootImage img = new GreenfootImage(12, 12);
        img.setColor(Color.YELLOW);
        img.fillOval(0, 0, 12, 12);
        setImage(img);

        dx = targetX - startX;
        dy = targetY - startY;

        dist = Math.sqrt(dx * dx + dy * dy);

        if(dist != 0)
        {
            dx /= dist;
            dy /= dist;
        }
    }

    public void act()
    {
        moveBullet();
        hitEnemy();
    }

    public void moveBullet()
    {
        worldX += dx * speed;
        worldY += dy * speed;

        GameWorld gw = (GameWorld)getWorld();

        int sx = (int)(gw.screenCX + (worldX - gw.aureaSolvine.worldX));
        int sy = (int)(gw.screenCY + (worldY - gw.aureaSolvine.worldY));

        setLocation(sx, sy);
    }

    public void hitEnemy()
    {
        java.util.List<Enemy> enemies =
            getWorld().getObjects(Enemy.class);

        for(Enemy enemy : enemies)
        {
            double ex = enemy.worldX - worldX;
            double ey = enemy.worldY - worldY;

            double distance =
                Math.sqrt(ex * ex + ey * ey);

            if(distance < 25)
            {
                enemy.takeDamage(damage);

                getWorld().removeObject(this);
                return;
            }
        }
    }
}