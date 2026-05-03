# TowerSiege – Gioco Tower Defense in Java

## Obiettivo del Progetto

Il gruppo si pone come obiettivo quello di realizzare una versione digitale single player del gioco **"Tower Defense"** chiamata **TowerSiege**.

Tower Defense è un genere di videogioco strategico in cui il giocatore deve difendere una base o un percorso dall'arrivo di ondate di nemici. Per farlo, si posizionano torri o difese lungo il percorso in slot predefiniti, ognuna con abilità e caratteristiche diverse. L'obiettivo è gestire risorse e strategie per fermare i nemici prima che raggiungano il traguardo. Il gameplay combina pianificazione, gestione e adattamento continuo alla difficoltà crescente.

---

## Funzionalità Minime (Obbligatorie)

- Gestione delle ondate di nemici
- Gestione delle torri (posizionamento e attacco)
- Gestione della mappa e dei percorsi dei nemici su griglia 16×12
- Sistema di danno ai nemici e proiettili
- Condizioni di vittoria e sconfitta
- Sistema di potenziamento delle torri (fino al livello 3)
- Gestione dei punti vita (base, nemici)
- Sistema di risorse (monete ottenute eliminando i nemici)

## Funzionalità Opzionali

- Mappe multiple selezionabili (3 livelli di difficoltà crescente)
- Schermata iniziale e menu di navigazione
- Abilità speciali: Pioggia di Fuoco e Gelo Globale
- Grafica animata con sprite sheet dei nemici (zombie)
- Decorazioni procedurali della mappa (alberi, rocce, arbusti)

## Challenge Principali

- La gestione coerente tra nemici e le torri su sistema a griglia
- Aggiornamento coerente dell'interfaccia grafica (60 fps con Swing Timer)
- Implementazione dei potenziamenti delle torri e dei nemici

---

## Architettura del Progetto (Pattern MVC)

Il progetto segue il pattern architetturale **Model–View–Controller (MVC)**:

| Layer | Package | Responsabilità |
|---|---|---|
| **Model** | `model/` | Stato del gioco, logica, dati |
| **View** | `view/` | Rendering grafico (Swing/Java2D) |
| **Controller** | `controller/` | Ciclo di gioco, input utente |
| **Commons** | `commons/` | Costanti condivise, caricamento mappe |
| **Application** | `application/` | Punto di ingresso (`main`) |

---

## Piano di Sviluppo per Commit

Di seguito è riportata la storia completa dello sviluppo, organizzata commit per commit, con l'elenco preciso di **ogni file Java creato**, **le classi al suo interno** e **i metodi implementati**.

---

### Commit 1 – `5c04f87` · Primo caricamento del gioco

**Descrizione:** Creazione dello scheletro iniziale del progetto con tutte le classi base. La mappa è definita tramite waypoint pixel e il gioco è già funzionante nelle sue meccaniche fondamentali.

---

#### 📁 `application/TowerDefense.java`

**Classe:** `TowerDefense`

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `main(String[] args)` | `public static` | Punto di ingresso del programma. Crea `GameModel`, `GameView`, `GameController` e chiama `controller.start()` |

---

#### 📁 `commons/GameConstants.java`

**Classe:** `GameConstants`

| Costante | Tipo | Valore | Descrizione |
|---|---|---|---|
| `TILE_SIZE` | `int` | `50` | Dimensione in pixel di ogni cella della griglia |
| `COLS` | `int` | `16` | Numero di colonne della griglia |
| `ROWS` | `int` | `12` | Numero di righe della griglia |

---

#### 📁 `commons/MapData.java`

**Classe:** `MapData` (record/POJO di trasferimento dati JSON → oggetti Java)

| Campo | Tipo | Descrizione |
|---|---|---|
| `grid` | `int[][]` | Matrice 16×12: `0`=erba, `1`=sentiero, `2`=costruibile |
| `waypoints` | `List<double[]>` | Lista di punti (pixel) che i nemici seguono |
| `buildingSpots` | `List<int[]>` | Lista di posizioni `[col, row]` dove piazzare torri |

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `getGrid()` | `public` | Restituisce la matrice griglia |
| `getWaypoints()` | `public` | Restituisce la lista waypoint |
| `getBuildingSpots()` | `public` | Restituisce la lista degli slot edificabili |

