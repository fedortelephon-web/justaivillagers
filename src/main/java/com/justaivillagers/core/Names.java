package com.justaivillagers.core;

import java.util.List;
import java.util.Locale;

/**
 * Name and trait pools. Kept out of prompt code so a new language is one file, not ten switches.
 *
 * <p>Every name is male on purpose: the village has no women and no mothers. Children have a father
 * and nothing else, which the prompts state as a hard rule.
 */
public final class Names {

    private Names() {
    }

    private static final List<String> RU_ADULT = List.of(
            "Фёдор", "Иван", "Пётр", "Семён", "Аркадий", "Григорий", "Матвей", "Тимофей", "Варфоломей",
            "Кузьма", "Демьян", "Ерофей", "Захар", "Игнат", "Лука", "Мирон", "Никита", "Остап",
            "Прохор", "Родион", "Савва", "Тихон", "Устин", "Фома", "Яков",
            "Аким", "Алексей", "Анатолий", "Андрей", "Антон", "Арсений", "Афанасий", "Богдан",
            "Борис", "Вадим", "Валентин", "Валерий", "Василий", "Виктор", "Виталий", "Владимир",
            "Владислав", "Всеволод", "Вячеслав", "Гаврила", "Геннадий", "Георгий", "Герман", "Глеб",
            "Даниил", "Денис", "Дмитрий", "Дорофей", "Евгений", "Евдоким", "Егор", "Елисей",
            "Емельян", "Ефим", "Захария", "Зиновий", "Илларион", "Иннокентий", "Иосиф", "Ипатий",
            "Кирилл", "Клим", "Кондрат", "Константин", "Корней", "Лаврентий", "Леонид", "Лукас",
            "Максим", "Марк", "Мартын", "Михаил", "Мстислав", "Назар", "Нестор", "Олег",
            "Онисим", "Павел", "Панкрат", "Пахом", "Платон", "Поликарп", "Потап", "Радислав",
            "Ратмир", "Роман", "Ростислав", "Руслан", "Савелий", "Святослав", "Северин", "Сергей",
            "Сила", "Спиридон", "Станислав", "Степан", "Тарас", "Терентий", "Трофим", "Ульян",
            "Фаддей", "Феликс", "Феодосий", "Филат", "Филипп", "Фрол", "Харитон", "Эдуард",
            "Юлиан", "Юрий", "Ярослав"
    );

    private static final List<String> RU_CHILD = List.of(
            "Даня", "Ваня", "Петя", "Сёма", "Миша", "Коля", "Тёма", "Лёша", "Гоша", "Стёпа",
            "Алёша", "Андрюша", "Боря", "Васенька", "Витя", "Володя", "Гриша", "Дениска", "Дима",
            "Егорка", "Женя", "Илюша", "Кирюша", "Костя", "Лёня", "Максимка", "Марк", "Митя",
            "Никитка", "Павлик", "Проша", "Рома", "Савка", "Серёжа", "Слава", "Тимоша", "Толя",
            "Федя", "Филя", "Юра", "Яша"
    );

    private static final List<String> EN_ADULT = List.of(
            "Andrew", "Matthew", "Henry", "Ryan", "Arthur", "Bernard", "Cedric", "Duncan", "Edmund",
            "Frederick", "Gareth", "Harold", "Jasper", "Lionel", "Miles", "Nigel", "Oswald",
            "Percival", "Quentin", "Rupert", "Silas", "Terrence", "Wesley",
            "Alan", "Albert", "Alfred", "Arnold", "Barry", "Basil", "Bertram", "Bruce",
            "Calvin", "Charles", "Clarence", "Clive", "Colin", "Conrad", "Curtis", "Cyril",
            "Damian", "Daniel", "Dennis", "Desmond", "Donald", "Douglas", "Edgar", "Edwin",
            "Elliott", "Emory", "Eric", "Ernest", "Eugene", "Evan", "Felix", "Fergus",
            "Fletcher", "Francis", "Frank", "Gerald", "Gilbert", "Gordon", "Graham", "Gregory",
            "Guy", "Harvey", "Herbert", "Horace", "Howard", "Hugh", "Ian", "Isaac",
            "Ivor", "Jacob", "James", "Jarvis", "Jerome", "John", "Julian", "Keith",
            "Kenneth", "Laurence", "Leonard", "Leslie", "Lewis", "Malcolm", "Marcus", "Martin",
            "Maurice", "Maxwell", "Michael", "Milton", "Nathan", "Neil", "Nicholas", "Norman",
            "Oliver", "Oscar", "Patrick", "Paul", "Peter", "Philip", "Ralph", "Raymond",
            "Reginald", "Richard", "Robert", "Roger", "Roland", "Ronald", "Russell", "Samuel",
            "Sebastian", "Simon", "Stanley", "Stephen", "Stuart", "Sydney", "Thomas", "Timothy",
            "Tobias", "Trevor", "Victor", "Vincent", "Walter", "Warren", "Wilfred", "William"
    );

    private static final List<String> EN_CHILD = List.of(
            "Danny", "Tommy", "Billy", "Charlie", "Freddie", "Georgie", "Harry", "Jamie", "Kit", "Ollie",
            "Alfie", "Archie", "Benny", "Bertie", "Bobby", "Eddie", "Ernie", "Frankie", "Gus", "Hugo",
            "Jack", "Joey", "Johnny", "Laurie", "Leo", "Louie", "Milo", "Ned", "Oscar", "Percy",
            "Reggie", "Ronnie", "Sammy", "Sid", "Sonny", "Stan", "Teddy", "Toby", "Wally", "Will"
    );

