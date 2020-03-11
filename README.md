# techtest

# Utilisation en lignes de commandes
Les librairies Commons-CLI, Jackson et Univocity ont été utilisées dans ce projet, il faudra donc les ajouter au classpath afin de lancer l'application en lignes de commandes.
La commande est récupérable via l'éditeur Eclipse (clic droit sur Main.java > Properties > Run/Debug Settings > selection de "Main" > Edit > Show Command Line
... Après réflexion, il y a sûrement un autre raccourci, mais je ne le connais pas !

Il est possible de rentrer 3 paramètres :
-> --file     suivi de l'absolute path vers un fichier tsv. Permet d'alimenter l'application avec un ficheir tsv personnalisé. Si ce paramètre n'est pas renseigné, un fichier tsv par défaut (celui fournit avec l'énoncé) est pris en compte.
-> --nbpoi    suivi d'un POI au format JSON. Le POI devra être encadré par des guillemets et les guillemets internes escaped.
-> --densest  suivi d'un entier qui détermine le nombre de zones à afficher.

## Exemples
Récupérer le nombre de PoI contenues dans la zone {"min_lat": 6.5,"min_lon": -7} avec une liste de PoI personnalisée
> java -cp ... com.happn.techtest.Main --file "D:/Mon/rep/avec/ma/listePoI.tsv" --nbpoi "{\"min_lat\": 6.5,\"min_lon\": -7}"

Récupérer les 3 zones les plus denses avec le fichier de base
> java -cp ... com.happn.techtest.Main --densest 3

# Modèle de données
Le développement du modèle de données a été effectué en TDD.
Le modèle de données est composé de 2 classes principales et d'une classe World qui sert à faire le lien avec les 2 autres et définir le contour du monde.
### POI
Classe représentant un point d'intérêt. Elle est caractérisée par 2 coordonnées et un id.
Les méthodes equals() et hashcode() ont été override afin de pouvoir comparer des objets créé manuellement à des objets de la base.
### Zone
Représente une zone de la carte. Elle est caractérisée par 4 coordonnées qui sont ses 4 coins. Une zone est ainsi un quadrilatère.
Je voulais que la Zone soit immuable. une fois complètement créée, elle ne devait pas pouvoir être modifiée pour plusieurs. On lui associe des Points d'intérêt et elle servira de clé pour dans une Map pour récupérer les POI associés. De fait, ses méthodes equals() et hashcode() ont été également override.
Ainsi, les setter autorisent un set initial, mais pas la modification des valeurs de la zone (une déclaration final des variable aurait pu faire l'affaire, mais obligeait l'utilisateur à définir dès la création toutes les caractéristiques de la Zone. C'est une solution qui me semble + robuste mais qui ne me permettait pas de créer une zone avec seulement une partie des coordonnées et compléter ensuite les autres paramètres).
### World
Représente le monde.
Pour le besoin du test, il n'est nécessaire que de connaître le nombre de POI associés à chaque zone... ainsi, World contient une Map de tous les PoI injectés en entrée regroupés par Zone.

# Définition d'appartenance à une zone
Une information était floue, il était difficile de savoir dans quelle limite un PoI appartient à une zone. Les bords sont-ils inclusifs ? exclusifs ? les 2 ?
J'ai fait le choix de les rendre inclusif. Une zone contient toutes les valeurs <= à ses bordure max et >= à ses bordures min. Ainsi, un point d'intérêt qui serait sur une coordonnées partagée par plusieurs zones appartient à ces différentes zones.
Exemple : Un point de coordonnées {(0, 0), (0, 0)} appartient à 4 zones :
{(-0.5, 0), (-0.5, 0)}
{(-0.5, 0), (0, 0.5)}
{(0, 0.5), (-0.5, 0)}
{(0, 0.5), (0, 0.5)}
Cette classe ne créé que les zones ayant un point d'intérêt associé. Pour cet exercice, cela permet de limiter les traitements uniquement au besoin finale. J'ai pris le parti de considérer que les zones sans point d'intérêt... n'ont pas d'intérêt :p
De la même manière, les points hors monde ne sont pas pris en compte.

# Accès aux données
### WorldManager
L'accès aux données est effectué par la classe WorldManager.
Cette classe contient l'état actuel du monde en paramètre et permet ainsi de parcourir les données qui y sont enregistrées. Elle expose les méthodes qui seront exposées pour appel par CLI ou REST.
Il faudrait sans doute créer une interface sur cette classe ou un abstract pour permettre différente implémentations en fonction des appellant (CLI ou REST, ou autre futur) mais au moment de la rédaction de ce readme, je n'ai pas pris le temps de le faire.
#### Méthode getNumberOfPoiInZone(Zone zone)
Retourne le nombre de POI pour une zone donnée en recherchant dans la Map les POI associés à une zone (créée à partir d'une zone partielle fournie en entrée).
- Dans le cas du CLI, le monde doit être instancié à chaque appel en ligne de commande... Il serait pénible de reconstruire toutes les données de la map à chaque fois. Au final, seuls la zone demandée à besoin de connaitre ses points d'intérêt. Ainsi, pour accélérer le processus, la méthode est appelée avec un World qui est en fait la Zone cible.
De plus, si la zone est hors monde, le calcul est tout de même fait dans le cas du CLI. Le contrôle n'est pas codé pour le moment, mais le comportement ne me semble pas abberrant. Au final, cette méthode peut simplement demander le nombre de PoI pour une zone donnée, indépendemment du monde.
- Dans le cas d'un Service REST, le World doit être instancié au démarrage du serveur... la Map est ainsi créée au démarrage et n'est plus retouchée. Les coordonnées par défaut peuvent être utilisées. La méthode ne fera que rechercher une clé dans une Map. Aucun traitement n'est à refaire.
#### Méthode getnDensestZone(int numberOfZones)
Retourne les n zones les plus denses. (Le traitement en CLI et REST est le même pour cette méthode, il faut de toutes facon calculer toutes les zones du monde pour pouvoir trier).
L'algo se contente de trier la map par nombre de PoI associé et renvoie les n premières entrées.
Pour le moment, il y a donc certaines limitations :
- si le nombre entré est > au nombre de zones créées via les POI, toutes les zones seront retournée, mais il n'y aura pas de creation de zones vides pour compléter
- il n'y a pas de règle pour définir une priorité quand il y a plusieurs zones de même densité (si l'ont demande les 2 zones de plus grosse densité et que dans le monde, il y a 4 zones de densité égales... 2 parmi celles ci seront retournées.)

# CLI
Le Main.java contient le traitement des entrants et la configuration du World préalable à l'appel des méthodes métier.
Je pense qu'il faudrait découper un peu mieux cette partie. Extraire une partie générique et extraire les différentes configurations. Je pensais voir + en détail l'organisation du code et le découpage des classes lorsque je mettrai en place le service REST. Mais cela n'aura sans doute pas lieu, n'ayant pas eu le temps de retravailler sur le projet avant la deadline.
Des classes de conversion JSON et TSV sont également présentes pour s'occuper de la transformation des données pour nourrir le WorldManager.

# REST
TODO

# Conclusion
Dans l'ensemble, j'ai tenté de créer une architecture qui soit évolutive, tout en restant efficace.
En continuant de coder et en ajoutant des fonctionnalité ou des façons de présenter les données. L'archi évoluera forcément, mais les TU mis en place permettront de sécuriser ces refactorisations.
