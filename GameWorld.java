import greenfoot.*;

public class GameWorld extends World
{
    public AureaSolvine aureaSolvine;
    public LeonClovis leonClovis;

    public Actor player;

    int enemySpawnTimer = 0;

    static final int ENEMY_SPAWN_INTERVAL = 60;
    static final int MIN_SPAWN_DISTANCE = 300;
    static final int MAX_ENEMIES = 30;

    GreenfootImage bgTile;

    int bgOffX = 0;
    int bgOffY = 0;

    public int screenCX;
    public int screenCY;

    public GameWorld(String character)
    {
        super(1500, 750, 1);

        screenCX = getWidth() / 2;
        screenCY = getHeight() / 2;

        bgTile = new GreenfootImage("background.png");

        drawBackground(0, 0);

        if(character.equals("aurea"))
        {
            aureaSolvine = new AureaSolvine();

            player = aureaSolvine;

            addObject(
                aureaSolvine,
                screenCX,
                screenCY
            );
        }
        else if(character.equals("leon"))
        {
            leonClovis = new LeonClovis();

            player = leonClovis;

            addObject(
                leonClovis,
                screenCX,
                screenCY
            );
        }
    }

    public GameWorld()
    {
        this("aurea");
    }

    public void act()
    {
        updateCamera();

        drawBackground(bgOffX, bgOffY);

        updateScreenPositions();

        spawnEnemy();

        drawHUD();
    }

    public void updateCamera()
    {
        double px = 0;
        double py = 0;

        if(aureaSolvine != null)
        {
            px = aureaSolvine.worldX;
            py = aureaSolvine.worldY;
        }

        if(leonClovis != null)
        {
            px = leonClovis.worldX;
            py = leonClovis.worldY;
        }

        bgOffX = (int)((-px % bgTile.getWidth()
                    + bgTile.getWidth())
                    % bgTile.getWidth());

        bgOffY = (int)((-py % bgTile.getHeight()
                    + bgTile.getHeight())
                    % bgTile.getHeight());
    }

    private void updateScreenPositions()
    {
        double camX = 0;
        double camY = 0;

        if(aureaSolvine != null)
        {
            camX = aureaSolvine.worldX;
            camY = aureaSolvine.worldY;
        }

        if(leonClovis != null)
        {
            camX = leonClovis.worldX;
            camY = leonClovis.worldY;
        }

        for(Enemy e : getObjects(Enemy.class))
        {
            int sx =
                (int)(screenCX + (e.worldX - camX));

            int sy =
                (int)(screenCY + (e.worldY - camY));

            e.setLocation(sx, sy);
        }
    }

    private void drawBackground(int offX, int offY)
    {
        GreenfootImage bg = getBackground();

        bg.clear();

        int tw = bgTile.getWidth();
        int th = bgTile.getHeight();

        for(int x = offX - tw;
            x < getWidth() + tw;
            x += tw)
        {
            for(int y = offY - th;
                y < getHeight() + th;
                y += th)
            {
                bg.drawImage(bgTile, x, y);
            }
        }
    }

    public void drawHUD()
    {
        if(aureaSolvine != null)
        {
            aureaSolvine.displayStats();
        }
        if(leonClovis != null)
        {
            leonClovis.displayStats();
        }
    }

    public void spawnEnemy()
    {
        enemySpawnTimer++;

        if(enemySpawnTimer >= ENEMY_SPAWN_INTERVAL)
        {
            enemySpawnTimer = 0;

            if(getObjects(Enemy.class).size()
                >= MAX_ENEMIES)
            {
                return;
            }

            double px = 0;
            double py = 0;

            if(aureaSolvine != null)
            {
                px = aureaSolvine.worldX;
                py = aureaSolvine.worldY;
            }

            if(leonClovis != null)
            {
                px = leonClovis.worldX;
                py = leonClovis.worldY;
            }

            double x;
            double y;

            do
            {
                x = px +
                    Greenfoot.getRandomNumber(1600)
                    - 800;

                y = py +
                    Greenfoot.getRandomNumber(900)
                    - 450;
            }
            while(distanceBetween(x, y, px, py)
                    < MIN_SPAWN_DISTANCE);

            Enemy e = new Enemy(x, y);

            int sx =
                (int)(screenCX + (x - px));

            int sy =
                (int)(screenCY + (y - py));

            addObject(e, sx, sy);
        }
    }

    public Enemy getClosestEnemy()
    {
        java.util.List<Enemy> enemies =
            getObjects(Enemy.class);

        if(enemies.isEmpty())
        {
            return null;
        }

        Enemy closest = null;

        double minD = Double.MAX_VALUE;

        double px = 0;
        double py = 0;

        if(aureaSolvine != null)
        {
            px = aureaSolvine.worldX;
            py = aureaSolvine.worldY;
        }

        if(leonClovis != null)
        {
            px = leonClovis.worldX;
            py = leonClovis.worldY;
        }

        for(Enemy e : enemies)
        {
            double d =
                distanceBetween(
                    e.worldX,
                    e.worldY,
                    px,
                    py
                );

            if(d < minD)
            {
                minD = d;
                closest = e;
            }
        }

        return closest;
    }

    public double distanceBetween(
        double x1,
        double y1,
        double x2,
        double y2)
    {
        double dx = x1 - x2;
        double dy = y1 - y2;

        return Math.sqrt(dx * dx + dy * dy);
    }
}