    private static final List<String> EN_SURNAMES = List.of(
            "Ashford", "Bancroft", "Barley", "Blackwood", "Bramble", "Brook", "Cartwright", "Chandler",
            "Clayton", "Cobb", "Cooper", "Crane", "Dunmore", "Easton", "Fairfax", "Fenwick", "Fletcher",
            "Foster", "Gale", "Goddard", "Greenhill", "Hale", "Harlow", "Hayward", "Hedgerow", "Holloway",
            "Holmes", "Hunter", "Kendrick", "Langley", "Lockwood", "Marlow", "Mason", "Mercer", "Milford",
            "Miller", "Nash", "Netherby", "Oakley", "Osgood", "Palmer", "Pike", "Prescott", "Quincy",
            "Radcliffe", "Reeve", "Rowe", "Rushton", "Sawyer", "Shepherd", "Smith", "Sparrow", "Stafford",
            "Stanton", "Stroud", "Sutton", "Tanner", "Thatcher", "Thorne", "Trent", "Underhill", "Vance",
            "Wade", "Ward", "Waverly", "Weaver", "Weller", "Weston", "Whitaker", "Wickham", "Winslow",
            "Woodward", "Wren", "Yardley"
    );

    private static final List<String> RU_SURNAMES = List.of(
            "Абрамов", "Агеев", "Аксёнов", "Александров", "Андреев", "Анисимов", "Антонов", "Артемьев",
            "Афанасьев", "Баранов", "Белов", "Беляев", "Бирюков", "Блинов", "Богданов", "Большаков",
            "Борисов", "Быков", "Варламов", "Васильев", "Верещагин", "Виноградов", "Власов", "Волков",
            "Воробьёв", "Воронин", "Гаврилов", "Герасимов", "Голубев", "Горбунов", "Гордеев", "Григорьев",
            "Громов", "Гуляев", "Гусев", "Данилов", "Дементьев", "Денисов", "Дроздов", "Евдокимов",
            "Егоров", "Елисеев", "Ермаков", "Ершов", "Ефимов", "Жуков", "Зайцев", "Захаров",
            "Зимин", "Зуев", "Иванов", "Игнатов", "Ильин", "Исаев", "Кабанов", "Казаков",
            "Калинин", "Карпов", "Киселёв", "Климов", "Ковалёв", "Козлов", "Колесников", "Комаров",
            "Кондратьев", "Королёв", "Костин", "Кошелев", "Красильников", "Крылов", "Кудрявцев", "Кузнецов",
            "Куликов", "Куприянов", "Лаврентьев", "Лазарев", "Лебедев", "Леонов", "Логинов", "Лукин",
            "Макаров", "Максимов", "Мамонтов", "Марков", "Мартынов", "Маслов", "Медведев", "Мельников",
            "Миронов", "Михайлов", "Морозов", "Мухин", "Назаров", "Наумов", "Некрасов", "Никитин",
            "Николаев", "Новиков", "Носков", "Овчинников", "Орехов", "Орлов", "Осипов", "Панов",
            "Пантелеев", "Пахомов", "Петров", "Пименов", "Поляков", "Пономарёв", "Попов", "Потапов",
            "Прохоров", "Родионов", "Рожков", "Романов", "Рыбаков", "Рябов", "Савельев", "Савин",
            "Сазонов", "Самойлов", "Сафонов", "Селезнёв", "Семёнов", "Сергеев", "Сидоров", "Симонов",
            "Ситников", "Смирнов", "Соболев", "Соколов", "Соловьёв", "Сорокин", "Степанов", "Столяров",
            "Субботин", "Суворов", "Тарасов", "Терентьев", "Тимофеев", "Титов", "Тихонов", "Третьяков",
            "Трофимов", "Туров", "Уваров", "Ульянов", "Устинов", "Фадеев", "Фёдоров", "Филиппов",
            "Фомин", "Фролов", "Харитонов", "Хохлов", "Цветков", "Чернов", "Чернышёв", "Чесноков",
            "Шаров", "Шестаков", "Шилов", "Ширяев", "Шубин", "Щербаков", "Щукин", "Юдин",
            "Яковлев", "Якушев"
    );

    private static final List<String> ES_ADULT = List.of(
            "Alejandro", "Álvaro", "Andrés", "Antonio", "Arturo", "Benito", "Bernardo", "Bruno",
            "Carlos", "César", "Cristóbal", "Daniel", "Diego", "Domingo", "Eduardo", "Emilio",
            "Enrique", "Ernesto", "Esteban", "Federico", "Felipe", "Fernando", "Francisco", "Gabriel",
            "Gonzalo", "Gregorio", "Guillermo", "Gustavo", "Héctor", "Horacio", "Hugo", "Ignacio",
            "Isidoro", "Iván", "Jacobo", "Javier", "Joaquín", "Jorge", "José", "Juan",
            "Julián", "Leandro", "León", "Lorenzo", "Lucas", "Luis", "Manuel", "Marcelo",
            "Marco", "Mario", "Martín", "Mateo", "Mauricio", "Miguel", "Nicolás", "Octavio",
            "Óscar", "Pablo", "Pedro", "Rafael", "Ramón", "Raúl", "Ricardo", "Roberto",
            "Rodrigo", "Rubén", "Salvador", "Samuel", "Santiago", "Sebastián", "Sergio", "Simón",
            "Teodoro", "Tomás", "Ulises", "Vicente", "Víctor", "Xavier"
    );

    private static final List<String> ES_CHILD = List.of(
            "Alvarito", "Andresito", "Antoñito", "Carlitos", "Cristianito", "Danilo", "Dieguito",
            "Eduardito", "Emilito", "Fernandito", "Francisquito", "Gabrielito", "Gonzalito", "Huguito",
            "Javito", "Jorgito", "Joselito", "Juanito", "Lucho", "Manolito", "Marquito", "Martiniano",
            "Miguelito", "Nicolás", "Pablito", "Pepito", "Rafita", "Ramoncito", "Ricardito", "Rodriguito",
            "Santi", "Sebitas", "Sergito", "Tomasito", "Vicentito"
    );