---

#### 📁 `commons/MapLoader.java`

**Classe:** `MapLoader`

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `load(String resourcePath)` | `public static` | Legge il file JSON dalla cartella `resources/maps/`, fa il parsing con Gson e restituisce un oggetto `MapData` |

---

#### 📁 `model/GameState.java`

**Enum:** `GameState`

| Valore | Descrizione |
|---|---|
| `MENU` | Schermata del menu principale |
| `LEVEL_SELECT` | Schermata di selezione livello |
| `PLAYING` | Partita in corso |
| `PAUSED` | Partita in pausa |
| `VICTORY` | Il giocatore ha vinto |
| `DEFEAT` | Il giocatore ha perso |

---

#### 📁 `model/EnemyType.java`

**Enum:** `EnemyType`

| Valore | Salute | Velocità | Ricompensa | Descrizione |
|---|---|---|---|---|
| `BASIC` | 100 | 1.0 | 10 monete | Nemico standard (zombie villager 1) |
| `FAST` | 60 | 2.0 | 15 monete | Nemico veloce (zombie villager 2) |
| `TANK` | 300 | 0.5 | 30 monete | Nemico resistente (zombie villager 3) |

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `getHealth()` | `public` | Restituisce la salute base del tipo |
| `getSpeed()` | `public` | Restituisce la velocità base del tipo |
| `getReward()` | `public` | Restituisce le monete guadagnate all'eliminazione |

---

#### 📁 `model/TowerType.java`

**Enum:** `TowerType`

| Valore | Costo | Danno | Gittata | Cooldown | Descrizione |
|---|---|---|---|---|---|
| `BASIC` | 100 | 20 | 3 | 60 tick | Torre standard |
| `SNIPER` | 150 | 60 | 6 | 120 tick | Alta gittata, danno elevato |
| `RAPID` | 120 | 10 | 2 | 20 tick | Tiro rapido |
| `ICE` | 130 | 15 | 3 | 50 tick | Rallenta i nemici |

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `getCost()` | `public` | Restituisce il costo in monete |
| `getDamage()` | `public` | Restituisce il danno base |
| `getRange()` | `public` | Restituisce la gittata (in unità griglia) |
| `getCooldown()` | `public` | Restituisce il cooldown in tick tra un attacco e l'altro |

---

#### 📁 `model/Player.java`

**Classe:** `Player`

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `Player(int startCoins, int baseHealth)` | `public` | Costruttore: inizializza monete e vite della base |
| `getCoins()` | `public` | Restituisce le monete correnti |
| `getBaseHealth()` | `public` | Restituisce i punti vita rimasti alla base |
| `addCoins(int amount)` | `public` | Aggiunge monete al giocatore |
| `spendCoins(int amount)` | `public` | Sottrae monete (usata per comprare torri) |
| `damageBase(int amount)` | `public` | Riduce i punti vita della base quando un nemico la raggiunge |
| `canAfford(int cost)` | `public` | Ritorna `true` se il giocatore ha abbastanza monete |

---

#### 📁 `model/BuildingSpot.java`

**Classe:** `BuildingSpot`

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `BuildingSpot(int col, int row)` | `public` | Costruttore: salva le coordinate griglia della cella |
| `getCol()` | `public` | Restituisce la colonna |
| `getRow()` | `public` | Restituisce la riga |
| `getPixelCenterX()` | `public` | Calcola e restituisce il centro X in pixel (`col * 50 + 25`) |
| `getPixelCenterY()` | `public` | Calcola e restituisce il centro Y in pixel (`row * 50 + 25`) |
| `isOccupied()` | `public` | Ritorna `true` se c'è una torre su questo slot |
| `getTower()` | `public` | Restituisce la torre presente (o `null`) |
| `setTower(Tower t)` | `public` | Posiziona una torre sullo slot |
| `clearTower()` | `public` | Rimuove la torre dallo slot (vendita) |

