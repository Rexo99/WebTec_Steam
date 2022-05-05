# Beispiel RESTful API mit JAX-RS

Um eine RESTful API mit JAX-RS gut entwickeln zu können, sollte man eine Entwicklungsumgebung einrichten. In diesem Runbook ist die Einrichtung einer solchen beschrieben.

## Einrichten der Umgebung

1. Installieren Sie ein **Java Development Kit (JDK)** in einer aktuellen Version. Um zu überprüfen, ob die Installation erfolgreich war, können Sie folgenden Befehl verwenden:

    ```bash
    java --version 
    ```

    Folgende Ausgabe sollte in der Konsole erscheinen: 
    ```
    openjdk 18 2022-03-22
    OpenJDK Runtime Environment Homebrew (build 18+0)
    OpenJDK 64-Bit Server VM Homebrew (build 18+0, mixed mode, sharing)
    ```

2. Laden Sie sich einen aktuellen **JakartaEE Container** (z.B. Glassfish [https://glassfish.org](https://glassfish.org)) herunter und entpacken Sie das ZIP File auf Ihrem System an geeigneter Stelle. Je nach Betriebssystem können Sie dafür den Paketmanager verwenden. Hier entpacken wir dieses ZIP File einfach in dem Unterordner, in dem sich diese Datei befindet. Die Dateistruktur  muss wie folgt aussehen:

    ```bash
    demo
    |- glassfish6
        |- bin
        |- glassfish
        |- javadb
        |- META-INF
        |- mq
    |- pizza
        |- src
        |- pom.xml
    |- README.md
    ```

3. Setzen Sie mit dem folgenden Befehl den Zugriffsmodus des Adminskriptes von Glassfish auf "Ausführen".

    ```bash
    chmod +x glassfish/bin/asaadmin
    ```

4. Starten Sie Glassfish:

    ```bash
    ./glassfish6/bin/asadmin start-domain
    ````

    Output:
    ```
    Waiting for domain1 to start ...
    Successfully started the domain : domain1
    domain  Location: /Users/user/hsh/wt/abschlussaufgabe/glassfish6/glassfish/domains/domain1
    Log File: /Users/user/hsh/wt/abschlussaufgabe/glassfish6/glassfish/domains/domain1/logs/server.log
    Admin Port: 4848
    Command start-domain executed successfully.
    ```

    Wenn Sie Glassfish stoppen möchten, verwenden Sie folgenden Befehl:

    ```bash
    ./glassfish6/bin/asadmin start-domain
    ```

5. Stellen Sie sicher, dass Sie die Weboberfläche von Glassfish erreichen können, in dem Sie folgende Webseite in Ihrem Browser öffnen: [http://localhost:4848](http://localhost:4848)

    ![Glassfish Admin Oberfläche](assets/glassfish-admin.png)

## Einrichten eines JAX-RS Projektes

Ein Beispiel-Projekt (zum Thema Pizza) finden Sie im Ordner "pizza". Sie können dieses Projekt als Vorlage verwenden oder  eigene Projekt erzeugen.

Das Beispiel-Projekt wurde mit dem Software-Project-Managment Tool [Maven](https://maven.apache.org/) erstellt. Dieses Tool können Sie entweder manuell oder je nach Betriebssystem mit dem integrierten Paketmanager installieren.

Wechseln Sie zunächst mit der Konsole in den pizza Ordner:

```bash
cd pizza
```

Dort befindet sich die Projekt-Datei, die sogenannte **Project-Object-Model** [pom.xml](pizza/pom.xml), die die wichtigsten Projekteinstellungen (z.B. Projektnamen, Java Version, Abhängigkeiten und Deployment-Konfigurationen) enthält. Führt man Maven in einem Ordner mit einer pom.xml Datei aus, wird diese automatisch von Maven eingelesen.


### Wichtigste Maven Befehle

Maven bietet weit mehr Funktionen als wir in diesem Projekt benötigen. Daher folgt hier eine Liste mit den wichtigsten Befehlen. 

1.  Installation und Java Konfiguration überprüfen:
    ```bash
    mvn --version
    ```

2. Installation aller Abhängigkeiten
    ```bash
    mvn install
    ```

3. Kompilieren eines Projektes
    ```bash
    mvn package
    ```
    Dieser Befehl führt zudem automatisch alle Tests aus. Mit dem Parameter `-DskipTests=true` kann dies deaktiviert werden. Dieser Befehl erstellt einen neuen target-Ordner, in dem das Ergebnis des Kompilierens abgelegt wird.
    ```bash
    pizza
    |- target
      |- classes
      |- generated-sources
      |- maven-archiver
      |- maven-status
      |- pizza-1.0-SNAPSHOT
      |- test-classes
      |- pizza-1.0-SNAPSHOT.war
    ```
    Die Anwendung wird als sogenanntes Webarchive (pizza-1.0-SNAPSHOT.war) abgelegt. Diese Datei enthält ausschließlich die von der Anwendung benötigten Klassen. Wie in der Vorlesung erklärt wurde, sind dort weder eine Java-Umgebung, noch die JAX-RS Implementierung abgelegt. Diese werden vom Server-Host bzw. Servlet-Container bereitgestellt.

4. Um die Anwendung in dem zuvor eingerichteten Servlet-Container bereitzustellen, kann folgender Maven-Befehl verwendet werden:
    ```bash
    mvn cargo:deploy
    ```

    Um eine bereits bereitgestellte Anwendung zu aktualisieren kann folgender Befehl verwendet werden:
    ```bash
    mven cargo:redeploy
    ````

5. Alle wichtigten Schritte (Testen, Kompilieren, Bereitstellen) können auch hintereinander mit folgendem Kommando ausgeführt werden:
    ```bash
    mvn package cargo:redeploy
    ```

## Kontrolle

Wenn alle Schritte erfolgreich ausgeführt wurden, können Sie unter [http://localhost:8080/pizza](http://localhost:8080/pizza) auf die Anwendung zugreifen.