    private static final List<String> ES_SURNAMES = List.of(
            "Álvarez", "Aguilar", "Arroyo", "Benítez", "Blanco", "Cabrera", "Calvo", "Campos",
            "Carmona", "Carrasco", "Castillo", "Castro", "Cortés", "Cruz", "Delgado", "Díaz",
            "Domínguez", "Durán", "Escobar", "Esteban", "Fernández", "Ferrer", "Flores", "Fuentes",
            "Gallardo", "García", "Garrido", "Gil", "Gómez", "González", "Guerrero", "Gutiérrez",
            "Hernández", "Herrera", "Hidalgo", "Iglesias", "Jiménez", "Lara", "León", "López",
            "Lozano", "Marín", "Márquez", "Martín", "Martínez", "Medina", "Méndez", "Molina",
            "Montero", "Morales", "Moreno", "Moya", "Navarro", "Núñez", "Ortega", "Ortiz",
            "Otero", "Pacheco", "Padilla", "Pascual", "Pastor", "Peña", "Pérez", "Prieto",
            "Ramírez", "Ramos", "Reyes", "Rivas", "Rivera", "Robles", "Rodríguez", "Rojas",
            "Romero", "Rubio", "Ruiz", "Sáez", "Salas", "Sánchez", "Santana", "Santiago",
            "Sanz", "Serrano", "Soler", "Suárez", "Torres", "Vargas", "Vázquez", "Vega",
            "Velasco", "Vicente", "Vidal", "Villa"
    );

    private static final List<String> PT_ADULT = List.of(
            "Afonso", "Alberto", "Alexandre", "Álvaro", "André", "Antônio", "Armando", "Artur",
            "Augusto", "Benedito", "Bernardo", "Bruno", "Caio", "Carlos", "César", "Cláudio",
            "Cristiano", "Daniel", "Davi", "Dênis", "Diego", "Dimas", "Domingos", "Douglas",
            "Eduardo", "Elias", "Emílio", "Enrique", "Emanuel", "Erasmo", "Estêvão", "Fábio",
            "Felipe", "Fernando", "Filipe", "Flávio", "Francisco", "Frederico", "Gabriel", "Geraldo",
            "Gilberto", "Gonçalo", "Gustavo", "Heitor", "Henrique", "Hugo", "Igor", "Inácio",
            "Ivan", "Jaime", "Joaquim", "Jorge", "José", "João", "Júlio", "Leandro",
            "Leonardo", "Lucas", "Luciano", "Luís", "Manuel", "Marcelo", "Márcio", "Marco",
            "Mário", "Mateus", "Maurício", "Miguel", "Nelson", "Nicolau", "Otávio", "Paulo",
            "Pedro", "Rafael", "Raul", "Renato", "Ricardo", "Roberto", "Rodolfo", "Rodrigo",
            "Rogério", "Rúben", "Samuel", "Sandro", "Sérgio", "Silvio", "Simão", "Teodoro",
            "Thiago", "Tomás", "Valter", "Vasco", "Vicente", "Vítor", "Wagner", "Wesley"
    );

    private static final List<String> PT_CHILD = List.of(
            "Beto", "Biel", "Cacá", "Caio", "Dudu", "Dinho", "Fefê", "Gabi",
            "Gui", "Juca", "Juninho", "Léo", "Leléu", "Lucas", "Luquinhas", "Manu",
            "Marcelinho", "Mimi", "Nando", "Neguinho", "Pedrinho", "Pepê", "Rafa", "Renatinho",
            "Ricardinho", "Robinho", "Serginho", "Tiaguinho", "Téo", "Tunico", "Vitinho", "Zeca"
    );

    private static final List<String> PT_SURNAMES = List.of(
            "Almeida", "Alves", "Amaral", "Andrade", "Araújo", "Azevedo", "Barbosa", "Barros",
            "Batista", "Borges", "Braga", "Branco", "Camargo", "Campos", "Cardoso", "Carvalho",
            "Castro", "Coelho", "Correia", "Costa", "Cunha", "Cruz", "Cunha", "Dias",
            "Duarte", "Esteves", "Faria", "Fernandes", "Ferreira", "Fonseca", "Freitas", "Garcia",
            "Gomes", "Gonçalves", "Guedes", "Henriques", "Lima", "Lopes", "Machado", "Marques",
            "Martins", "Melo", "Mendes", "Monteiro", "Moraes", "Moreira", "Nascimento", "Neves",
            "Nogueira", "Nunes", "Oliveira", "Pacheco", "Paiva", "Pereira", "Pinto", "Pires",
            "Queiroz", "Ramos", "Reis", "Ribeiro", "Rocha", "Rodrigues", "Rosa", "Sampaio",
            "Santana", "Santos", "Silva", "Soares", "Sousa", "Teixeira", "Tavares", "Vieira"
    );

    private static final List<String> DE_ADULT = List.of(
            "Achim", "Adolf", "Albert", "Albrecht", "Alfred", "Andreas", "Ansgar", "Anton",
            "Armin", "Arndt", "Arnold", "Arthur", "Axel", "Baldur", "Bastian", "Bernd",
            "Bernhard", "Bertram", "Björn", "Bruno", "Carsten", "Christian", "Christoph", "Claus",
            "Clemens", "Conrad", "Curt", "Detlef", "Dietmar", "Dietrich", "Dieter", "Eberhard",
            "Eckart", "Edgar", "Egbert", "Egon", "Ekkehard", "Emil", "Erich", "Ernst",
            "Erwin", "Eugen", "Falk", "Felix", "Ferdinand", "Florian", "Frank", "Franz",
            "Friedrich", "Fritz", "Georg", "Gerhard", "Gerd", "Gottfried", "Günter", "Gustav",
            "Hannes", "Hans", "Harald", "Hartmut", "Heiko", "Heinrich", "Heinz", "Helmut",
            "Hendrik", "Herbert", "Hermann", "Holger", "Horst", "Hubert", "Ingo", "Jakob",
            "Joachim", "Jochen", "Johannes", "Jörg", "Josef", "Jürgen", "Karl", "Kaspar",
            "Klaus", "Konrad", "Kurt", "Lars", "Leonhard", "Lorenz", "Lothar", "Ludwig",
            "Manfred", "Markus", "Martin", "Matthias", "Maximilian", "Meinhard", "Michael", "Norbert",
            "Oskar", "Otto", "Paul", "Peter", "Rainer", "Ralf", "Reinhard", "Richard",
            "Robert", "Rolf", "Rudolf", "Sebastian", "Siegfried", "Stefan", "Sven", "Theodor",
            "Thomas", "Thorsten", "Tobias", "Udo", "Ulrich", "Uwe", "Volker", "Waldemar",
            "Walter", "Werner", "Wilhelm", "Wolfgang"
    );