---

#### 📁 `model/Tower.java`

**Classe:** `Tower`

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `Tower(TowerType type)` | `public` | Costruttore: crea una torre di livello 1 del tipo specificato |
| `setPosition(double x, double y)` | `public` | Imposta la posizione pixel al centro della cella |
| `tick()` | `public` | Decrementa il cooldown di attacco ad ogni frame |
| `isEnemyInRange(Enemy enemy)` | `public` | Controlla se un nemico è entro la gittata della torre |
| `attack(Enemy enemy)` | `public` | Spara un proiettile se il cooldown è 0 e il nemico è in range; restituisce un `Projectile` |
| `upgrade()` | `public` | Incrementa il livello della torre (max 3) |
| `getType()` | `public` | Restituisce il tipo (`TowerType`) |
| `getLevel()` | `public` | Restituisce il livello corrente |
| `getDamage()` | `public` | Restituisce il danno attuale (base + bonus livello) |
| `getRange()` | `public` | Restituisce la gittata in unità griglia |

---

#### 📁 `model/Enemy.java`

**Classe:** `Enemy`

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `Enemy(EnemyType type, int waveNumber)` | `public` | Costruttore: inizializza salute, velocità, ricompensa scalate col numero d'ondata |
| `takeDamage(int damage)` | `public` | Riduce la salute e attiva l'effetto "flash rosso" per 8 frame |
| `setReachedEnd(boolean val)` | `public` | Segna se il nemico ha raggiunto la fine del percorso |
| `isReachedEnd()` | `public` | Controlla se il nemico è arrivato alla base |
| `setCoinAwarded(boolean val)` | `public` | Evita che la ricompensa venga assegnata più volte |
| `isCoinAwarded()` | `public` | Controlla se la moneta è già stata assegnata |
| `applySlow(double multiplier, int durationTicks)` | `public` | Applica il rallentamento da torre ICE |
| `updateStatus()` | `public` | Aggiorna i timer (slow, flash); chiamato ogni tick |
| `getEffectiveSpeed()` | `public` | Calcola la velocità corrente tenendo conto del rallentamento |
| `moveAlongPath(List<double[]> waypoints)` | `public` | Muove il nemico di un passo verso il prossimo waypoint; ritorna `true` se ha raggiunto la fine |
| `tickVisuals()` | `public` | Decrementa `hitFlashTicks` per animare il colpo ricevuto |
| `getPixelX()` / `getPixelY()` | `public` | Restituiscono la posizione corrente in pixel |
| `setPosition(double x, double y)` | `public` | Imposta la posizione iniziale di spawn |
| `getType()`, `getHealth()`, `getMaxHealth()`, `getReward()`, `isAlive()`, `getHitFlashTicks()` | `public` | Getter dello stato del nemico |

---

#### 📁 `model/Projectile.java`

**Classe:** `Projectile`

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `Projectile(Tower source, Enemy target)` | `public` | Costruttore: calcola la traiettoria verso il bersaglio |
| `update()` | `public` | Muove il proiettile di un passo verso il bersaglio |
| `hasHit()` | `public` | Ritorna `true` se il proiettile ha raggiunto il bersaglio |
| `getX()` / `getY()` | `public` | Posizione corrente del proiettile |
| `getSourceTowerType()` | `public` | Tipo della torre che ha sparato (usato per il rendering del proiettile) |
| `getTarget()` | `public` | Il nemico bersaglio |

---

#### 📁 `model/Wave.java`

**Classe:** `Wave`

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `Wave(int levelNum)` | `public` | Costruttore: genera la lista delle ondate in base al livello |
| `getTotalWaves()` | `public` | Numero totale di ondate del livello |
| `getWave(int index)` | `public` | Restituisce la lista di `EnemyType` da spawnare nell'ondata `index` |

---

#### 📁 `model/Map.java`

