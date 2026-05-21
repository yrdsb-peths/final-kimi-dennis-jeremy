import greenfoot.*;

public class GameWorld extends World
{
    public AureaSolvine aureaSolvine;

    int enemySpawnTimer = 0;
    int lightningTimer = 0;

    public GameWorld()
    {
        super(1000, 500, 1);

        aureaSolvine = new AureaSolvine();

        addObject(aureaSolvine, 750, 375);
    }

    public void act()
    {
        spawnEnemy();
        spawnLightning();
    }
    
    public void spawnLightning()
    {
        lightningTimer++;
    
        // 每1.5秒
        if(lightningTimer >= 90)
        {
            lightningTimer = 0;
    
            Enemy closest = getClosestEnemy();
    
            if(closest != null)
            {
                addObject(
                    new Lightning(),
                    closest.getX(),
                    closest.getY()
                );
            }
        }
    }

    public void spawnEnemy()
    {
        enemySpawnTimer++;

        if(enemySpawnTimer >= 60)
        {
            enemySpawnTimer = 0;

            int x;
            int y;

            do
            {
                x = Greenfoot.getRandomNumber(getWidth());
                y = Greenfoot.getRandomNumber(getHeight());

            }
            while(distanceToPlayer(x, y) < 200);

            addObject(new Enemy(), x, y);
        }
    }

    public double distanceToPlayer(int x, int y)
    {
        int dx = x - aureaSolvine.getX();
        int dy = y - aureaSolvine.getY();

        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public Enemy getClosestEnemy()
    {
        java.util.List<Enemy> enemies =
            getObjects(Enemy.class);
    
        if(enemies.isEmpty())
        {
            return null;
        }
    
        Enemy closest = enemies.get(0);
    
        double closestDistance =
            distanceBetween(
                aureaSolvine.getX(),
                aureaSolvine.getY(),
                closest.getX(),
                closest.getY()
            );
    
        for(Enemy enemy : enemies)
        {
            double distance =
                distanceBetween(
                    aureaSolvine.getX(),
                    aureaSolvine.getY(),
                    enemy.getX(),
                    enemy.getY()
                );
    
            if(distance < closestDistance)
            {
                closestDistance = distance;
                closest = enemy;
            }
        }
    
        return closest;
    }
    
    public double distanceBetween(int x1, int y1, int x2, int y2 )
    {
        int dx = x1 - x2;
        int dy = y1 - y2;
    
        return Math.sqrt(dx * dx + dy * dy);
    }
}