    private static final List<String> DE_CHILD = List.of(
            "Achim", "Andi", "Basti", "Benno", "Berndl", "Bubi", "Christl", "Didi",
            "Emil", "Ferdl", "Fritzi", "Gerdi", "Goggo", "Hansi", "Heiner", "Heinzi",
            "Hubsi", "Jackl", "Jonas", "Kalle", "Karli", "Kasti", "Klemmi", "Lenni",
            "Luis", "Maxi", "Michi", "Mucki", "Nicki", "Olli", "Pepi", "Poldi",
            "Rudi", "Seppi", "Simi", "Steffi", "Toni", "Uli", "Vroni", "Wasti"
    );

    private static final List<String> DE_SURNAMES = List.of(
            "Albrecht", "Bachmann", "Bauer", "Baumann", "Beck", "Becker", "Berger", "Bergmann",
            "Böhm", "Brandt", "Braun", "Brunner", "Busch", "Dietrich", "Engel", "Ernst",
            "Fischer", "Frank", "Franke", "Frey", "Friedrich", "Fuchs", "Graf", "Groß",
            "Günther", "Haas", "Hahn", "Hartmann", "Hauser", "Heinrich", "Hermann", "Hertz",
            "Hoffmann", "Hofmann", "Horn", "Huber", "Jäger", "Janssen", "Kaiser", "Keller",
            "Klein", "Koch", "König", "Kramer", "Krause", "Krebs", "Krüger", "Kuhn",
            "Lang", "Lange", "Lehmann", "Lorenz", "Ludwig", "Maier", "Mayer", "Meier",
            "Meyer", "Möller", "Müller", "Neumann", "Otto", "Peters", "Pfeiffer", "Pohl",
            "Richter", "Riedel", "Roth", "Sauer", "Schäfer", "Scheer", "Schmid", "Schmidt",
            "Schneider", "Scholz", "Schröder", "Schubert", "Schulte", "Schulz", "Schumacher", "Schwarz",
            "Seidel", "Simon", "Stein", "Thomas", "Ulrich", "Vogel", "Vogt", "Wagner",
            "Walter", "Weber", "Wegner", "Weiß", "Werner", "Winkler", "Wolf", "Zimmermann"
    );

    private static final List<String> FR_ADULT = List.of(
            "Alain", "Albert", "Alexandre", "Alphonse", "André", "Antoine", "Armand", "Arnaud",
            "Arthur", "Auguste", "Baptiste", "Bastien", "Benjamin", "Bernard", "Bertrand", "Blaise",
            "Bruno", "Cédric", "Charles", "Christian", "Christophe", "Claude", "Clément", "Corentin",
            "Damien", "Daniel", "David", "Denis", "Didier", "Dominique", "Edmond", "Édouard",
            "Émile", "Emmanuel", "Éric", "Étienne", "Fabien", "Fabrice", "Fernand", "Florent",
            "François", "Frédéric", "Gabriel", "Gaël", "Gaston", "Georges", "Gérard", "Gilbert",
            "Gilles", "Grégoire", "Guillaume", "Guy", "Henri", "Hervé", "Honoré", "Hubert",
            "Hugo", "Jacques", "Jean", "Jérôme", "Joël", "Jules", "Julien", "Laurent",
            "Léon", "Louis", "Luc", "Lucien", "Marc", "Marcel", "Martin", "Mathieu",
            "Maurice", "Maxime", "Michel", "Nicolas", "Noël", "Olivier", "Pascal", "Patrice",
            "Patrick", "Paul", "Philippe", "Pierre", "Quentin", "Raymond", "Rémi", "René",
            "Richard", "Robert", "Rodolphe", "Roger", "Roland", "Romain", "Sébastien", "Serge",
            "Simon", "Stéphane", "Sylvain", "Théo", "Thierry", "Thomas", "Timothée", "Tristan",
            "Valentin", "Vincent", "Xavier", "Yann", "Yves"
    );

    private static final List<String> FR_CHILD = List.of(
            "Alexis", "Antoine", "Baptou", "Benoît", "Coco", "Dédé", "Fanfan", "Fifils",
            "Gégé", "Jojo", "Loulou", "Manu", "Mathis", "Mimile", "Nino", "Nounours",
            "Paulo", "Pierrot", "Roro", "Titou", "Toto", "Tutu", "Vivi", "Yoyo"
    );

