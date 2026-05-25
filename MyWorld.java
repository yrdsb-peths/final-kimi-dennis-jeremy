import greenfoot.*;

public class MyWorld extends World
{
    SimpleTimer enemySpawnTimer = new SimpleTimer();

    public MyWorld()
    {    
        super(800, 600, 1);

        prepare();
    }

    public void act()
    {
        spawnEnemies();
    }

    public void prepare()
    {
        LeonClovis leon = new LeonClovis();
        addObject(leon, 400, 300);

        for(int i = 0; i < 3; i++)
        {
            spawnEnemy();
        }
    }

    public void spawnEnemies()
    {
        if(enemySpawnTimer.millisElapsed() > 2000)
        {
            if(getObjects(Enemy.class).size() < 5)
            {
                spawnEnemy();
            }

            enemySpawnTimer.mark();
        }
    }

    public void spawnEnemy()
    {
        int x = Greenfoot.getRandomNumber(getWidth());
        int y = Greenfoot.getRandomNumber(getHeight());

        addObject(new Enemy(), x, y);
    }
}
