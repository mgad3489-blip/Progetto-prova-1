package it.unibo.towerdefense.view;

import it.unibo.towerdefense.model.GameModel;
import it.unibo.towerdefense.model.GameState;
import it.unibo.towerdefense.model.Enemy;
import it.unibo.towerdefense.model.Tower;
import it.unibo.towerdefense.model.Projectile;
import it.unibo.towerdefense.model.BuildingSpot;
import it.unibo.towerdefense.model.TowerType;
import it.unibo.towerdefense.model.EnemyType;
import it.unibo.towerdefense.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GameView {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final Color C_GOLD = new Color(255, 215, 0);
    private static final Color C_UI = new Color(20, 15, 10, 210);

    private static Image spTowerBasic, spTowerSniper, spTowerRapid, spTowerIce;
    private static Image spEnemyBasic, spEnemyFast, spEnemyTank;
    private static Image imgTree, imgBush, imgRock, imgRockBush;

    static {
        try {
            ClassLoader cl = GameView.class.getClassLoader();
            String b = "images/Images pack/Assets/";
            spTowerBasic = loadImg(cl, b + "Structures/Towers/magic_crystal_tower.png");
            spTowerSniper = loadImg(cl, b + "Characters/Heroes/knight_hero.png");
            spTowerRapid = loadImg(cl, b + "Characters/Heroes/mage_hero.png");
            spTowerIce = tintImg(spTowerBasic, new Color(100, 180, 255, 120));
            spEnemyBasic = loadImg(cl, b + "Enemies/Orcs/orc_raider.png");
            spEnemyTank = loadImg(cl, b + "Enemies/Orcs/orc_brute.png");
            spEnemyFast = tintImg(spEnemyBasic, new Color(50, 255, 50, 130));
            imgTree = loadImg(cl, b + "Props/Nature/pine_tree.png");
            imgBush = loadImg(cl, b + "Props/Nature/bush_round.png");
            imgRock = loadImg(cl, b + "Props/Nature/rock_cluster.png");
            imgRockBush = loadImg(cl, b + "Props/Nature/rock_bush_cluster.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Image loadImg(ClassLoader cl, String path) {
        try { java.io.InputStream is = cl.getResourceAsStream(path); if (is != null) return javax.imageio.ImageIO.read(is); } catch (Exception ignored) {}
        return null;
    }

    private static Image tintImg(Image src, Color tint) {
        if (src == null)
            return null;
        int w = src.getWidth(null), h = src.getHeight(null);
        if (w <= 0 || h <= 0) {
            w = 128;
            h = 128;
        }
        BufferedImage t = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = t.createGraphics();
        g.drawImage(src, 0, 0, w, h, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
        g.setColor(tint);
        g.fillRect(0, 0, w, h);
        g.dispose();
        return t;
    }

    private JFrame frame, menuFrame, levelFrame;
    private MapPanel mapPanel;
    private JDialog pauseDialog;

    public void displayWelcome() {
    }

    // =================== MENU ===================
    public void showStartMenu(GameController c) {
        if (menuFrame != null)
            menuFrame.dispose();
        menuFrame = new JFrame("TowerSiege");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setResizable(false);
        JPanel p = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(25, 18, 12), 0, getHeight(), new Color(50, 35, 20)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        JLabel title = mkLabel("TOWERSIEGE", new Font("Serif", Font.BOLD, 52), C_GOLD);
        JLabel sub = mkLabel("Difendi la base dalle ondate nemiche!", new Font("Serif", Font.ITALIC, 18),
                new Color(220, 200, 160));
        JLabel i1 = mkLabel("Click sx = piazza/potenzia | Click dx = vendi torre", new Font("Serif", Font.PLAIN, 13),
                new Color(180, 180, 180));
        JLabel i2 = mkLabel("W = Ondata | F = Fuoco | G = Gelo | ESC = Pausa", new Font("Serif", Font.PLAIN, 13),
                new Color(180, 180, 180));
        JButton start = btn("INIZIA PARTITA", new Color(30, 150, 80));
        start.addActionListener(e -> {
            menuFrame.dispose();
            c.beginGame();
        });
        JButton rules = btn("REGOLAMENTO", new Color(50, 90, 170));
        rules.addActionListener(e -> showRules());
        JButton exit = btn("ESCI", new Color(170, 45, 45));
        exit.addActionListener(e -> System.exit(0));
        p.add(title);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(sub);
        p.add(Box.createRigidArea(new Dimension(0, 12)));
        p.add(i1);
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(i2);
        p.add(Box.createRigidArea(new Dimension(0, 22)));
        p.add(start);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(rules);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(exit);
        menuFrame.add(p);
        menuFrame.setSize(800, 600);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }

    private void showRules() {
        JDialog d = new JDialog(menuFrame, "Regolamento", true);
        d.setSize(520, 420);
        d.setLocationRelativeTo(menuFrame);
        JTextArea t = new JTextArea();
        t.setEditable(false);
        t.setFont(new Font("Serif", Font.PLAIN, 14));
        t.setLineWrap(true);
        t.setWrapStyleWord(true);
        t.setBackground(new Color(40, 35, 30));
        t.setForeground(new Color(230, 220, 200));
        t.setMargin(new Insets(15, 15, 15, 15));
        t.setText("=== REGOLE ===\n\nOBIETTIVO:\nPiazza torri sui punti di costruzione per fermare i nemici!\n\n" +
                "TORRI:\n  Cristallo (50g) - Danno medio\n  Cavaliere (120g) - Alto danno, lento\n  Mago (80g) - Rapido fuoco\n  Ghiaccio (90g) - Rallenta\n\n"
                +
                "NEMICI:\n  Orco (standard) | Goblin (veloce, verde) | Bruto (lento, grosso)\n\n" +
                "CONTROLLI:\n  Click sinistro = piazza/potenzia torre\n  Click destro = vendi torre (rimborso 50%)\n  W = ondata | F = fuoco (15s CD) | G = gelo (8s CD) | ESC = pausa");
        JButton cb = new JButton("Chiudi");
        cb.addActionListener(e -> d.dispose());
        d.add(new JScrollPane(t), BorderLayout.CENTER);
        d.add(cb, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    // =================== LEVEL SELECT ===================
    public void showLevelSelect(GameController c, GameModel model) {
        if (levelFrame != null)
            levelFrame.dispose();
        levelFrame = new JFrame("Mappa dei Livelli");
        levelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        levelFrame.setResizable(false);
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // Beautiful world map background
                g2.setPaint(new GradientPaint(0, 0, new Color(65, 145, 55), 0, h, new Color(40, 110, 35)));
                g2.fillRect(0, 0, w, h);
                // Texture patches
                java.util.Random r = new java.util.Random(99);
                for (int i = 0; i < 40; i++) {
                    g2.setColor(new Color(50 + r.nextInt(30), 130 + r.nextInt(40), 40 + r.nextInt(20), 50));
                    g2.fillOval(r.nextInt(w), r.nextInt(h), 30 + r.nextInt(50), 30 + r.nextInt(50));
                }
                // Scatter decorations
                for (int i = 0; i < 25; i++) {
                    int dx = r.nextInt(w - 40) + 20, dy = r.nextInt(h - 60) + 20;
                    Image[] imgs = { imgTree, imgBush, imgRock, imgRockBush };
                    Image di = imgs[r.nextInt(4)];
                    int ds = 25 + r.nextInt(20);
                    if (di != null)
                        g2.drawImage(di, dx, dy, ds, ds, null);
                }

                // Level positions on a winding path
                int[][] lvlPos = { { 140, 440 }, { 400, 250 }, { 660, 400 } };

                // Draw winding path between levels
                g2.setColor(new Color(180, 150, 100));
                g2.setStroke(new BasicStroke(18, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                // Path: Level1 → curve → Level2 → curve → Level3
                g2.drawLine(lvlPos[0][0], lvlPos[0][1], 220, 380);
                g2.drawLine(220, 380, 280, 300);
                g2.drawLine(280, 300, 340, 270);
                g2.drawLine(340, 270, lvlPos[1][0], lvlPos[1][1]);
                g2.drawLine(lvlPos[1][0], lvlPos[1][1], 480, 270);
                g2.drawLine(480, 270, 540, 320);
                g2.drawLine(540, 320, 600, 370);
                g2.drawLine(600, 370, lvlPos[2][0], lvlPos[2][1]);
                // Inner path highlight
                g2.setColor(new Color(210, 195, 155));
                g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(lvlPos[0][0], lvlPos[0][1], 220, 380);
                g2.drawLine(220, 380, 280, 300);
                g2.drawLine(280, 300, 340, 270);
                g2.drawLine(340, 270, lvlPos[1][0], lvlPos[1][1]);
                g2.drawLine(lvlPos[1][0], lvlPos[1][1], 480, 270);
                g2.drawLine(480, 270, 540, 320);
                g2.drawLine(540, 320, 600, 370);
                g2.drawLine(600, 370, lvlPos[2][0], lvlPos[2][1]);
                g2.setStroke(new BasicStroke(1));

                // Title
                g2.setFont(new Font("Serif", Font.BOLD, 34));
                g2.setColor(new Color(0, 0, 0, 140));
                g2.drawString("MAPPA DEI LIVELLI", 222, 52);
                g2.setColor(C_GOLD);
                g2.drawString("MAPPA DEI LIVELLI", 220, 50);

                // Draw level nodes
                String[] names = { "Foresta", "Pianura", "Montagna" };
                String[] diff = { "Facile", "Medio", "Difficile" };
                Color[] cols = { new Color(40, 180, 80), new Color(200, 160, 40), new Color(200, 60, 60) };
                int maxU = model.getMaxUnlockedLevel();

                for (int i = 0; i < 3; i++) {
                    int cx = lvlPos[i][0], cy = lvlPos[i][1];
                    boolean unlocked = (i + 1) <= maxU;

                    // Glow around unlocked
                    if (unlocked) {
                        g2.setColor(new Color(cols[i].getRed(), cols[i].getGreen(), cols[i].getBlue(), 40));
                        g2.fillOval(cx - 50, cy - 50, 100, 100);
                    }

                    // Stone circle base
                    g2.setColor(new Color(0, 0, 0, 60));
                    g2.fillOval(cx - 37, cy - 33, 74, 70);
                    g2.setColor(unlocked ? new Color(60, 50, 35) : new Color(50, 50, 50));
                    g2.fillOval(cx - 35, cy - 35, 70, 70);
                    g2.setColor(unlocked ? cols[i] : new Color(80, 80, 80));
                    g2.setStroke(new BasicStroke(3));
                    g2.drawOval(cx - 35, cy - 35, 70, 70);
                    g2.setStroke(new BasicStroke(1));

                    // Level number
                    g2.setFont(new Font("Serif", Font.BOLD, 36));
                    g2.setColor(unlocked ? Color.WHITE : new Color(100, 100, 100));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString("" + (i + 1), cx - fm.stringWidth("" + (i + 1)) / 2, cy + 12);

                    // Name below
                    g2.setFont(new Font("Serif", Font.BOLD, 14));
                    g2.setColor(unlocked ? C_GOLD : new Color(100, 100, 100));
                    fm = g2.getFontMetrics();
                    g2.drawString(names[i], cx - fm.stringWidth(names[i]) / 2, cy + 52);

                    // Difficulty
                    g2.setFont(new Font("Serif", Font.ITALIC, 11));
                    g2.setColor(unlocked ? cols[i] : new Color(80, 80, 80));
                    fm = g2.getFontMetrics();
                    g2.drawString(diff[i], cx - fm.stringWidth(diff[i]) / 2, cy + 66);

                    // Lock
                    if (!unlocked) {
                        g2.setColor(new Color(160, 160, 160, 200));
                        g2.setFont(new Font("Serif", Font.BOLD, 28));
                        fm = g2.getFontMetrics();
                        g2.drawString("X", cx - fm.stringWidth("X") / 2, cy + 8);
                    }
                }

                // Back button hint
                g2.setFont(new Font("Serif", Font.PLAIN, 12));
                g2.setColor(new Color(200, 200, 200, 150));
                g2.drawString("Clicca su un livello per giocare", w / 2 - 100, h - 20);
            }
        };
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int[][] lvlPos = { { 140, 440 }, { 400, 250 }, { 660, 400 } };
                for (int i = 0; i < 3; i++) {
                    int dx = e.getX() - lvlPos[i][0], dy = e.getY() - lvlPos[i][1];
                    if (dx * dx + dy * dy < 40 * 40 && (i + 1) <= model.getMaxUnlockedLevel()) {
                        levelFrame.dispose();
                        c.startLevel(i + 1);
                        break;
                    }
                }
            }
        });
        panel.setPreferredSize(new Dimension(800, 600));
        levelFrame.add(panel);
        levelFrame.pack();
        levelFrame.setLocationRelativeTo(null);
        levelFrame.setVisible(true);
    }

    // =================== GAME ===================
    public void displayGameState(GameModel model, GameController c) {
        if (frame == null)
            initGui(model, c);
        else {
            mapPanel.setModel(model);
            mapPanel.setCtrl(c);
            mapPanel.repaint();
        }
    }

    private void initGui(GameModel model, GameController c) {
        frame = new JFrame("TowerSiege - Livello " + model.getCurrentLevel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        mapPanel = new MapPanel(model, c);
        mapPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.add(mapPanel);
        InputMap im = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = frame.getRootPane().getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "p");
        am.put("p", act(() -> c.togglePause()));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "w");
        am.put("w", act(() -> c.forceNextWave()));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "f");
        am.put("f", act(() -> c.triggerRainOfFire()));
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0), "g");
        am.put("g", act(() -> c.triggerGlobalFreeze()));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.requestFocusInWindow();
    }

    private AbstractAction act(Runnable r) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                r.run();
            }
        };
    }

    // =================== MAP PANEL ===================
    private static class MapPanel extends JPanel {
        private GameModel model;
        private GameController ctrl;
        private BuildingSpot hoverSpot;

        MapPanel(GameModel m, GameController c) {
            model = m;
            ctrl = c;
            setupMouse();
        }

        void setModel(GameModel m) {
            model = m;
        }

        void setCtrl(GameController c) {
            ctrl = c;
        }

        void setupMouse() {
            addMouseMotionListener(new MouseAdapter() {
                public void mouseMoved(MouseEvent e) {
                    int col = e.getX() / 50;
                    int row = e.getY() / 50;
                    hoverSpot = model.getMap().getSpotAt(col, row);
                    repaint();
                }
            });
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int col = e.getX() / 50;
                    int row = e.getY() / 50;
                    // Right-click = sell tower
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        BuildingSpot spot = model.getMap().getSpotAt(col, row);
                        if (spot != null)
                            ctrl.sellTowerAtSpot(spot);
                        return;
                    }
                    // Left-click: shop cards first
                    TowerType[] types = TowerType.values();
                    int cw = 100, gap = 8, total = types.length * (cw + gap) - gap;
                    int sx = (getWidth() - total) / 2, sy = getHeight() - 70 - 12;
                    for (int i = 0; i < types.length; i++) {
                        int cx = sx + i * (cw + gap);
                        if (e.getX() >= cx && e.getX() <= cx + cw && e.getY() >= sy && e.getY() <= sy + 70) {
                            ctrl.setSelectedTowerType(types[i]);
                            repaint();
                            return;
                        }
                    }
                    // Then building spots
                    BuildingSpot spot = model.getMap().getSpotAt(col, row);
                    if (spot != null)
                        ctrl.interactWithSpot(spot);
                }
            });
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (model == null) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int pw = getWidth(), ph = getHeight();
            int lvl = model.getCurrentLevel();
            java.util.Random rng = new java.util.Random(42 + lvl * 137);
            java.util.List<double[]> wp = model.getMap().getWaypoints();

            int[][] grid = model.getMap().getGrid();
            for (int r = 0; r < 12; r++) {
                for (int c = 0; c < 16; c++) {
                    int tx = c * 50;
                    int ty = r * 50;
                    int type = grid[r][c];

                    // Base grass for everything
                    g2.setColor(new Color(60 + (r+c)%3 * 5, 140 + (r+c)%2 * 8, 45, 255));
                    g2.fillRect(tx, ty, 50, 50);
                    g2.setColor(new Color(0,0,0,15)); g2.drawRect(tx, ty, 50, 50);

                    // Grid details
                    if (type == 1) {
                        // Path Tile
                        g2.setColor(new Color(190, 165, 120));
                        g2.fillRect(tx+2, ty+2, 46, 46);
                        
                        // Dirt texture
                        g2.setColor(new Color(170, 145, 100));
                        int n1 = rng.nextInt(3), n2 = rng.nextInt(3);
                        g2.fillRect(tx + 10 + n1, ty + 10 + n2, 8, 8);
                        g2.fillRect(tx + 30 + n2, ty + 20 + n1, 12, 10);
                        
                    } else if (type == 2) {
                        // Buildable Spot Base (Stone foundation)
                        g2.setColor(new Color(120, 120, 120));
                        g2.fillRect(tx+4, ty+4, 42, 42);
                        g2.setColor(new Color(100, 100, 100));
                        g2.fillRect(tx+8, ty+8, 34, 34);
                        g2.setColor(new Color(140, 140, 140));
                        g2.drawRect(tx+4, ty+4, 42, 42);
                    } else {
                        // Grass Decoration
                        int rndDeco = rng.nextInt(15);
                        if (rndDeco == 0 && imgTree != null) {
                            // Tree shadow
                            g2.setColor(new Color(0, 0, 0, 30));
                            g2.fillOval(tx - 5, ty + 25, 60, 20);
                            g2.drawImage(imgTree, tx - 10, ty - 30, 70, 80, null);
                        } else if (rndDeco == 1 && imgRockBush != null) {
                            g2.setColor(new Color(0, 0, 0, 30));
                            g2.fillOval(tx + 5, ty + 30, 40, 15);
                            g2.drawImage(imgRockBush, tx, ty - 5, 50, 50, null);
                        } else if (rndDeco == 2 && imgRock != null) {
                            g2.setColor(new Color(0, 0, 0, 30));
                            g2.fillOval(tx + 10, ty + 35, 30, 10);
                            g2.drawImage(imgRock, tx + 5, ty + 10, 40, 40, null);
                        } else if (rndDeco == 3 && imgBush != null) {
                            g2.setColor(new Color(0, 0, 0, 30));
                            g2.fillOval(tx + 5, ty + 30, 40, 15);
                            g2.drawImage(imgBush, tx, ty, 50, 50, null);
                        } else if (rng.nextInt(3) == 0) {
                            // Small grass tufts for empty tiles
                            g2.setColor(new Color(45, 110, 30));
                            g2.drawLine(tx + 15, ty + 25, tx + 18, ty + 15);
                            g2.drawLine(tx + 18, ty + 25, tx + 20, ty + 14);
                            g2.drawLine(tx + 21, ty + 25, tx + 25, ty + 18);
                            
                            // Add an occasional tiny flower
                            if (rng.nextInt(4) == 0) {
                                Color[] fColors = {new Color(255, 200, 80), new Color(255, 150, 150), new Color(200, 150, 255)};
                                g2.setColor(fColors[rng.nextInt(fColors.length)]);
                                g2.fillOval(tx + 28, ty + 22, 4, 4);
                            }
                        }
                    }
                }
            }

            // Spawn and End markers on Path
            if (wp.size() > 1) {
                double[] sp = wp.get(0); int spx = (int)sp[0], spy = (int)sp[1];
                g2.setColor(new Color(50, 220, 50, 60)); g2.fillRect(spx-25, spy-25, 50, 50);
                
                double[] ep = wp.get(wp.size()-1); int epx = (int)ep[0], epy = (int)ep[1];
                g2.setColor(new Color(220, 50, 50, 60)); g2.fillRect(epx-25, epy-25, 50, 50);
            }

            // ===== BUILDING SPOTS (TOWERS & HOVER) =====
            for (BuildingSpot spot : model.getMap().getBuildingSpots()) {
                int sx = (int)spot.getPixelCenterX(), sy = (int)spot.getPixelCenterY();
                
                if (!spot.isOccupied()) {
                    if (spot == hoverSpot) {
                        TowerType sel = ctrl.getSelectedTowerType();
                        int rPx = sel.getRange()*40;
                        g2.setColor(new Color(80,220,80,25)); g2.fillOval(sx-rPx,sy-rPx,rPx*2,rPx*2);
                        g2.setColor(new Color(150,255,150,50)); g2.drawOval(sx-rPx,sy-rPx,rPx*2,rPx*2);
                        
                        g2.setColor(new Color(255, 255, 255, 100)); g2.fillRect(sx-25, sy-25, 50, 50);
                        
                        Image pv = towerSprite(sel);
                        if (pv != null) { g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.45f));
                            g2.drawImage(pv,sx-20,sy-36,40,54,null); g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f)); }
                        g2.setColor(C_GOLD); g2.setFont(new Font("Serif",Font.BOLD,14)); g2.drawString(sel.getCost()+"g",sx-14,sy+20);
                    }
                } else {
                    Tower t = spot.getTower();
                    Image img = towerSprite(t.getType());
                    if (img != null) g2.drawImage(img,sx-22,sy-38,44,58,null);
                    if (t.getLevel() > 1) { g2.setColor(C_GOLD); g2.setFont(new Font("Serif",Font.BOLD,14));
                        g2.drawString(t.getLevel()==2?"*":"**",sx-6,sy-42); }
                    if (spot == hoverSpot) {
                        int rPx = t.getRange()*40;
                        g2.setColor(new Color(70,70,200,25)); g2.fillOval(sx-rPx,sy-rPx,rPx*2,rPx*2);
                        g2.setColor(new Color(140,140,255,50)); g2.drawOval(sx-rPx,sy-rPx,rPx*2,rPx*2);
                        
                        g2.setColor(new Color(255, 255, 255, 60)); g2.fillRect(sx-25, sy-25, 50, 50);
                        
                        String up = t.getLevel()<3 ? "Up:"+(t.getType().getCost()/2)+"g" : "MAX";
                        g2.setColor(Color.WHITE); g2.setFont(new Font("Serif",Font.BOLD,12)); g2.drawString(up,sx-28,sy-44);
                        // Sell info
                        int sellVal = t.getType().getCost()/2;
                        g2.setColor(new Color(255,100,100)); g2.setFont(new Font("Serif",Font.BOLD,11));
                        g2.drawString("Dx: Vendi +"+sellVal+"g",sx-32,sy-56);
                    }
                }
            }

            // ===== ENEMIES (truly different per type) =====
            for (Enemy en : model.getActiveEnemies()) {
                if (!en.isAlive()) continue;
                int ex = (int)en.getPixelX(), ey = (int)en.getPixelY();
                drawEnemy(g2, en, ex, ey);
            }

            // ===== PROJECTILES =====
            for (Projectile p : model.getProjectiles()) {
                int px = (int)p.getX(), py = (int)p.getY();
                switch (p.getSourceTowerType()) {
                    case BASIC:
                        g2.setColor(new Color(255,120,0,200)); g2.fillOval(px-6,py-6,12,12);
                        g2.setColor(new Color(255,200,50,100)); g2.fillOval(px-4,py-4,8,8); break;
                    case SNIPER:
                        g2.setColor(new Color(80,50,20)); g2.setStroke(new BasicStroke(3));
                        g2.drawLine(px-5,py-5,px+5,py+5); g2.setStroke(new BasicStroke(1));
                        g2.setColor(new Color(180,160,120)); g2.fillOval(px-3,py-3,6,6); break;
                    case RAPID:
                        g2.setColor(new Color(180,80,255,200)); g2.fillOval(px-5,py-5,10,10);
                        g2.setColor(new Color(220,160,255,100)); g2.fillOval(px-3,py-3,6,6); break;
                    case ICE:
                        g2.setColor(new Color(100,200,255,220));
                        g2.fillPolygon(new int[]{px,px-4,px,px+4}, new int[]{py-6,py,py+6,py}, 4);
                        g2.setColor(new Color(200,240,255,140)); g2.fillOval(px-3,py-3,6,6); break;
                }
            }

            // ===== ABILITY ANIMATIONS =====
            if (model.getFireAnimTicks() > 0) {
                float alpha = model.getFireAnimTicks() / 60f;
                g2.setColor(new Color(255, 60, 0, (int)(alpha * 80)));
                g2.fillRect(0, 0, pw, ph);
                // Falling fire particles
                java.util.Random fireRng = new java.util.Random(model.getFireAnimTicks());
                for (int i = 0; i < 30; i++) {
                    int fx = fireRng.nextInt(pw), fy = fireRng.nextInt(ph);
                    int fs = 4 + fireRng.nextInt(8);
                    g2.setColor(new Color(255, 100 + fireRng.nextInt(100), 0, (int)(alpha * 200)));
                    g2.fillOval(fx, fy, fs, fs);
                }
            }
            if (model.getFreezeAnimTicks() > 0) {
                float alpha = model.getFreezeAnimTicks() / 60f;
                g2.setColor(new Color(100, 200, 255, (int)(alpha * 60)));
                g2.fillRect(0, 0, pw, ph);
                // Ice crystals
                java.util.Random ir = new java.util.Random(model.getFreezeAnimTicks() + 500);
                for (int i = 0; i < 25; i++) {
                    int ix = ir.nextInt(pw), iy = ir.nextInt(ph);
                    g2.setColor(new Color(200, 230, 255, (int)(alpha * 180)));
                    int ss = 3 + ir.nextInt(6);
                    g2.fillPolygon(new int[]{ix, ix-ss, ix, ix+ss}, new int[]{iy-ss*2, iy, iy+ss*2, iy}, 4);
                }
            }

            // ===== HUD =====
            g2.setColor(C_UI); g2.fillRoundRect(8,8,195,80,14,14);
            g2.setColor(new Color(160,130,90)); g2.drawRoundRect(8,8,195,80,14,14);
            g2.setFont(new Font("Serif",Font.BOLD,15));
            g2.setColor(new Color(255,90,90)); g2.drawString("Vite: "+model.getPlayer().getBaseHealth(),18,30);
            g2.setColor(C_GOLD); g2.drawString("Oro: "+model.getPlayer().getCoins(),18,50);
            g2.setColor(Color.WHITE); g2.drawString("Ondata: "+model.getCurrentWave()+"/"+model.getTotalWaves(),18,70);

            // Abilities HUD (top right)
            int ax = pw - 165;
            g2.setColor(C_UI); g2.fillRoundRect(ax,8,155,55,10,10);
            g2.setColor(new Color(160,130,90)); g2.drawRoundRect(ax,8,155,55,10,10);
            g2.setFont(new Font("Serif",Font.BOLD,12));
            int fcd = model.getFireCooldown(), icd = model.getFreezeCooldown();
            g2.setColor(fcd > 0 ? Color.GRAY : new Color(255,100,50));
            g2.drawString(fcd > 0 ? "Fuoco CD:"+(fcd/60)+"s" : "[F] Fuoco", ax+10, 28);
            g2.setColor(icd > 0 ? Color.GRAY : new Color(100,200,255));
            g2.drawString(icd > 0 ? "Gelo CD:"+(icd/60)+"s" : "[G] Gelo", ax+10, 48);

            // Shop cards
            drawShop(g2);

            // Status
            if (model.getState() == GameState.VICTORY) {
                bigMsg(g2, "VITTORIA!", C_GOLD, pw, ph);
                g2.setFont(new Font("Serif",Font.PLAIN,16)); g2.setColor(new Color(255,255,255,180));
                String sub = "Ritorno alla mappa...";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(sub, (pw-fm.stringWidth(sub))/2, ph/2+20);
            } else if (model.getState() == GameState.DEFEAT) {
                bigMsg(g2, "SCONFITTA!", new Color(255,60,60), pw, ph);
            } else if (!model.isWaveInProgress() && model.getCurrentWave() < model.getTotalWaves()) {
                g2.setColor(new Color(255,255,255,190)); g2.setFont(new Font("Serif",Font.BOLD,16));
                String msg = "Premi [W] per l'ondata "+(model.getCurrentWave()+1);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(msg, (pw-fm.stringWidth(msg))/2, 106);
            }
        }

        void drawEnemy(Graphics2D g2, Enemy en, int ex, int ey) {
            EnemyType type = en.getType();
            int size;
            Image spr;
            Color fallback, outline;
            switch (type) {
                case FAST:
                    size = 22;
                    spr = spEnemyFast;
                    fallback = new Color(60, 200, 60);
                    outline = new Color(30, 140, 30);
                    break;
                case TANK:
                    size = 46;
                    spr = spEnemyTank;
                    fallback = new Color(120, 70, 30);
                    outline = new Color(80, 40, 15);
                    break;
                default:
                    size = 32;
                    spr = spEnemyBasic;
                    fallback = new Color(160, 90, 40);
                    outline = new Color(110, 60, 25);
                    break;
            }
            // Shadow
            g2.setColor(new Color(0, 0, 0, 35));
            g2.fillOval(ex - size / 3, ey + size / 4, size * 2 / 3, size / 4);
            if (spr != null) {
                g2.drawImage(spr, ex - size / 2, ey - size / 2, size, size, null);
                if (en.getHitFlashTicks() > 0) {
                    g2.setColor(new Color(255, 80, 80, 100));
                    g2.fillOval(ex - size / 2, ey - size / 2, size, size);
                }
            } else {
                // Detailed fallback: body + outline + type marker
                g2.setColor(fallback);
                g2.fillOval(ex - size / 2, ey - size / 2, size, size);
                g2.setColor(outline);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(ex - size / 2, ey - size / 2, size, size);
                g2.setStroke(new BasicStroke(1));
                // Eyes
                g2.setColor(Color.WHITE);
                g2.fillOval(ex - size / 5 - 2, ey - size / 6, size / 5, size / 5);
                g2.fillOval(ex + 2, ey - size / 6, size / 5, size / 5);
                g2.setColor(Color.BLACK);
                g2.fillOval(ex - size / 5, ey - size / 6 + 1, size / 8, size / 8);
                g2.fillOval(ex + 3, ey - size / 6 + 1, size / 8, size / 8);
                // Type label
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 8));
                String lbl = type == EnemyType.FAST ? "G" : type == EnemyType.TANK ? "B" : "O";
                g2.drawString(lbl, ex - 3, ey + size / 3);
            }
            // Health bar
            if (en.getHealth() < en.getMaxHealth()) {
                int bw = size + 4, bh = 5, bx = ex - bw / 2, by = ey - size / 2 - 10;
                g2.setColor(new Color(20, 20, 20, 200));
                g2.fillRect(bx - 1, by - 1, bw + 2, bh + 2);
                g2.setColor(new Color(50, 50, 50));
                g2.fillRect(bx, by, bw, bh);
                double pct = (double) en.getHealth() / en.getMaxHealth();
                g2.setColor(pct > .5 ? new Color(30, 200, 30)
                        : pct > .25 ? new Color(220, 200, 0) : new Color(200, 30, 30));
                g2.fillRect(bx, by, (int) (bw * pct), bh);
            }
        }

        void bigMsg(Graphics2D g2, String text, Color color, int pw, int ph) {
            g2.setFont(new Font("Serif", Font.BOLD, 50));
            FontMetrics fm = g2.getFontMetrics();
            int tx = (pw - fm.stringWidth(text)) / 2, ty = ph / 2 - 30;
            g2.setColor(new Color(0, 0, 0, 180));
            g2.drawString(text, tx + 3, ty + 3);
            g2.setColor(color);
            g2.drawString(text, tx, ty);
        }

        void drawShop(Graphics2D g2) {
            TowerType[] types = TowerType.values();
            int cw = 100, ch = 65, gap = 8, total = types.length * (cw + gap) - gap;
            int sx = (getWidth() - total) / 2, sy = getHeight() - ch - 12;
            g2.setColor(C_UI);
            g2.fillRoundRect(sx - 12, sy - 6, total + 24, ch + 12, 14, 14);
            g2.setColor(new Color(140, 115, 80));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(sx - 12, sy - 6, total + 24, ch + 12, 14, 14);
            g2.setStroke(new BasicStroke(1));
            for (int i = 0; i < types.length; i++) {
                TowerType t = types[i];
                int cx = sx + i * (cw + gap);
                boolean sel = t == ctrl.getSelectedTowerType(), aff = model.getPlayer().getCoins() >= t.getCost();
                g2.setColor(sel ? new Color(75, 115, 75) : new Color(50, 44, 36));
                g2.fillRoundRect(cx, sy, cw, ch, 8, 8);
                g2.setColor(sel ? C_GOLD : new Color(95, 80, 60));
                g2.setStroke(new BasicStroke(sel ? 2 : 1));
                g2.drawRoundRect(cx, sy, cw, ch, 8, 8);
                g2.setStroke(new BasicStroke(1));
                Image img = towerSprite(t);
                if (img != null)
                    g2.drawImage(img, cx + 3, sy + 3, 28, 38, null);
                g2.setFont(new Font("Serif", Font.BOLD, 10));
                g2.setColor(Color.WHITE);
                g2.drawString(t.getDescription(), cx + 34, sy + 18);
                g2.setFont(new Font("Serif", Font.BOLD, 12));
                g2.setColor(aff ? C_GOLD : new Color(200, 60, 60));
                g2.drawString(t.getCost() + "g", cx + 34, sy + 38);
                if (!aff) {
                    g2.setColor(new Color(0, 0, 0, 110));
                    g2.fillRoundRect(cx, sy, cw, ch, 8, 8);
                }
            }
        }

        static Image towerSprite(TowerType t) {
            switch (t) {
                case SNIPER:
                    return spTowerSniper;
                case RAPID:
                    return spTowerRapid;
                case ICE:
                    return spTowerIce;
                default:
                    return spTowerBasic;
            }
        }
    }

    // =================== PAUSE ===================
    public void showPauseMenu(GameController c) {
        SwingUtilities.invokeLater(() -> {
            if (pauseDialog != null)
                pauseDialog.dispose();
            pauseDialog = new JDialog(frame, "Pausa", true);
            pauseDialog.setSize(300, 270);
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            p.setBackground(new Color(30, 30, 40));
            p.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));
            JLabel tl = mkLabel("PAUSA", new Font("Serif", Font.BOLD, 22), C_GOLD);
            JButton rb = btn("Riprendi", new Color(0, 150, 80));
            rb.addActionListener(e -> c.togglePause());
            JButton rst = btn("Ricomincia", new Color(50, 120, 180));
            rst.addActionListener(e -> c.restartGame());
            JButton lvl = btn("Mappa Livelli", new Color(140, 120, 40));
            lvl.addActionListener(e -> c.backToLevelSelect());
            JButton mb = btn("Menu", new Color(150, 50, 50));
            mb.addActionListener(e -> c.backToMenu());
            p.add(tl);
            p.add(Box.createRigidArea(new Dimension(0, 12)));
            p.add(rb);
            p.add(Box.createRigidArea(new Dimension(0, 6)));
            p.add(rst);
            p.add(Box.createRigidArea(new Dimension(0, 6)));
            p.add(lvl);
            p.add(Box.createRigidArea(new Dimension(0, 6)));
            p.add(mb);
            pauseDialog.add(p);
            pauseDialog.setLocationRelativeTo(frame);
            pauseDialog.setVisible(true);
        });
    }

    public void hidePauseMenu() {
        SwingUtilities.invokeLater(() -> {
            if (pauseDialog != null) {
                pauseDialog.dispose();
                pauseDialog = null;
            }
        });
    }

    public void displayEndGame(GameState s) {
    }

    public void closeGameFrame() {
        if (frame != null) {
            frame.dispose();
            frame = null;
            mapPanel = null;
        }
        if (levelFrame != null) {
            levelFrame.dispose();
            levelFrame = null;
        }
    }

    // Helpers
    private static JLabel mkLabel(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setFont(font);
        l.setForeground(color);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
    }

    private static JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(260, 40));
        b.setFont(new Font("Serif", Font.BOLD, 15));
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}