    private static final List<String> FR_SURNAMES = List.of(
            "Adam", "André", "Arnaud", "Aubert", "Barbier", "Baron", "Baudry", "Bertin",
            "Blanc", "Blanchard", "Bonnet", "Boucher", "Bourgeois", "Boyer", "Brun", "Brunet",
            "Caron", "Chevalier", "Clément", "Colin", "Collet", "David", "Delorme", "Denis",
            "Deschamps", "Dubois", "Dubois", "Dufour", "Dumas", "Dumont", "Dupont", "Dupuis",
            "Durand", "Faure", "Fernandez", "Fischer", " Fontaine", "Fournier", "François", "Gaillard",
            "Garcia", "Garnier", "Gauthier", "Gérard", "Giraud", "Girard", "Guyot", "Henry",
            "Hubert", "Jacquet", "Jean", "Julien", "Lacroix", "Lambert", "Langlois", "Laurent",
            "Leclerc", "Lecomte", "Lefebvre", "Lefèvre", "Legrand", "Lemaire", "Lemoine", "Leroy",
            "Lesage", "Lopez", "Louis", "Marchand", "Marie", "Martin", "Masson", "Mathieu",
            "Mercier", "Meunier", "Meyer", "Michel", "Moreau", "Morel", "Moulin", "Nicolas",
            "Noël", "Olivier", "Perrin", "Perrot", "Petit", "Philippe", "Picard", "Pierre",
            "Poirier", "Renard", "Renaud", "Rey", "Rivière", "Robert", "Robin", "Roger",
            "Rousseau", "Roussel", "Roy", "Simon", "Thomas", "Vidal", "Vincent"
    );

    private static final List<String> RU_GOOD_ADULT = List.of(
            "добрый", "разговорчивый", "философ", "мечтатель", "оптимист", "заинтересованный",
            "любопытный", "спокойный", "заботливый", "весёлый"
    );

    private static final List<String> RU_BAD_ADULT = List.of(
            "злой", "скупой", "хвастун", "лентяй", "ворчун", "скептик", "завистливый", "угрюмый",
            "подозрительный", "высокомерный"
    );

    private static final List<String> RU_GOOD_CHILD = List.of("любопытный", "весёлый", "робкий", "мечтательный");
    private static final List<String> RU_BAD_CHILD = List.of("капризный", "пугливый", "ворчливый");

    private static final List<String> EN_GOOD_ADULT = List.of(
            "kind", "talkative", "philosopher", "dreamer", "optimist", "curious", "calm", "caring", "cheerful"
    );

    private static final List<String> EN_BAD_ADULT = List.of(
            "evil", "greedy", "boaster", "lazy", "grumpy", "skeptic", "envious", "sullen", "arrogant"
    );

    private static final List<String> EN_GOOD_CHILD = List.of("curious", "cheerful", "shy", "dreamy");
    private static final List<String> EN_BAD_CHILD = List.of("grumpy", "fearful", "whiny");

    private static final List<String> ES_GOOD_ADULT = List.of(
            "bondadoso", "conversador", "filósofo", "soñador", "optimista", "curioso",
            "tranquilo", "cariñoso", "alegre", "sociable"
    );

    private static final List<String> ES_BAD_ADULT = List.of(
            "malvado", "avaro", "fanfarrón", "perezoso", "gruñón", "escéptico", "envidioso",
            "huraño", "arrogante", "quejica"
    );

    private static final List<String> ES_GOOD_CHILD = List.of("curioso", "alegre", "tímido", "soñador");
    private static final List<String> ES_BAD_CHILD = List.of("gruñón", "miedoso", "quejica");

    private static final List<String> PT_GOOD_ADULT = List.of(
            "bondoso", "conversador", "filósofo", "sonhador", "otimista", "curioso",
            "tranquilo", "carinhoso", "alegre", "sociável"
    );

    private static final List<String> PT_BAD_ADULT = List.of(
            "malvado", "avarento", "fanfarrão", "preguiçoso", "rabugento", "cético", "invejoso",
            "arrogante", "queixoso", "desconfiado"
    );

    private static final List<String> PT_GOOD_CHILD = List.of("curioso", "alegre", "tímido", "sonhador");
    private static final List<String> PT_BAD_CHILD = List.of("rabugento", "medroso", "queixoso");

    private static final List<String> DE_GOOD_ADULT = List.of(
            "gütig", "gesprächig", "Philosoph", "Träumer", "Optimist", "neugierig",
            "ruhig", "fürsorglich", "fröhlich", "gesellig"
    );

    private static final List<String> DE_BAD_ADULT = List.of(
            "böse", "geizig", "Angeber", "faul", "mürrisch", "Skeptiker", "neidisch",
            "grantig", "arrogant", "misstrauisch"
    );

    private static final List<String> DE_GOOD_CHILD = List.of("neugierig", "fröhlich", "schüchtern", "verträumt");
    private static final List<String> DE_BAD_CHILD = List.of("mürrisch", "ängstlich", "nörgelnd");

    private static final List<String> FR_GOOD_ADULT = List.of(
            "bienveillant", "bavard", "philosophe", "rêveur", "optimiste", "curieux",
            "calme", "attentionné", "joyeux", "sociable"
    );

    private static final List<String> FR_BAD_ADULT = List.of(
            "méchant", "avare", "vantard", "paresseux", "grognon", "sceptique", "envieux",
            "maussade", "arrogant", "méfiant"
    );

    private static final List<String> FR_GOOD_CHILD = List.of("curieux", "joyeux", "timide", "rêveur");
    private static final List<String> FR_BAD_CHILD = List.of("grognon", "craintif", "pleurnicheur");

    private static final List<String> TR_ADULT = List.of(
            "Abdullah", "Ahmet", "Ali", "Alper", "Arda", "Aslan", "Aydın", "Barış",
            "Berk", "Bilal", "Bora", "Bülent", "Cem", "Cemal", "Cengiz", "Deniz",
            "Doğan", "Efe", "Ekrem", "Emre", "Enes", "Engin", "Erdal", "Eren",
            "Erkan", "Erol", "Ertan", "Eser", "Faruk", "Fatih", "Ferhat", "Fikret",
            "Furkan", "Gökhan", "Gürkan", "Hakan", "Halil", "Hasan", "Hayri", "Hikmet",
            "Hüseyin", "İbrahim", "İlker", "İsmail", "Kadir", "Kaan", "Kemal", "Kerem",
            "Levent", "Mahmut", "Mert", "Metin", "Mehmet", "Murat", "Mustafa", "Necati",
            "Nuri", "Oğuz", "Okan", "Onur", "Orhan", "Osman", "Ömer", "Özkan",
            "Polat", "Ramazan", "Recep", "Rıza", "Sabri", "Salih", "Sedat", "Selim",
            "Serdar", "Serkan", "Sinan", "Süleyman", "Şahin", "Tarık", "Tolga", "Tuncay",
            "Turgut", "Uğur", "Umut", "Vedat", "Volkan", "Yasin", "Yavuz", "Yusuf",
            "Zafer", "Zeki"
    );

