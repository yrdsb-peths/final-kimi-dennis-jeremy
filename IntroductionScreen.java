import greenfoot.*;

public class IntroductionScreen extends World
{
  public IntroductionScreen()
  {
    super(1500, 750, 1);
    drawContent();
  }

  public void act()
  {
    String key = Greenfoot.getKey();
    if(key == null)
    {
      return;
    }

    if(key.equalsIgnoreCase("escape")
        || key.equalsIgnoreCase("space")
        || key.equals(" "))
    {
      Greenfoot.setWorld(new TitleScreen());
    }
  }

  private void drawContent()
  {
    GreenfootImage bg = getBackground();
    bg.clear();
    bg.setColor(new Color(20, 20, 40));
    bg.fillRect(0, 0, getWidth(), getHeight());

    bg.setColor(Color.WHITE);
    bg.setFont(new Font("Arial", true, false, 40));
    drawCenteredText(bg, "HOW TO PLAY", getWidth() / 2, 70);

    int x = 100;
    int y = 130;
    int line = 28;

    drawSection(bg, x, y, "Goal");
    y += line;
    drawBody(bg, x, y, "Survive 30 rounds. Defeat enemies to earn XP and coins.");
    y += line * 2;

    drawSection(bg, x, y, "Choose Your Hero (Title Screen)");
    y += line;
    drawBody(bg, x, y, "[A] Aurea Solvine — starts with Lightning");
    y += line;
    drawBody(bg, x, y, "[L] Leon Clovis — starts with Gun");
    y += line;
    drawBody(bg, x, y, "[K] Kaine Velsarth — starts with Sword companion");
    y += line;
    drawBody(bg, x, y, "[I] Open this guide");
    y += line * 2;

    drawSection(bg, x, y, "Controls");
    y += line;
    drawBody(bg, x, y, "WASD — move your hero");
    y += line;
    drawBody(bg, x, y, "Kaine only: SPACE — switch sword type   TAB — flip sword direction");
    y += line * 2;

    drawSection(bg, x, y, "How Rounds Work");
    y += line;
    drawBody(bg, x, y, "Enemies spawn continuously. Near the end of each round, an Elite enemy appears.");
    y += line;
    drawBody(bg, x, y, "Once the Elite spawns, no new normal enemies appear.");
    y += line;
    drawBody(bg, x, y, "Defeat the Elite and clear all remaining enemies to end the round early");
    y += line;
    drawBody(bg, x, y, "and head straight to the shop!");
    y += line * 2;

    drawSection(bg, x, y, "Progression");
    y += line;
    drawBody(bg, x, y, "Level up from XP to gain attribute points — spend them in the shop.");
    y += line * 2;

    drawSection(bg, x, y, "Shop (after each round)");
    y += line;
    drawBody(bg, x, y, "[1] Power +1   [2] Speed +1   [3] Stamina +1 (also +10 Max HP)");
    y += line;
    drawBody(bg, x, y, "[Q] Fireball   [W] Lightning   [E] IceWave   [R] Sword upgrade (Kaine)");
    y += line;
    drawBody(bg, x, y, "Buy weapon: 10 coins   Upgrade weapon: 15 coins");
    y += line;
    drawBody(bg, x, y, "SPACE — start the next round");

    bg.setFont(new Font("Arial", true, false, 24));
    bg.setColor(new Color(255, 220, 80));
    drawCenteredText(bg, "Press SPACE or ESC to return", getWidth() / 2, 710);
  }

  private void drawSection(GreenfootImage bg, int x, int y, String text)
  {
    bg.setFont(new Font("Arial", true, false, 24));
    bg.setColor(new Color(200, 200, 255));
    bg.drawString(text, x, y);
  }

  private void drawBody(GreenfootImage bg, int x, int y, String text)
  {
    bg.setFont(new Font("Arial", false, false, 20));
    bg.setColor(Color.WHITE);
    bg.drawString(text, x, y);
  }

  private void drawCenteredText(GreenfootImage bg, String text, int cx, int y)
  {
    int approxW = text.length() * 16;
    bg.drawString(text, cx - approxW / 2, y);
  }
}
