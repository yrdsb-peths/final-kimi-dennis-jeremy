import greenfoot.*;

public class MyWorld extends World
{
    private static final int CENTER_X = 400;
    private static final String BACKGROUND_IMAGE = "images/Background .png";

    private boolean playerChosen = false;
    private final SimpleTimer enemySpawnTimer = new SimpleTimer();

    public LeonClovis leon;
    public KaineVelsarth kaine;

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
        drawWorldBackground();

        GreenfootImage titleImage = new GreenfootImage(
            "Spider Skill Battles",
            42,
            new Color(20, 20, 20),
            new Color(245, 245, 245)
        );

        getBackground().drawImage(
            titleImage,
            (getWidth() - titleImage.getWidth()) / 2,
            120
        );

        showText("Press K for Kaine", CENTER_X, 230);
        showText("Press L for Leon", CENTER_X, 260);
        showText("Click the world, then press a key", CENTER_X, 300);
    }

    public void clearTitleScreen()
    {
        drawWorldBackground();
        showText("", CENTER_X, 230);
        showText("", CENTER_X, 260);
        showText("", CENTER_X, 300);
        showText("", CENTER_X, 520);
        showText("", CENTER_X, 545);
        showText("", CENTER_X, 570);
    }

    private void drawWorldBackground()
    {
        GreenfootImage background;

        try
        {
            background = new GreenfootImage(BACKGROUND_IMAGE);
            background.scale(getWidth(), getHeight());
        }
        catch(IllegalArgumentException exception)
        {
            background = new GreenfootImage(getWidth(), getHeight());
            background.setColor(new Color(245, 245, 245));
            background.fill();
        }

        setBackground(background);
    }

    public void spawnKaine()
    {
        kaine = new KaineVelsarth();
        addObject(kaine, CENTER_X, 300);
        playerChosen = true;
        clearTitleScreen();
        showText(kaine.getStartingLoadoutText(), CENTER_X, 520);
        spawnStartingEnemies();
    }

    public void spawnLeon()
    {
        leon = new LeonClovis();
        addObject(leon, CENTER_X, 300);
        playerChosen = true;
        clearTitleScreen();
        showText("Leon starts with ranged attacks.", CENTER_X, 520);
        showText("", CENTER_X, 545);
        showText("", CENTER_X, 570);
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
        addObject(new Enemy(x, y), x, y);
    }
}