    private static final List<String> TR_CHILD = List.of(
            "Ahmetçik", "Aliş", "Ardacık", "Berkecik", "Boncuk", "Can", "Cemcük", "Cengo",
            "Efecik", "Emrecik", "Enecik", "Eray", "Erkut", "Fıstık", "Göktuğ", "Hasançık",
            "Kadircik", "Kaançık", "Keremcik", "Kuzey", "Mertçik", "Mertcan", "Metincik",
            "Muratçık", "Oğuzhan", "Onurcuk", "Ömercik", "Paşa", "Selimcik", "Sinançık",
            "Tolgaçık", "Uğurcuk", "Umutçuk", "Yusufçuk"
    );

    private static final List<String> TR_SURNAMES = List.of(
            "Ak", "Akar", "Akbaş", "Akdemir", "Akgün", "Aksoy", "Aktürk", "Altın",
            "Arslan", "Aslan", "Ateş", "Avcı", "Aydın", "Aygün", "Balcı", "Barut",
            "Başaran", "Bayrak", "Bayram", "Bilgin", "Bozkurt", "Bulut", "Cengiz", "Coşkun",
            "Ceylan", "Çakır", "Çelik", "Çetin", "Demir", "Demirci", "Deniz", "Doğan",
            "Duman", "Duran", "Durmaz", "Erdem", "Erdoğan", "Eren", "Ergin", "Eroğlu",
            "Güneş", "Güler", "Gültekin", "Gündoğdu", "Gürbüz", "Işık", "Kara", "Karaca",
            "Kaya", "Keskin", "Kılıç", "Koç", "Korkmaz", "Köse", "Kurt", "Kurtuluş",
            "Mutlu", "Ocak", "Oğuz", "Özdemir", "Özkan", "Öztürk", "Polat", "Sarı",
            "Savaş", "Şahin", "Şen", "Taş", "Tekin", "Toprak", "Turan", "Türk",
            "Uçar", "Uysal", "Ünal", "Yalçın", "Yavuz", "Yazıcı", "Yıldırım", "Yıldız",
            "Yılmaz", "Yücel", "Zorlu"
    );

    private static final List<String> PL_ADULT = List.of(
            "Adam", "Adrian", "Albert", "Aleksander", "Andrzej", "Antoni", "Arkadiusz", "Artur",
            "Bartosz", "Błażej", "Bogdan", "Bogumił", "Bolesław", "Bronisław", "Cezary", "Czesław",
            "Damian", "Daniel", "Dariusz", "Dawid", "Dominik", "Edward", "Emil", "Eugeniusz",
            "Filip", "Franciszek", "Grzegorz", "Gustaw", "Henryk", "Hubert", "Ignacy", "Igor",
            "Iwona", "Jacek", "Jakub", "Jan", "Janusz", "Jarosław", "Jerzy", "Józef",
            "Kacper", "Kamil", "Karol", "Kazimierz", "Klaudiusz", "Konrad", "Krystian", "Krzysztof",
            "Leszek", "Lech", "Łukasz", "Maciej", "Marcin", "Marek", "Mariusz", "Mateusz",
            "Michał", "Mieczysław", "Mikołaj", "Miłosz", "Norbert", "Oskar", "Patryk", "Paweł",
            "Piotr", "Przemysław", "Radosław", "Rafał", "Robert", "Roman", "Ryszard", "Sławomir",
            "Stanisław", "Stefan", "Sylwester", "Szymon", "Tadeusz", "Tomasz", "Wacław", "Waldemar",
            "Wiesław", "Wiktor", "Witold", "Władysław", "Wojciech", "Zbigniew", "Zdzisław", "Zygmunt"
    );

    private static final List<String> PL_CHILD = List.of(
            "Adasiek", "Bartuś", "Bolko", "Bolek", "Franuś", "Gustlik", "Henio", "Ignacy",
            "Jaś", "Jędrek", "Kajtek", "Kacperek", "Kostek", "Krzyś", "Lolek", "Maciuś",
            "Mietek", "Mikołajek", "Miś", "Oskarek", "Patryczek", "Piotruś", "Rysiek", "Staś",
            "Szymek", "Tomek", "Wacio", "Wituś", "Wojtuś", "Zbyś"
    );

    private static final List<String> PL_SURNAMES = List.of(
            "Adamczyk", "Bąk", "Baran", "Bąk", "Bednarek", "Bielecki", "Borkowski", "Borowski",
            "Brzozowski", "Czarnecki", "Czerwiński", "Dąbrowski", "Duda", "Dudek", "Falkowski", "Gajewski",
            "Górski", "Grabowski", "Jabłoński", "Jakubowski", "Jankowski", "Jaworski", "Jóźwiak", "Kaczmarek",
            "Kamiński", "Kaźmierczak", "Kowal", "Kowalczyk", "Kowalski", "Kozłowski", "Krajewski", "Krawczyk",
            "Król", "Kubiak", "Kucharski", "Kwiatkowski", "Lewandowski", "Lis", "Maciejewski", "Majewski",
            "Malinowski", "Marciniak", "Mazur", "Michalski", "Mróz", "Nowak", "Nowakowski", "Olszewski",
            "Ostrowski", "Pawlak", "Pawłowski", "Piątek", "Piotrowski", "Polak", "Przybylski", "Rutkowski",
            "Sadowski", "Sawicki", "Sikora", "Sikorski", "Sobczak", "Stępień", "Szczepański", "Szewczyk",
            "Szymański", "Tomaszewski", "Urbański", "Walczak", "Wesołowski", "Wilk", "Witkowski", "Wójcik",
            "Wojciechowski", "Wolski", "Wróbel", "Wróblewski", "Zając", "Zalewski", "Zawadzki", "Zieliński",
            "Ziółkowski", "Żak", "Żmuda"
    );