**Classe:** `Map`

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `Map(MapData data)` | `public` | Costruttore: riceve i dati JSON parsati e costruisce la griglia 16×12 |
| `getGrid()` | `public` | Restituisce la matrice `int[][]` con il tipo di ogni cella |
| `getWaypoints()` | `public` | Restituisce la lista dei waypoint pixel che i nemici seguono |
| `getBuildingSpots()` | `public` | Restituisce la lista degli slot disponibili per le torri |
| `isInsideBuildableCell(int col, int row)` | `public` | Controlla se la cella è di tipo costruibile (`2`) |

---

#### 📁 `model/GameModel.java`

**Classe:** `GameModel`

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `GameModel(String mapPath)` | `public` | Costruttore: carica la mappa, crea `Player`, `Wave`, liste nemici e proiettili |
| `loadLevel(int levelNum)` | `public` | Carica il file `level{N}.json` e reinizializza mappa e ondate |
| `start()` | `public` | Porta il gioco in stato `PLAYING` e azzera lo stato |
| `startNextWave()` | `public` | Aggiunge i nemici dell'ondata corrente alla coda di spawn |
| `update()` | `public` | **Tick principale del gioco** (chiamato 60×/sec): spawn nemici, movimento, attacco torri, controllo vittoria/sconfitta |
| `buildTowerOnSpot(Tower tower, BuildingSpot spot)` | `public` | Acquista e piazza una torre se il giocatore ha abbastanza monete |
| `upgradeTower(Tower tower)` | `public` | Potenzia la torre se sotto il livello massimo e con monete sufficienti |
| `sellTower(BuildingSpot spot)` | `public` | Rimuove la torre e rimborsa metà del costo |
| `castRainOfFire()` | `public` | Abilità speciale: danneggia tutti i nemici attivi |
| `castGlobalFreeze()` | `public` | Abilità speciale: rallenta tutti i nemici per qualche secondo |
| `pause()` / `resume()` | `public` | Cambia lo stato del gioco tra `PLAYING` e `PAUSED` |
| `getState()` / `setState()` | `public` | Getter/setter dello stato corrente |
| `getMap()`, `getPlayer()`, `getActiveEnemies()`, `getProjectiles()` | `public` | Getter per i sotto-componenti del modello |
| `getCurrentWave()`, `getTotalWaves()`, `isWaveInProgress()` | `public` | Informazioni sull'ondata corrente |
| `getFireCooldown()`, `getFreezeCooldown()`, `getFireAnimTicks()`, `getFreezeAnimTicks()` | `public` | Stato delle abilità speciali |
| `getCurrentLevel()`, `getMaxUnlockedLevel()`, `isVictoryRedirectReady()` | `public` | Progressione livelli |

---

#### 📁 `controller/GameController.java`

**Classe:** `GameController`

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `GameController(GameModel model, GameView view)` | `public` | Costruttore: crea il `Timer` Swing che chiama `model.update()` e `view.displayGameState()` ogni 16ms (~60fps) |
| `start()` | `public` | Mostra il menu principale |
| `beginGame()` | `public` | Naviga alla schermata di selezione livello |
| `startLevel(int level)` | `public` | Carica e avvia un livello specifico, fa partire il game loop |
| `togglePause()` | `public` | Alterna tra pausa e gioco in corso |
| `restartGame()` | `public` | Riavvia il livello corrente |
| `backToMenu()` | `public` | Torna al menu principale fermando il game loop |
| `backToLevelSelect()` | `public` | Torna alla mappa dei livelli |
| `forceNextWave()` | `public` | Avvia la prossima ondata anticipatamente (tasto `W`) |
| `triggerRainOfFire()` | `public` | Attiva l'abilità fuoco (tasto `F`) |
| `triggerGlobalFreeze()` | `public` | Attiva l'abilità gelo (tasto `G`) |
| `interactWithSpot(BuildingSpot spot)` | `public` | Gestisce il click sinistro su uno slot: piazza o potenzia una torre |
| `sellTowerAtSpot(BuildingSpot spot)` | `public` | Gestisce il click destro su uno slot: vende la torre |
| `setSelectedTowerType(TowerType type)` | `public` | Cambia il tipo di torre selezionato dal negozio in basso |
| `getSelectedTowerType()` | `public` | Restituisce il tipo di torre attualmente selezionato |

