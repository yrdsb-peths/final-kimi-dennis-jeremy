import greenfoot.*;

public class MyWorld extends World
{
    private boolean playerChosen = false;
    SimpleTimer enemySpawnTimer = new SimpleTimer();

    public MyWorld()
    {    
        super(800, 600, 1);
        showTitleScreen();
    }

    public void act()
    {
        if(!playerChosen)
        {
            checkPlayerChoice();
        }
        else
        {
            spawnEnemies();
        }
    }

    public void checkPlayerChoice()
    {
        String key = Greenfoot.getKey();

        if(key == null)
        {
            return;
        }

        if(key.equalsIgnoreCase("k"))
        {
            spawnKaine();
        }
        else if(key.equalsIgnoreCase("l"))
        {
            spawnLeon();
        }
    }

    public void showTitleScreen()
    {
        showText("Title Screen", 400, 180);
        showText("Press K for Kaine", 400, 230);
        showText("Press L for Leon", 400, 260);
        showText("Click the world, then press a key", 400, 300);
    }

    public void clearTitleScreen()
    {
        showText("", 400, 180);
        showText("", 400, 230);
        showText("", 400, 260);
        showText("", 400, 300);
        showText("", 400, 520);
        showText("", 400, 545);
        showText("", 400, 570);
    }

    public void spawnKaine()
    {
        KaineVelsarth kaine = new KaineVelsarth();
        addObject(kaine, 400, 300);

        playerChosen = true;
        clearTitleScreen();

        showText(kaine.getStartingLoadoutText(), 400, 520);
        showText(kaine.getInventoryText(), 400, 545);
        showText(kaine.getEquippedText(), 400, 570);

        spawnStartingEnemies();
    }

    public void spawnLeon()
    {
        LeonClovis leon = new LeonClovis();
        addObject(leon, 400, 300);

        playerChosen = true;
        clearTitleScreen();

        spawnStartingEnemies();
    }

    public void spawnStartingEnemies()
    {
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