    private static final List<String> IT_ADULT = List.of(
            "Alessandro", "Alessio", "Alberto", "Aldo", "Amedeo", "Andrea", "Angelo", "Antonio",
            "Armando", "Arturo", "Augusto", "Battista", "Beniamino", "Bernardo", "Bruno", "Calogero",
            "Carlo", "Cesare", "Claudio", "Cristiano", "Daniele", "Dario", "Davide", "Diego",
            "Domenico", "Donato", "Edoardo", "Emilio", "Enrico", "Enzo", "Ernesto", "Ettore",
            "Ezio", "Fabio", "Fabrizio", "Federico", "Filippo", "Fiorello", "Flavio", "Franco",
            "Gabriele", "Gaetano", "Gennaro", "Giacomo", "Giancarlo", "Gianni", "Gigi", "Gino",
            "Giorgio", "Giovanni", "Giovanni", "Giulio", "Giuseppe", "Graziano", "Guido", "Ignazio",
            "Ivano", "Lamberto", "Lauro", "Leonardo", "Lorenzo", "Luca", "Luciano", "Luigi",
            "Marcello", "Marco", "Mario", "Massimo", "Matteo", "Maurizio", "Michele", "Nicolò",
            "Nicola", "Orazio", "Oreste", "Osvaldo", "Ottavio", "Paolo", "Pasquale", "Pietro",
            "Primo", "Raffaele", "Renato", "Riccardo", "Roberto", "Rocco", "Rodolfo", "Romeo",
            "Rosario", "Salvatore", "Sandro", "Sergio", "Silvio", "Simone", "Stefano", "Tiziano",
            "Tommaso", "Ubaldo", "Ugo", "Valerio", "Vincenzo", "Vittorio"
    );

    private static final List<String> IT_CHILD = List.of(
            "Albertino", "Andreino", "Bepi", "Bepino", "Berto", "Ciccio", "Ciccillo", "Dino",
            "Duccio", "Ercolino", "Fefè", "Giannino", "Gigetto", "Gigino", "Ginetto", "Lello",
            "Lillo", "Luchino", "Mimmo", "Nando", "Ninetto", "Nino", "Peppino", "Peppuccio",
            "Pippo", "Raffaellino", "Riccetto", "Rocco", "Sandrino", "Tino", "Tittì", "Tonino",
            "Totò", "Ugoletto", "Vincenzino"
    );

    private static final List<String> IT_SURNAMES = List.of(
            "Amato", "Barbieri", "Basso", "Battaglia", "Bellini", "Benedetti", "Bernardi", "Bertolini",
            "Bianchi", "Bianco", "Bonetti", "Bruno", "Cairo", "Cammarata", "Caputo", "Carbone",
            "Caruso", "Cassano", "Castellani", "Cattaneo", "Cavallo", "Colombo", "Conti", "Coppola",
            "Costa", "D'Angelo", "De Luca", "De Santis", "Esposito", "Fabbri", "Ferrara", "Ferrari",
            "Ferretti", "Fiore", "Fontana", "Galli", "Gallo", "Gatti", "Gentile", "Giordano",
            "Giuliani", "Greco", "Guerra", "Leone", "Lombardi", "Longo", "Mancini", "Marchetti",
            "Marchi", "Marini", "Martinelli", "Marino", "Mazza", "Messina", "Milani", "Molinari",
            "Montanari", "Morelli", "Moretti", "Negri", "Neri", "Orlando", "Palmieri", "Palumbo",
            "Parisi", "Pellegrini", "Pellegrino", "Piras", "Rinaldi", "Riva", "Rizzi", "Rizzo",
            "Romano", "Rossi", "Russo", "Sala", "Sanna", "Santoro", "Sartori", "Serra",
            "Silvestri", "Sorrentino", "Spinelli", "Testa", "Valentini", "Villa", "Vitale", "Zani"
    );

    private static final List<String> TR_GOOD_ADULT = List.of(
            "iyi kalpli", "konuşkan", "filozof", "hayalperest", "iyimser", "meraklı",
            "sakin", "şefkatli", "neşeli", "sosyal"
    );

    private static final List<String> TR_BAD_ADULT = List.of(
            "kötü", "cimri", "övüngen", "tembel", "huysuz", "şüpheci", "kıskanç",
            "somurtkan", "kibirli", "mızmız"
    );

    private static final List<String> TR_GOOD_CHILD = List.of("meraklı", "neşeli", "utangaç", "hayalperest");
    private static final List<String> TR_BAD_CHILD = List.of("huysuz", "korkak", "mızmız");

    private static final List<String> PL_GOOD_ADULT = List.of(
            "życzliwy", "rozmowny", "filozof", "marzyciel", "optymista", "ciekawski",
            "spokojny", "troskliwy", "wesoły", "towarzyski"
    );

    private static final List<String> PL_BAD_ADULT = List.of(
            "złośliwy", "skąpy", "samochwała", "leniwy", "zrzędliwy", "sceptyk", "zawistny",
            "ponury", "arogancki", "marudny"
    );

    private static final List<String> PL_GOOD_CHILD = List.of("ciekawski", "wesoły", "nieśmiały", "marzyciel");
    private static final List<String> PL_BAD_CHILD = List.of("zrzędliwy", "strachliwy", "marudny");