---

#### 📁 `view/GameView.java`

**Classe:** `GameView`

| Metodo | Visibilità | Descrizione |
|---|---|---|
| `displayWelcome()` | `public` | Inizializza la finestra principale |
| `showStartMenu(GameController c)` | `public` | Disegna il menu principale con titolo, pulsanti e istruzioni |
| `showLevelSelect(GameController c, GameModel model)` | `public` | Mostra la mappa interattiva con i 3 nodi livello (bloccati/sbloccati) |
| `displayGameState(GameModel model, GameController c)` | `public` | Aggiorna la finestra di gioco ad ogni tick (chiamato dal game loop) |
| `showPauseMenu(GameController c)` | `public` | Mostra il dialogo di pausa |
| `hidePauseMenu()` | `public` | Nasconde il dialogo di pausa |
| `closeGameFrame()` | `public` | Chiude la finestra di gioco corrente |
| `displayEndGame(GameState s)` | `public` | Mostra schermata finale (vittoria o sconfitta) |
| `paintComponent(Graphics g)` | `protected` (inner `MapPanel`) | **Metodo di rendering principale**: disegna la griglia tile per tile, poi decorazioni erba, poi torri, poi nemici, poi proiettili, poi HUD |
| `drawEnemy(Graphics2D g2, Enemy en, int ex, int ey)` | `void` (inner) | Disegna un singolo nemico con sprite animata (o fallback geometrico) e barra HP |
| `drawShop(Graphics2D g2)` | `void` (inner) | Disegna il pannello negozio in basso con le 4 carte torre selezionabili |
| `bigMsg(Graphics2D g2, String text, Color color, int pw, int ph)` | `void` (inner) | Mostra un messaggio grande al centro dello schermo (es. "VITTORIA!") |

---

### Commit 2 – `783ac97` · Refactoring: transizione a griglia, sistema animazione, nuovi asset

**Descrizione:** Migrazione completa dal sistema a coordinate continue (pixel liberi) a un **sistema a griglia 16×12 con tile da 50px**. Aggiunta del rendering decorativo procedurale, integrazione dei nuovi pacchetti immagini (Pack 2 – eroi/torri, Pack 3 – zombie animati).

#### File modificati:

**`commons/GameConstants.java`** — Aggiunte le costanti `TILE_SIZE = 50`, `COLS = 16`, `ROWS = 12`.

**`model/BuildingSpot.java`** — I campi `x, y` cambiano da `double` (pixel) a `int` (colonna/riga). Aggiunti `getPixelCenterX()` e `getPixelCenterY()`.

**`model/Map.java`** — Aggiunta la matrice `int[][] grid`. Il parsing dei waypoint ora converte le coordinate griglia in pixel centrali (`col * 50 + 25`).

**`view/GameView.java`** — Riscrittura del metodo `paintComponent`:
- Il rendering avviene con un doppio ciclo `for (r=0..11) for (c=0..15)` che legge `grid[r][c]`
- Le celle d'erba (tipo `0`) ottengono decorazioni procedurali casuali: alberi (`imgTree`), rocce (`imgRock`), arbusti (`imgBush`), fiori
- Le celle sentiero (tipo `1`) hanno texture sabbia/fango
- Le celle costruibili (tipo `2`) hanno una base in pietra grigia
- La logica click (`interactWithSpot`) ora usa `col = x / 50`, `row = y / 50` invece della distanza euclidea
- Caricamento degli sprite delle torri da **Pack 2** (sprite strip 576×96, 6 frame da 96px)
- Caricamento di 24 frame PNG per ogni zombie da **Pack 3** negli array `spEnemyBasicAnim[]`, `spEnemyFastAnim[]`, `spEnemyTankAnim[]`
- I nemici vengono animati leggendo il frame corretto con `(System.currentTimeMillis() / 40) % 24`

---

### Commit 3 – `aa851c9` · Test: cambio titolo a "Definitive Edition" _(revertito)_

**Descrizione:** Commit di test per verificare che le modifiche al codice si riflettessero nel gioco in esecuzione. Il titolo `"TOWERSIEGE"` è stato temporaneamente cambiato in `"TOWERSIEGE: DEFINITIVE EDITION"` per confermare la visibilità delle modifiche.

File modificato: **`view/GameView.java`** → metodo `showStartMenu()`, parametro stringa del `JLabel title`.

---

### Commit 4 – `42b0a9b` · Ripristino titolo e correzione sistema animazione

**Descrizione:** Ripristino del titolo originale `"TOWERSIEGE"`. Correzione definitiva del sistema di caricamento immagini da `ImageIcon(URL)` a `ImageIO.read(InputStream)` per risolvere i crash silenziosi causati dagli spazi nei nomi delle cartelle (es. `PNG Sequences`). Applicazione corretta degli array di animazione su nemici e torri.

File modificato: **`view/GameView.java`**:
- Metodo `loadImg()` → usa `cl.getResourceAsStream(path)` + `ImageIO.read(is)`
- Metodo `drawEnemy()` → usa `spEnemyBasicAnim[frame]`, `spEnemyFastAnim[frame]`, `spEnemyTankAnim[frame]`
- Metodo `paintComponent()` → le torri usano `drawImage` con ritaglio: `g2.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null)` per estrarre il frame corretto dalla strip

---

## Struttura delle Cartelle

```
Progetto OOP/
├── src/main/java/it/unibo/towerdefense/
│   ├── application/
│   │   └── TowerDefense.java          ← main()
│   ├── commons/
│   │   ├── GameConstants.java          ← TILE_SIZE, COLS, ROWS
│   │   ├── MapData.java                ← DTO per JSON delle mappe
│   │   └── MapLoader.java              ← Carica JSON → MapData
│   ├── controller/
│   │   └── GameController.java         ← Game loop, input, navigazione
│   ├── model/
│   │   ├── GameState.java              ← Enum stati del gioco
│   │   ├── EnemyType.java              ← Enum tipi nemici
│   │   ├── TowerType.java              ← Enum tipi torri
│   │   ├── Player.java                 ← Monete e vite base
│   │   ├── BuildingSpot.java           ← Slot griglia per torri
│   │   ├── Tower.java                  ← Logica torre e attacco
│   │   ├── Enemy.java                  ← Movimento e danni nemico
│   │   ├── Projectile.java             ← Proiettile in volo
│   │   ├── Wave.java                   ← Generazione ondate
│   │   ├── Map.java                    ← Griglia 16×12 + waypoint
│   │   └── GameModel.java              ← Stato centrale del gioco
│   └── view/
│       └── GameView.java               ← Tutto il rendering Swing
└── src/main/resources/
    ├── maps/
    │   ├── level1.json                 ← Mappa Foresta (Facile)
    │   ├── level2.json                 ← Mappa Pianura (Medio)
    │   └── level3.json                 ← Mappa Montagna (Difficile)
    └── images/
        ├── Images pack/Assets/         ← Pack 1: props natura
        ├── pack2/1/, pack2/2/, pack2/3/ ← Pack 2: eroi/torri (sprite strip)
        └── pack3/Zombie_Villager_*/    ← Pack 3: zombie animati (24 frame PNG)
```

---

## Come Eseguire il Gioco

```bat
run_game.bat
```

Il file `run_game.bat` esegue automaticamente:
1. Compilazione di tutti i file `.java`
2. Copia delle risorse nella cartella `build/classes`
3. Avvio del gioco con `java -cp build/classes it.unibo.towerdefense.application.TowerDefense`

### Controlli di Gioco

| Tasto / Azione | Effetto |
|---|---|
| **Click sinistro** su slot libero | Piazza la torre selezionata |
| **Click sinistro** su torre esistente | Potenzia la torre (+1 livello, max 3) |
| **Click destro** su torre | Vende la torre (rimborso 50%) |
| **W** | Avvia la prossima ondata |
| **F** | Abilità speciale: Pioggia di Fuoco |
| **G** | Abilità speciale: Gelo Globale |
| **P** / **ESC** | Pausa / Riprendi |