    private static final List<String> IT_GOOD_ADULT = List.of(
            "buono", "loquace", "filosofo", "sognatore", "ottimista", "curioso",
            "tranquillo", "premuroso", "allegro", "socievole"
    );

    private static final List<String> IT_BAD_ADULT = List.of(
            "malvagio", "avaro", "fanfarone", "pigro", "brontolone", "scettico", "invidioso",
            "musone", "arrogante", "lamentoso"
    );

    private static final List<String> IT_GOOD_CHILD = List.of("curioso", "allegro", "timido", "sognatore");
    private static final List<String> IT_BAD_CHILD = List.of("brontolone", "pauroso", "lamentoso");

    /**
     * Normalised language code, one of {@link Lang#ALL}.
     *
     * <p>Delegates to {@link Lang} so the locale mapping lives in exactly one place.
     */
    public static String normalise(String languageCode) {
        return Lang.normalise(languageCode);
    }

    static List<String> pool(String lang, boolean child) {
        switch (Lang.normalise(lang)) {
            case Lang.RU:
                return child ? RU_CHILD : RU_ADULT;
            case Lang.ES:
                return child ? ES_CHILD : ES_ADULT;
            case Lang.PT:
                return child ? PT_CHILD : PT_ADULT;
            case Lang.DE:
                return child ? DE_CHILD : DE_ADULT;
            case Lang.FR:
                return child ? FR_CHILD : FR_ADULT;
            case Lang.TR:
                return child ? TR_CHILD : TR_ADULT;
            case Lang.PL:
                return child ? PL_CHILD : PL_ADULT;
            case Lang.IT:
                return child ? IT_CHILD : IT_ADULT;
            case Lang.EN:
            default:
                return child ? EN_CHILD : EN_ADULT;
        }
    }

    /**
     * Names usable at any age.
     *
     * <p>The name must not depend on whether the villager is currently a child: a baby that grows up
     * kept its UUID, so if the pool changed with age the villager would be renamed mid-life while its
     * conversation history stayed behind. Both pools are therefore merged, and both kinds of name
     * read plausibly for an adult or a child.
     */
    static List<String> namePool(String lang) {
        List<String> adults = pool(lang, false);
        List<String> children = pool(lang, true);
        List<String> merged = new java.util.ArrayList<>(adults.size() + children.size());
        merged.addAll(adults);
        merged.addAll(children);
        return merged;
    }

    /** Personality traits usable at any age, for the same reason as {@link #namePool(String)}. */
    static List<String> personalityPool(String lang, boolean good) {
        List<String> adults = personalities(lang, good, false);
        List<String> children = personalities(lang, good, true);
        List<String> merged = new java.util.ArrayList<>(adults.size() + children.size());
        merged.addAll(adults);
        merged.addAll(children);
        return merged;
    }

    /**
     * Surname pool. {@code null} means this language uses no surnames.
     *
     * <p>Names alone collided far too often: with 128 first names, ten villagers already collided
     * 30% of the time and twenty collided 79% of the time. Adding a surname multiplies the pool by
     * the number of surnames, which brings the chance of two identical villagers down to noise
     * without having to depend on load order, which would rename villagers between sessions.
     */
    static List<String> surnamePool(String lang) {
        switch (Lang.normalise(lang)) {
            case Lang.RU:
                return RU_SURNAMES;
            case Lang.ES:
                return ES_SURNAMES;
            case Lang.PT:
                return PT_SURNAMES;
            case Lang.DE:
                return DE_SURNAMES;
            case Lang.FR:
                return FR_SURNAMES;
            case Lang.TR:
                return TR_SURNAMES;
            case Lang.PL:
                return PL_SURNAMES;
            case Lang.IT:
                return IT_SURNAMES;
            case Lang.EN:
            default:
                return EN_SURNAMES;
        }
    }

    static List<String> personalities(String lang, boolean good, boolean child) {
        switch (Lang.normalise(lang)) {
            case Lang.RU:
                if (child) {
                    return good ? RU_GOOD_CHILD : RU_BAD_CHILD;
                }
                return good ? RU_GOOD_ADULT : RU_BAD_ADULT;
            case Lang.ES:
                if (child) {
                    return good ? ES_GOOD_CHILD : ES_BAD_CHILD;
                }
                return good ? ES_GOOD_ADULT : ES_BAD_ADULT;
            case Lang.PT:
                if (child) {
                    return good ? PT_GOOD_CHILD : PT_BAD_CHILD;
                }
                return good ? PT_GOOD_ADULT : PT_BAD_ADULT;
            case Lang.DE:
                if (child) {
                    return good ? DE_GOOD_CHILD : DE_BAD_CHILD;
                }
                return good ? DE_GOOD_ADULT : DE_BAD_ADULT;
            case Lang.FR:
                if (child) {
                    return good ? FR_GOOD_CHILD : FR_BAD_CHILD;
                }
                return good ? FR_GOOD_ADULT : FR_BAD_ADULT;
            case Lang.TR:
                if (child) {
                    return good ? TR_GOOD_CHILD : TR_BAD_CHILD;
                }
                return good ? TR_GOOD_ADULT : TR_BAD_ADULT;
            case Lang.PL:
                if (child) {
                    return good ? PL_GOOD_CHILD : PL_BAD_CHILD;
                }
                return good ? PL_GOOD_ADULT : PL_BAD_ADULT;
            case Lang.IT:
                if (child) {
                    return good ? IT_GOOD_CHILD : IT_BAD_CHILD;
                }
                return good ? IT_GOOD_ADULT : IT_BAD_ADULT;
            case Lang.EN:
            default:
                if (child) {
                    return good ? EN_GOOD_CHILD : EN_BAD_CHILD;
                }
                return good ? EN_GOOD_ADULT : EN_BAD_ADULT;
        }
    }
}
